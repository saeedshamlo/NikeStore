package com.sevenlearn.nike.feature.common

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sevenlearn.nike.R
import com.sevenlearn.nike.common.formatPrice
import com.sevenlearn.nike.common.implementSpringAnimationTrait
import com.sevenlearn.nike.data.Product
import com.sevenlearn.nike.services.ImageLoadingService
import com.sevenlearn.nike.view.NikeImageView
import java.lang.IllegalStateException
import java.text.DecimalFormat

const val VIEW_TYPE_ROUND = 0
const val VIEW_TYPE_GRID = 1
const val VIEW_TYPE_LIST = 2

class ProductListAdapter(
    var viewType: Int = VIEW_TYPE_ROUND,
    var isViews:Boolean = false,
    val imageLoadingService: ImageLoadingService
) :
    RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    var onProductClickListener: OnProductClickListener? = null

    var products = ArrayList<Product>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productIv: NikeImageView = itemView.findViewById(R.id.productIv)
        val titleTv: TextView = itemView.findViewById(R.id.productTitleTv)
        val currentPriceTv: TextView = itemView.findViewById(R.id.currentPriceTv)
        val favoriteBtn = itemView.findViewById<ImageView>(R.id.favoriteBtn)
        val views = itemView.findViewById<TextView>(R.id.viewsTv)?:null
        val liner = itemView.findViewById<LinearLayout>(R.id.linerViews)?:null
        fun bindProduct(product: Product) {
            imageLoadingService.load(productIv, product.image)
            titleTv.text = product.title
            currentPriceTv.text = formatPrice(product.price)

            itemView.implementSpringAnimationTrait()
            itemView.setOnClickListener {
                onProductClickListener?.onProductClick(product)
            }
            favoriteBtn.setOnClickListener {
                onProductClickListener?.onFavoriteBtn(product)
                product.isFavorite = !product.isFavorite
                notifyItemChanged(adapterPosition)

            }
            if(product.isFavorite){
                favoriteBtn.setImageResource(R.drawable.ic_favorite_fill)
            }
            else
            {
                favoriteBtn.setImageResource(R.drawable.ic_favorites)
            }

            if (isViews){
                liner?.visibility = View.VISIBLE
                views?.text = product.views.toString()+ "+"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutResId = when (viewType) {
            VIEW_TYPE_ROUND -> R.layout.item_product
            VIEW_TYPE_GRID -> R.layout.item_product_grid
            VIEW_TYPE_LIST -> R.layout.item_product_list
            else -> throw IllegalStateException("View Type Error")
        }
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        )
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindProduct(products[position])

    override fun getItemCount(): Int = products.size

    interface OnProductClickListener {
        fun onProductClick(product: Product)
        fun onFavoriteBtn(product: Product)
    }
}