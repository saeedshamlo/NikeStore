package com.sevenlearn.nike.feature.shipping

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sevenlearn.nike.R
import com.sevenlearn.nike.common.*
import com.sevenlearn.nike.data.SubmitOrderResult
import com.sevenlearn.nike.feature.cart.CartItemAdapter
import com.sevenlearn.nike.feature.checkout.CheckOutActivity
import com.sevenlearn.nikestore.data.PurchaseDetail
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_shipping.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.IllegalStateException

class ShippingActivity : AppCompatActivity() {
    val viewModel: ShippingViewModel by viewModel()
    val compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shipping)

        val purchaseDetail = intent.getParcelableExtra<PurchaseDetail>(EXTRA_KEY_DATA)
            ?: throw IllegalStateException("Null")
        val viewHolder = CartItemAdapter.PurchaseDetailViewHolder(purchaseDetailView)
        viewHolder.bindPurchase(
            purchaseDetail.totalPrice,
            purchaseDetail.shipping_cost,
            purchaseDetail.payable_price
        )


        val onClick = View.OnClickListener {
            viewModel.submitOrder(
                firstNameEt.text.toString(),
                lastNameEt.text.toString(),
                postalEt.text.toString(),
                phoneNumberEt.text.toString(),
                adressEt.text.toString(),
                if (it.id == R.id.onlinePaymentBtn) PAYMENT_METHOD_ONLINE else PAYMENT_METHOD_COD
            )
                .asyncNetworkRequest()
                .subscribe(object : NikeSingleObserver<SubmitOrderResult>(compositeDisposable) {
                    override fun onSuccess(t: SubmitOrderResult) {
                        if (t.bank_gateway_url.isNotEmpty()) {
                            openUrlInCustomTab(this@ShippingActivity, t.bank_gateway_url)
                        } else {
                            startActivity(
                                Intent(
                                    this@ShippingActivity,
                                    CheckOutActivity::class.java
                                ).apply {
                                    putExtra(EXTRA_KEY_ID, t.order_id)
                                })
                        }

                        finish()
                    }

                })
        }
        onlinePaymentBtn.setOnClickListener(onClick)
        codBtn.setOnClickListener(onClick)
    }


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}