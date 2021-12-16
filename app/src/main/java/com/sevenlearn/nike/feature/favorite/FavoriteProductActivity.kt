package com.sevenlearn.nike.feature.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sevenlearn.nike.R
import com.sevenlearn.nike.common.EXTRA_KEY_DATA
import com.sevenlearn.nike.common.NikeActivity
import com.sevenlearn.nike.data.Product
import com.sevenlearn.nike.feature.product.ProductDetailActivity
import kotlinx.android.synthetic.main.activity_favorite_product.*
import kotlinx.android.synthetic.main.view_default_empty_state.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class FavoriteProductActivity : NikeActivity(),FavoriteProductAdapter.FavoriteProductEventListener {

    val viewModel:FavoriteProductViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_product)


        viewModel.productLIveData.observe(this) {
            if (it.isNotEmpty()) {
                favoriteRv.layoutManager = LinearLayoutManager(this)
                favoriteRv.adapter = FavoriteProductAdapter(it as MutableList<Product>, this, get())
            }else{
                showEmptyState(R.layout.view_default_empty_state)
                emptyStateMessageTv.text = "هنوز محصولی به لیست علاقمندی اضافه نکردی"
            }
        }

        helpBtn.setOnClickListener {
            Snackbar.make(it,getString(R.string.favorites_help_message),Snackbar.LENGTH_LONG).show()
        }


    }

    override fun onclick(product: Product) {
       startActivity(Intent(this,ProductDetailActivity::class.java).apply {
           putExtra(EXTRA_KEY_DATA,product)
       })
    }

    override fun onLongClick(product: Product) {
        viewModel.removeFromFavorite(product)
    }
}