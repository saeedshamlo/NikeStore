package com.sevenlearn.nike.feature.cart

import androidx.lifecycle.MutableLiveData
import com.sevenlearn.nike.R
import com.sevenlearn.nike.common.NikeSingleObserver
import com.sevenlearn.nike.common.NikeViewModel
import com.sevenlearn.nike.common.asyncNetworkRequest
import com.sevenlearn.nike.data.EmptyState
import com.sevenlearn.nike.data.TokenContainer
import com.sevenlearn.nike.data.repo.CartRepository
import com.sevenlearn.nikestore.data.CartItem
import com.sevenlearn.nikestore.data.CartItemCount
import com.sevenlearn.nikestore.data.CartResponse
import com.sevenlearn.nikestore.data.PurchaseDetail
import io.reactivex.Completable
import org.greenrobot.eventbus.EventBus

class CartViewModel(val cartRepository: CartRepository) : NikeViewModel() {


    val cartItemLiveData = MutableLiveData<List<CartItem>>()
    val purchaseDetailLiveData = MutableLiveData<PurchaseDetail>()
    val emptyStateLiveData = MutableLiveData<EmptyState>()

    private fun getCartItems() {
        if (!TokenContainer.token.isNullOrEmpty()) {
            progressBarLiveData.value = true
            emptyStateLiveData.value = EmptyState(false)
            cartRepository.get().asyncNetworkRequest()
                .doFinally { progressBarLiveData.value = false }
                .subscribe(object : NikeSingleObserver<CartResponse>(compositeDisposable) {
                    override fun onSuccess(t: CartResponse) {
                        if (t.cart_items.isNotEmpty()) {
                            cartItemLiveData.value = t.cart_items
                            purchaseDetailLiveData.value =
                                PurchaseDetail(t.total_price, t.shipping_cost, t.payable_price)
                        }
                        else{
                            emptyStateLiveData.value = EmptyState(true, R.string.cartEmptyState)
                        }
                    }

                })
        }
        else{
            emptyStateLiveData.value = EmptyState(true,R.string.cartEmptyStateLogin,true)
        }
    }

    fun removeItemFromCart(cartItem: CartItem): Completable {
        return cartRepository.remove(cartItem.cart_item_id)
            .doAfterSuccess {
                calculateAndPublishPurchaseDetail()

                val cartItemCount = EventBus.getDefault().getStickyEvent(CartItemCount::class.java)
                cartItemCount?.let {
                    it.count -= cartItem.count
                    EventBus.getDefault().postSticky(it)
                }

                cartItemLiveData.value?.let {
                    if(it.isEmpty()){
                        emptyStateLiveData.postValue( EmptyState(true, R.string.cartEmptyState))
                    }
                }
            }.ignoreElement()
    }

    fun increaseCartItemCount(cartItem: CartItem): Completable {
        return cartRepository.changeCount(cartItem.cart_item_id, ++cartItem.count)
            .doAfterSuccess {
                calculateAndPublishPurchaseDetail()
                val cartItemCount = EventBus.getDefault().getStickyEvent(CartItemCount::class.java)
                cartItemCount?.let {
                    it.count +=1
                    EventBus.getDefault().postSticky(it)
                }
            }
            .ignoreElement()
    }

    fun decreaseCartItemCount(cartItem: CartItem): Completable {
        return cartRepository.changeCount(cartItem.cart_item_id, --cartItem.count)
            .doAfterSuccess {
                calculateAndPublishPurchaseDetail()
                val cartItemCount =EventBus.getDefault().getStickyEvent(CartItemCount::class.java)
                cartItemCount?.let {
                    it.count -=1
                    EventBus.getDefault().postSticky(it)
                }
            }
            .ignoreElement()

    }

    fun refresh() {
        getCartItems()
    }

    private fun calculateAndPublishPurchaseDetail() {
        cartItemLiveData.value?.let { cartItems ->
            purchaseDetailLiveData.value?.let { purchaseDetail ->
                var totalPrice = 0
                var payeblePrice = 0
                cartItems.forEach {
                    totalPrice += it.product.price * it.count
                    payeblePrice += (it.product.price - it.product.discount) * it.count
                }
                purchaseDetail.totalPrice = totalPrice
                purchaseDetail.payable_price = payeblePrice
                purchaseDetailLiveData.postValue(purchaseDetail)
            }
        }
    }
}