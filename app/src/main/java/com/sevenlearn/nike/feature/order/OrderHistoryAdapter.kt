package com.sevenlearn.nike.feature.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.generic.RoundingParams
import com.sevenlearn.nike.R
import com.sevenlearn.nike.common.convertDpToPixel
import com.sevenlearn.nike.common.formatPrice
import com.sevenlearn.nike.view.NikeImageView
import com.sevenlearn.nikestore.data.OrderHistoryItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_order_history.view.*

class OrderHistoryAdapter(val context: Context, val orders: List<OrderHistoryItem>) :
    Adapter<OrderHistoryAdapter.ViewHolder>() {

    val params: LinearLayout.LayoutParams
    val raundParams:RoundingParams

    init {
        val size = convertDpToPixel(100f, context).toInt()
        val margin = convertDpToPixel(16f, context).toInt()
        val round = convertDpToPixel(8f,context)
        params = LinearLayout.LayoutParams(size, size)
        params.setMargins(margin, 0, 0, 0)
        raundParams = RoundingParams.fromCornersRadius(round)

    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(orderHistoryItem: OrderHistoryItem) {
            containerView.orderIdTv.text = orderHistoryItem.id.toString()
            containerView.orderAmountTv.text = formatPrice(orderHistoryItem.payable)
            containerView.orderDateTv.text = orderHistoryItem.date.replace(","," ").replace(".","/")
            containerView.orderProductLn.removeAllViews()
            orderHistoryItem.order_items.forEach {
                    val imageView = NikeImageView(context)
                    imageView.setImageURI(it.product.image)
                    imageView.layoutParams = params
                    containerView.orderProductLn.addView(imageView)
                    imageView.hierarchy = GenericDraweeHierarchyBuilder(context.resources)
                        .setRoundingParams(raundParams)
                        .build();
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_order_history, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int {
        return orders.size
    }
}