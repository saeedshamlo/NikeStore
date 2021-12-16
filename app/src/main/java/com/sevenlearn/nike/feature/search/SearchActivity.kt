package com.sevenlearn.nike.feature.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.sevenlearn.nike.R
import com.sevenlearn.nike.common.EXTRA_KEY_DATA
import com.sevenlearn.nike.common.NikeActivity
import com.sevenlearn.nike.common.NikeSingleObserver
import com.sevenlearn.nike.common.asyncNetworkRequest
import com.sevenlearn.nike.data.Product
import com.sevenlearn.nike.feature.common.ProductListAdapter
import com.sevenlearn.nike.feature.common.VIEW_TYPE_LIST
import com.sevenlearn.nike.feature.product.ProductDetailActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.view_default_empty_state.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class SearchActivity : NikeActivity(), ProductListAdapter.OnProductClickListener {
    val viewModel: SearchViewModel by viewModel()
    val compositeDisposable = CompositeDisposable()
    val adapter: ProductListAdapter by inject { parametersOf(VIEW_TYPE_LIST, true) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchEt.isFocusableInTouchMode = true
        searchEt.isFocusable = true

        searchRv.layoutManager = LinearLayoutManager(this)
        searchRv.adapter = adapter
        searchEt.setOnEditorActionListener { textView, i, keyEvent ->
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, textView.text.toString())
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                FirebaseAnalytics.getInstance(this)
                    .logEvent(FirebaseAnalytics.Event.SEARCH, bundle)
                viewModel.progressBarLiveData.observe(this) {
                    setProgressIndicator(it)
                }
                viewModel.search(textView.text.toString())
                    .asyncNetworkRequest()
                    .subscribe(object : NikeSingleObserver<List<Product>>(compositeDisposable) {
                        override fun onSuccess(t: List<Product>) {
                            val show = showEmptyState(R.layout.view_default_empty_state)
                            if (t.isNotEmpty()) {
                                show?.visibility = View.GONE
                                adapter.products = t as ArrayList<Product>
                            } else {
                                show?.visibility = View.VISIBLE
                                emptyStateMessageTv.text = "چیزی پیدا نشد"
                            }
                        }
                    })
            }
            return@setOnEditorActionListener false
        }

        adapter.onProductClickListener = this

    }

    override fun onProductClick(product: Product) {
        startActivity(Intent(this, ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, product)
        })
    }

    override fun onFavoriteBtn(product: Product) {
        viewModel.addProductToFavorite(product)
    }
}