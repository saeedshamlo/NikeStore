package com.sevenlearn.nike.feature.main

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.MaterialColors
import com.sevenlearn.nike.R
import com.sevenlearn.nike.common.NikeActivity
import com.sevenlearn.nike.common.convertDpToPixel
import com.sevenlearn.nikestore.data.CartItemCount
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : NikeActivity() {
    private var currentNavController: LiveData<NavController>? = null
    private lateinit var navController: NavController
    val viewModel:MainViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        navController = navHostFragment.navController

        // Setup the bottom navigation view with navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationMain)
        bottomNavigationView.setupWithNavController(navController)




    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    fun onCartItemsCountChangeEvent(cartItemCount: CartItemCount){
        val badge = bottomNavigationMain.getOrCreateBadge(R.id.cart)
        badge.badgeGravity = BadgeDrawable.BOTTOM_START
        badge.backgroundColor = MaterialColors.getColor(bottomNavigationMain,R.attr.colorPrimary)
        badge.number =cartItemCount.count
        badge.verticalOffset = convertDpToPixel(10f,this).toInt()
        badge.isVisible = cartItemCount.count > 0

    }
    override fun onResume() {
        super.onResume()
        viewModel.getCartItemCount()
    }


}


