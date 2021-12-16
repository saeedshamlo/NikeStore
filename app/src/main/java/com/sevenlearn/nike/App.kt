package com.sevenlearn.nike

import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import androidx.room.Room
import com.facebook.drawee.backends.pipeline.Fresco
import com.sevenlearn.nike.data.db.AppDataBase
import com.sevenlearn.nike.data.repo.*
import com.sevenlearn.nike.data.repo.order.OrderRemoteDataSource
import com.sevenlearn.nike.data.repo.order.OrderRepository
import com.sevenlearn.nike.data.repo.order.OrderRepositoryImpl
import com.sevenlearn.nike.data.repo.source.*
import com.sevenlearn.nike.feature.auth.AuthViewModel
import com.sevenlearn.nike.feature.cart.CartViewModel
import com.sevenlearn.nike.feature.checkout.CheckOutViewModel
import com.sevenlearn.nike.feature.common.ProductListAdapter
import com.sevenlearn.nike.feature.favorite.FavoriteProductViewModel
import com.sevenlearn.nike.feature.list.ProductListViewModel
import com.sevenlearn.nike.feature.product.ProductDetailViewModel
import com.sevenlearn.nike.feature.home.HomeViewModel
import com.sevenlearn.nike.feature.main.MainViewModel
import com.sevenlearn.nike.feature.order.OrderViewModel
import com.sevenlearn.nike.feature.product.comment.CommentListViewModel
import com.sevenlearn.nike.feature.profile.ProfileViewModel
import com.sevenlearn.nike.feature.search.SearchViewModel
import com.sevenlearn.nike.feature.shipping.ShippingViewModel
import com.sevenlearn.nike.services.FrescoImageLoadingService
import com.sevenlearn.nike.services.ImageLoadingService
import com.sevenlearn.nike.services.http.createApiServiceInstance
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber
import kotlin.math.sin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        Fresco.initialize(this)

        val myModules = module {
            single { createApiServiceInstance() }
            single<ImageLoadingService> { FrescoImageLoadingService() }
            single { Room.databaseBuilder(this@App,AppDataBase::class.java,"db_app").build()}
            factory<ProductRepository> {
                ProductRepositoryImpl(
                    ProductRemoteDataSource(get()),
                   get<AppDataBase>().productDao()
                )
            }
            single { UserLocalDataSource(get()) }
            single<SharedPreferences> {
                this@App.getSharedPreferences(
                    "app_settings",
                    MODE_PRIVATE
                )
            }
            factory { (view_type: Int,isViews:Boolean) -> ProductListAdapter(view_type,isViews, get()) }
            factory<BannerRepository> { BannerRepositoryImpl(BannerRemoteDataSource(get())) }
            factory<CommentRepository> { CommentRepositoryImpl(CommentRemoteDataSource(get())) }
            factory<CartRepository> { CartRepositoryImpl(CartRemoteDataSource(get())) }
            single<OrderRepository> { OrderRepositoryImpl(OrderRemoteDataSource(get())) }
            single<UserRepository> {
                UserRpositoryImpl(
                    UserLocalDataSource(get()),
                    UserRemoteDataSource(get())
                )
            }


            viewModel { HomeViewModel(get(), get()) }
            viewModel { (bundle: Bundle) -> ProductDetailViewModel(bundle, get(), get(),get()) }
            viewModel { (productId: Int) -> CommentListViewModel(productId, get()) }
            viewModel { (sort: Int) -> ProductListViewModel(sort, get()) }
            viewModel { AuthViewModel(get()) }
            viewModel { CartViewModel(get()) }
            viewModel { MainViewModel(get()) }
            viewModel { ShippingViewModel(get()) }
            viewModel { (orderId: Int) -> CheckOutViewModel(orderId, get()) }
            viewModel { ProfileViewModel(get()) }
            viewModel { FavoriteProductViewModel(get()) }
            viewModel { OrderViewModel(get()) }
            viewModel { SearchViewModel(get()) }
        }

        startKoin {
            androidContext(this@App)
            modules(myModules)
        }

        val userRepository: UserRepository = get()
        userRepository.loadToken()
    }
}