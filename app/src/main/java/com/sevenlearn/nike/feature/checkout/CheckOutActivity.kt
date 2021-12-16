package com.sevenlearn.nike.feature.checkout

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sevenlearn.nike.R
import com.sevenlearn.nike.common.EXTRA_KEY_ID
import com.sevenlearn.nike.common.formatPrice
import com.sevenlearn.nike.feature.order.OrderActivity
import kotlinx.android.synthetic.main.activity_check_out.*
import kotlinx.android.synthetic.main.item_purchase_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CheckOutActivity : AppCompatActivity() {
    val viewModel:CheckOutViewModel by viewModel {
        val uri:Uri? = intent.data
        if(uri!=null){
            parametersOf(uri.getQueryParameter("order_id")!!.toInt())
        }
        else
        {
            parametersOf(intent.extras?.getInt(EXTRA_KEY_ID))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)

        viewModel.checkOutLiveData.observe(this){
            orderPriceTv.text = formatPrice(it.payable_price)
            oderStatusTv.text = it.payment_status
            puchaseStatusTv.text = if(it.purchase_success) "خرید با موفقیت انجام شد." else "خرید ناموفق"
        }

        returnHomeBtn.setOnClickListener {
            finish()
        }
        orderHistoryBtn.setOnClickListener {

            startActivity(Intent(this,OrderActivity::class.java))
            finish()
        }

    }
}