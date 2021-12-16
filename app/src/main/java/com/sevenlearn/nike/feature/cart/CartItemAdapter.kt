package com.sevenlearn.nike.feature.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sevenlearn.nike.R
import com.sevenlearn.nike.common.formatPrice
import com.sevenlearn.nike.services.ImageLoadingService
import com.sevenlearn.nikestore.data.CartItem
import com.sevenlearn.nikestore.data.PurchaseDetail
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_cart.view.*
import kotlinx.android.synthetic.main.item_purchase_detail.view.*


const val VIEW_TYPE_CART_ITEM = 0
const val VIEW_TYPE_PURCHASE_DETAIL = 1

class CartItemAdapter(
    val cartItems: MutableList<CartItem>,
    val imageLoadingService: ImageLoadingService,
    val cartItemViewCallBack: CartItemViewCallBack
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var purchaseDetail:PurchaseDetail? = null
    inner class CartItemViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bindCartItem(cartItem: CartItem) {
            containerView.productTitleTv.text = cartItem.product.title
            containerView.cartItemCountTv.text = cartItem.count.toString()
            containerView.previousPriceTv.text =
                formatPrice(cartItem.product.previous_price + cartItem.product.discount)
            containerView.orderPriceTv.text = formatPrice(cartItem.product.price)
            imageLoadingService.load(containerView.productIv, cartItem.product.image)

            containerView.removeFromCartBtn.setOnClickListener {
                cartItemViewCallBack.onRemoveCartBtnClick(cartItem)
            }

            containerView.changeCountProgress.visibility =
                if (cartItem.changeCountProgressBarIsVisible) View.VISIBLE else View.GONE

            containerView.cartItemCountTv.visibility = if (cartItem.changeCountProgressBarIsVisible) View.INVISIBLE else View.VISIBLE

            containerView.increaseBtn.setOnClickListener {
                cartItem.changeCountProgressBarIsVisible = true
                containerView.changeCountProgress.visibility = View.VISIBLE
                containerView.cartItemCountTv.visibility = View.INVISIBLE
                cartItemViewCallBack.onIncreaseCartItemBtnClick(cartItem)
            }

            containerView.decreaseBtn.setOnClickListener {
                if (cartItem.count > 1) {
                    cartItem.changeCountProgressBarIsVisible = true
                    containerView.changeCountProgress.visibility = View.VISIBLE
                    containerView.cartItemCountTv.visibility = View.INVISIBLE
                    cartItemViewCallBack.onDecreaseCartItemBtnClick(cartItem)
                }
            }

            containerView.productIv.setOnClickListener {
                cartItemViewCallBack.onProductImageClick(cartItem)
            }

        }
    }


     class PurchaseDetailViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bindPurchase(totalPrice: Int, shipingPrice: Int, payablePrice: Int) {
            containerView.totalPriceTv.text = formatPrice(totalPrice)
            containerView.shipingtTv.text = formatPrice(shipingPrice)
            containerView.payablePriceTv.text = formatPrice(payablePrice)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_CART_ITEM) {
            CartItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
            )
        } else {
            PurchaseDetailViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_purchase_detail, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       if(holder is CartItemViewHolder){
           holder.bindCartItem(cartItems[position])
       }
        else if(holder is PurchaseDetailViewHolder){
            purchaseDetail?.let {
                holder.bindPurchase(it.totalPrice,it.shipping_cost,it.payable_price)
            }
       }
    }


    override fun getItemCount(): Int {
        return cartItems.size + 1
    }


    override fun getItemViewType(position: Int): Int {
        if (position == cartItems.size) {
            return VIEW_TYPE_PURCHASE_DETAIL
        } else
            return VIEW_TYPE_CART_ITEM
    }

    interface CartItemViewCallBack {
        fun onRemoveCartBtnClick(cartItem: CartItem)
        fun onIncreaseCartItemBtnClick(cartItem: CartItem)
        fun onDecreaseCartItemBtnClick(cartItem: CartItem)
        fun onProductImageClick(cartItem: CartItem)
    }

    fun removeCartItem(cartItem: CartItem){
        val index = cartItems.indexOf(cartItem)
        if (index>-1){
            cartItems.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun increaseCount(cartItem: CartItem){
        val index = cartItems.indexOf(cartItem)
        if (index>-1){
            cartItems[index].changeCountProgressBarIsVisible =false
            notifyItemChanged(index)
        }
    }
    fun decreaseCount(cartItem: CartItem){
        val index = cartItems.indexOf(cartItem)
        if (index>-1){
            cartItems[index].changeCountProgressBarIsVisible = false
            notifyItemChanged(index)
        }
    }
}