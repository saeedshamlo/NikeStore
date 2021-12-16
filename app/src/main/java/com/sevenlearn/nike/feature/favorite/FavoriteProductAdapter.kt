package com.sevenlearn.nike.feature.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sevenlearn.nike.R
import com.sevenlearn.nike.data.Product
import com.sevenlearn.nike.services.ImageLoadingService
import com.sevenlearn.nike.view.NikeImageView

class FavoriteProductAdapter(
    var products: MutableList<Product>,
    var favoriteProductEventListener: FavoriteProductEventListener,
    val imageLoadingService: ImageLoadingService
) : RecyclerView.Adapter<FavoriteProductAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title = itemView.findViewById<TextView>(R.id.productTitleTv)
        val cover = itemView.findViewById<NikeImageView>(R.id.productIv)
        fun bind(product: Product) {
            title.text = product.title
            imageLoadingService.load(cover, product.image)

            itemView.setOnClickListener {
                favoriteProductEventListener.onclick(product)
            }
            itemView.setOnLongClickListener {
                products.remove(product)
                notifyItemRemoved(adapterPosition)
                favoriteProductEventListener.onLongClick(product)
                false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_favorite_product, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int {
        return products.size
    }

    interface FavoriteProductEventListener {
        fun onclick(product: Product)
        fun onLongClick(product: Product)
    }
}