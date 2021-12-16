package com.sevenlearn.nike.services.http

import com.google.gson.JsonObject
import com.sevenlearn.nike.data.*
import com.sevenlearn.nikestore.data.*
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("product/list")
    suspend fun getProducts(@Query("sort") sort: String): List<Product>

    @GET("banner/slider")
    suspend fun getBanners(): List<Banner>

    @GET("comment/list")
    fun getComments(@Query("product_id") productId: Int): Single<List<Comment>>

    @POST("comment/add")
    fun addComment(@Body jsonObject: JsonObject):Single<Comment>

    @POST("cart/add")
    fun addToCart(@Body jsonObject: JsonObject): Single<AddToCartResponse>

    @POST("cart/remove")
    fun removeItemFromCart(@Body jsonObject: JsonObject): Single<MessageResponse>

    @GET("cart/list")
    fun getCart(): Single<CartResponse>

    @POST("cart/changeCount")
    fun changeCount(@Body jsonObject: JsonObject): Single<AddToCartResponse>

    @GET("cart/count")
    fun getCartItemsCount(): Single<CartItemCount>

    @POST("auth/token")
    fun login(@Body jsonObject: JsonObject): Single<TokenResponse>

    @POST("user/register")
    fun signUp(@Body jsonObject: JsonObject): Single<MessageResponse>

    @POST("auth/token")
    fun refreshToken(@Body jsonObject: JsonObject): Call<TokenResponse>

    @POST("order/submit")
    fun submitOrder(@Body jsonObject: JsonObject): Single<SubmitOrderResult>

    @GET("order/checkout")
    fun checkout(@Query("order_id") orderId: Int): Single<Checkout>

    @GET("order/list")
    fun getOrderList():Single<List<OrderHistoryItem>>



    @GET("product/search")
    fun searchProduct(@Query("q")query:String):Single<List<Product>>


}

fun createApiServiceInstance(): ApiService {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor {
            val oldrequset = it.request()
            val newRequset = oldrequset.newBuilder()
            if (TokenContainer.token != null) {
                newRequset.addHeader("Authorization", "Bearer ${TokenContainer.token}")
            }

            newRequset.addHeader("Accept", "application/json")
            newRequset.method(oldrequset.method, oldrequset.body)
            return@addInterceptor it.proceed(newRequset.build())
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://expertdevelopers.ir/api/v1/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
    return retrofit.create(ApiService::class.java)
}