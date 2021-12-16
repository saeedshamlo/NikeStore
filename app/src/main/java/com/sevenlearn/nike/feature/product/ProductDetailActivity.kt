package com.sevenlearn.nike.feature.product

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sevenlearn.nike.R
import com.sevenlearn.nike.common.*
import com.sevenlearn.nike.data.Comment
import com.sevenlearn.nike.data.Product
import com.sevenlearn.nike.feature.product.comment.CommentListActivity
import com.sevenlearn.nike.services.ImageLoadingService
import com.sevenlearn.nike.view.scroll.ObservableScrollViewCallbacks
import com.sevenlearn.nike.view.scroll.ScrollState
import com.sevenlearn.nikestore.data.AddToCartResponse
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_product_detail.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class ProductDetailActivity : NikeActivity() {
    val productDetailViewModel: ProductDetailViewModel by viewModel { parametersOf(intent.extras) }
    val imageLoadingService: ImageLoadingService by inject()
    val commentAdapter = CommentAdapter()
    val compositeDisposable =CompositeDisposable()
    lateinit var product:Product
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        productDetailViewModel.productLiveData.observe(this) {
            product = it
            imageLoadingService.load(productIv, it.image)
            titleTv.text = it.title
            previousPriceTv.text = formatPrice(it.previous_price)
            previousPriceTv.paintFlags=Paint.STRIKE_THRU_TEXT_FLAG
            currentPriceTv.text = formatPrice(it.price)
            toolbarTitleTv.text = it.title

            if(it.isFavorite){
                favoriteBtn.setImageResource(R.drawable.ic_favorite_fill)
            }
            else
            {
                favoriteBtn.setImageResource(R.drawable.ic_favorites)
            }
            favoriteBtn.setOnClickListener {view->
                productDetailViewModel.addProductToFavorite(it)
                it.isFavorite = !it.isFavorite
                if(it.isFavorite){
                    favoriteBtn.setImageResource(R.drawable.ic_favorite_fill)
                }
                else
                {
                    favoriteBtn.setImageResource(R.drawable.ic_favorites)
                }
            }

            countCommentBtn.setOnClickListener {view ->
                startActivity(Intent(this, CommentListActivity::class.java).apply {
                    putExtra(EXTRA_KEY_ID, productDetailViewModel.productLiveData.value!!.id)
                    putExtra(EXTRA_KEY_DATA,it)
                })
            }

        }

        productDetailViewModel.progressBarLiveData.observe(this){
            setProgressIndicator(it)
        }

        productDetailViewModel.commentsLiveData.observe(this) {
            Timber.i(it.toString())
            countCommentBtn.text = "${it.size} نظر"
            commentAdapter.comments = it as ArrayList<Comment>
            if (it.size > 3) {
                viewAllCommentsBtn.visibility = View.VISIBLE
                viewAllCommentsBtn.setOnClickListener {
                    startActivity(Intent(this, CommentListActivity::class.java).apply {
                        putExtra(EXTRA_KEY_ID, productDetailViewModel.productLiveData.value!!.id)
                        putExtra(EXTRA_KEY_DATA,product)
                    })
                }
            }
        }

        initViews()


    }

    fun initViews() {
        commentsRv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        commentsRv.adapter = commentAdapter
        commentsRv.isNestedScrollingEnabled = false

        productIv.post {
            val productIvHeight = productIv.height
            val toolbar = toolbarView
            val productImageView = productIv
            observableScrollView.addScrollViewCallbacks(object : ObservableScrollViewCallbacks {
                override fun onScrollChanged(
                    scrollY: Int,
                    firstScroll: Boolean,
                    dragging: Boolean
                ) {
                    Timber.i("productIv height is -> $productIvHeight")
                    toolbar.alpha = scrollY.toFloat() / productIvHeight.toFloat()
                    productImageView.translationY = scrollY.toFloat() / 2
                }

                override fun onDownMotionEvent() {
                }

                override fun onUpOrCancelMotionEvent(scrollState: ScrollState?) {
                }

            })
        }
        addToCartBtn.setOnClickListener{
            productDetailViewModel.onAddToCartBtn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :NikeCompletableObserver(compositeDisposable){
                    override fun onComplete() {
                        showSnackBar(getString(R.string.success_AddToCart))
                    }
                })
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}