package com.sevenlearn.nike.feature.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sevenlearn.nike.R
import com.sevenlearn.nike.common.EXTRA_KEY_DATA
import com.sevenlearn.nike.common.NikeFragment
import com.sevenlearn.nike.common.convertDpToPixel
import com.sevenlearn.nike.data.Product
import com.sevenlearn.nike.data.SORT_LATEST
import com.sevenlearn.nike.data.SORT_POPULAR
import com.sevenlearn.nike.feature.common.ProductListAdapter
import com.sevenlearn.nike.feature.common.VIEW_TYPE_GRID
import com.sevenlearn.nike.feature.common.VIEW_TYPE_ROUND
import com.sevenlearn.nike.feature.list.ProductListActivity
import com.sevenlearn.nike.feature.main.BannerSliderAdapter
import com.sevenlearn.nike.feature.product.ProductDetailActivity
import com.sevenlearn.nike.feature.search.SearchActivity
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : NikeFragment(), ProductListAdapter.OnProductClickListener {
    val homeViewModel: HomeViewModel by viewModel()
    val productLastedListAdapter: ProductListAdapter by inject{ parametersOf(VIEW_TYPE_ROUND,false)}
    val productPopularListAdapter: ProductListAdapter by inject{ parametersOf(VIEW_TYPE_ROUND,false)}
    val timer = Timer()
    override fun onResume() {
        super.onResume()
        calculateBannerSliderHeight()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope
        latestProductsRv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        latestProductsRv.adapter = productLastedListAdapter
        productLastedListAdapter.onProductClickListener = this
        productPopularListAdapter.onProductClickListener = this


        homeViewModel.errorLiveData.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()
        }
        homeViewModel.productsLastedLiveData.observe(viewLifecycleOwner) {
            Timber.i(it.toString())
            productLastedListAdapter.products = it as ArrayList<Product>
        }

        popularProductsRv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        popularProductsRv.adapter = productPopularListAdapter
        homeViewModel.productsPopularLiveData.observe(this) {
            productPopularListAdapter.products = it as ArrayList<Product>
        }


        homeViewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }

        seeMoreLasted.setOnClickListener {
            startActivity(Intent(requireContext(), ProductListActivity::class.java).apply {
                putExtra(EXTRA_KEY_DATA, SORT_LATEST)
            })
        }
        seeMorePopular.setOnClickListener {
            startActivity(Intent(requireContext(), ProductListActivity::class.java).apply {
                putExtra(EXTRA_KEY_DATA, SORT_POPULAR)
            })
        }

        homeViewModel.bannersLiveData.observe(viewLifecycleOwner) {
            Timber.i(it.toString())
            val bannerSliderAdapter = BannerSliderAdapter(this, it)
            bannerSliderViewPager.adapter = bannerSliderAdapter
            calculateBannerSliderHeight()
            sliderIndicator.setViewPager2(bannerSliderViewPager)

            /* timer.schedule(object : TimerTask() {
                override fun run() {
                    if (bannerSliderViewPager?.currentItem!! < bannerSliderAdapter.itemCount - 1)
                        bannerSliderViewPager.setCurrentItem(
                            bannerSliderViewPager.currentItem + 1,
                            true
                        )
                    else
                        bannerSliderViewPager.setCurrentItem(0, true)
                }
            }, 5000, 5000)*/

        }
        searchEtT.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        startActivity(Intent(requireContext(),SearchActivity::class.java))
                }

                return v?.onTouchEvent(event) ?: true
            }
        })


        /*searchEtT.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })*/
    }
    override fun onProductClick(product: Product) {
        startActivity(Intent(requireContext(), ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, product)
        })
    }

    override fun onFavoriteBtn(product: Product) {
       homeViewModel.addProductToFavorite(product)
    }

    private fun calculateBannerSliderHeight() {

        val viewPagerHeight = (((bannerSliderViewPager.measuredWidth - convertDpToPixel(
            32f,
            requireContext()
        )) * 173) / 328).toInt()

        val layoutParams = bannerSliderViewPager.layoutParams
        layoutParams.height = viewPagerHeight
        bannerSliderViewPager.layoutParams = layoutParams
    }

    override fun onDestroy() {
        super.onDestroy()
       /* timer.cancel()
        timer.purge()
       // timer == null*/
    }
}


