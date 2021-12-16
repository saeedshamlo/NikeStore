package com.sevenlearn.nike.feature.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sevenlearn.nike.R
import com.sevenlearn.nike.common.EXTRA_KEY_DATA
import com.sevenlearn.nike.common.NikeCompletableObserver
import com.sevenlearn.nike.common.NikeFragment
import com.sevenlearn.nike.feature.auth.AuthActivity
import com.sevenlearn.nike.feature.product.ProductDetailActivity
import com.sevenlearn.nike.feature.shipping.ShippingActivity
import com.sevenlearn.nike.services.ImageLoadingService
import com.sevenlearn.nikestore.data.CartItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.view_empty_state.*
import kotlinx.android.synthetic.main.view_empty_state.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class CartFragment: NikeFragment(),CartItemAdapter.CartItemViewCallBack {

    val cartViewModel:CartViewModel by viewModel()
    var cartAdapter:CartItemAdapter? =null
    val imageLoadingService:ImageLoadingService by inject()
    val compositeDisposable = CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartViewModel.progressBarLiveData.observe(viewLifecycleOwner){
            setProgressIndicator(it)
        }

        cartViewModel.cartItemLiveData.observe(viewLifecycleOwner){
            cartItemsRv.layoutManager = LinearLayoutManager(requireContext())
            cartAdapter = CartItemAdapter(it as MutableList<CartItem>,imageLoadingService,this)
            cartItemsRv.adapter = cartAdapter
        }

        cartViewModel.purchaseDetailLiveData.observe(viewLifecycleOwner){
            cartAdapter?.let { adapter ->
                adapter.purchaseDetail = it
                adapter.notifyItemChanged(adapter.cartItems.size)
            }
        }

        cartViewModel.emptyStateLiveData.observe(viewLifecycleOwner){
            if (it.mustShow){
                val emptyState =showEmptyState(R.layout.view_empty_state)
                emptyState?.let {view ->
                    view.emptyStateMessage.text = getString(it.messageResId)
                    view.emptyStateCtaBtn.visibility = if (it.mustShowCallToAction)View.VISIBLE else View.GONE
                    view.emptyStateCtaBtn.setOnClickListener {
                        startActivity(Intent(requireContext(),AuthActivity::class.java))
                    }
                }
            }
            else
            {
                emptyView?.visibility=View.GONE
            }
        }

        payBtn.setOnClickListener {
            startActivity(Intent(requireActivity(),ShippingActivity::class.java)
                .apply {
                    putExtra(EXTRA_KEY_DATA,cartViewModel.purchaseDetailLiveData.value)
                })
        }
    }


    override fun onStart() {
        super.onStart()
        cartViewModel.refresh()
    }

    override fun onRemoveCartBtnClick(cartItem: CartItem) {
        cartViewModel.removeItemFromCart(cartItem)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :NikeCompletableObserver(compositeDisposable){
                override fun onComplete() {
                   cartAdapter?.removeCartItem(cartItem)
                }
            })
    }

    override fun onIncreaseCartItemBtnClick(cartItem: CartItem) {
        cartViewModel.increaseCartItemCount(cartItem)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :NikeCompletableObserver(compositeDisposable){
                override fun onComplete() {
                    cartAdapter?.increaseCount(cartItem)
                }
            })
    }

    override fun onDecreaseCartItemBtnClick(cartItem: CartItem) {
        cartViewModel.decreaseCartItemCount(cartItem)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :NikeCompletableObserver(compositeDisposable){
                override fun onComplete() {
                    cartAdapter?.decreaseCount(cartItem)
                }
            })
    }

    override fun onProductImageClick(cartItem: CartItem) {
        startActivity(Intent(requireContext(),ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA,cartItem.product)
        })
    }


    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
}