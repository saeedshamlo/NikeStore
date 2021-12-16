package com.sevenlearn.nike.feature.list

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.i
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.facebook.common.logging.FLog.i
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sevenlearn.nike.R
import com.sevenlearn.nike.common.EXTRA_KEY_DATA
import com.sevenlearn.nike.common.NikeActivity
import com.sevenlearn.nike.data.Product
import com.sevenlearn.nike.feature.common.ProductListAdapter
import com.sevenlearn.nike.feature.common.VIEW_TYPE_GRID
import com.sevenlearn.nike.feature.common.VIEW_TYPE_LIST
import com.sevenlearn.nike.feature.product.ProductDetailActivity

import kotlinx.android.synthetic.main.activity_product_list.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber
import timber.log.Timber.Forest.i
import java.util.*
import kotlin.collections.ArrayList

class ProductListActivity : NikeActivity(),ProductListAdapter.OnProductClickListener {
    val viewModel: ProductListViewModel by viewModel {
        parametersOf(
            intent.extras!!.getInt(
                EXTRA_KEY_DATA
            )
        )
    }
    val adapter: ProductListAdapter by inject { parametersOf(VIEW_TYPE_GRID,false) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        toolbarView.onBackButtonClickListener =View.OnClickListener {
            finish()
        }

        adapter.onProductClickListener = this
        val gridLayoutManager = GridLayoutManager(this, 2)
        productListRv.layoutManager = gridLayoutManager
        productListRv.adapter = adapter

        view_type_changer.setOnClickListener {
            if (adapter.viewType == VIEW_TYPE_GRID) {
                view_type_changer.setImageResource(R.drawable.list)
                adapter.viewType = VIEW_TYPE_LIST
                gridLayoutManager.spanCount = 1
                adapter.notifyDataSetChanged()
            } else {
                view_type_changer.setImageResource(R.drawable.grid)
                adapter.viewType = VIEW_TYPE_GRID
                gridLayoutManager.spanCount = 2
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.sortTitleLiveData.observe(this){
            sortTitleView.text = getString(it)
        }

        viewModel.progressBarLiveData.observe(this){
            setProgressIndicator(it)
        }
        viewModel.productLiveData.observe(this) {
            Timber.i(it.toString())
            adapter.products = it as ArrayList<Product>
        }
        viewModel.errorLiveData.observe(this){
            Toast.makeText(this,it, Toast.LENGTH_LONG).show()
        }
        viewOnClickSort.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(this)
                .setSingleChoiceItems(
                    R.array.sort, viewModel.sort
                ) {
                        dialog, sortSelectedIndex ->
                    viewModel.onSelectedSortByUser(sortSelectedIndex)
                    dialog.dismiss()

                }.setTitle(R.string.sort)

            dialog.show()

        }
    }

    override fun onProductClick(product: Product) {
        startActivity(Intent(this,ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA,product)
        })
    }

    override fun onFavoriteBtn(product: Product) {
       viewModel.addProductToFavorite(product)
    }
}