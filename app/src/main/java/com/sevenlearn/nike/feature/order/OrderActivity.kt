package com.sevenlearn.nike.feature.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sevenlearn.nike.R
import com.sevenlearn.nike.common.NikeActivity
import kotlinx.android.synthetic.main.activity_order.*
import org.koin.android.viewmodel.ext.android.viewModel

class OrderActivity : NikeActivity() {
    val viewModel:OrderViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        orderHistoryRv.layoutManager =LinearLayoutManager(this)
        viewModel.orderLIveData.observe(this){
            orderHistoryRv.adapter = OrderHistoryAdapter(this,it.reversed())
        }
        viewModel.progressBarLiveData.observe(this){
            setProgressIndicator(it)
        }
    }
}