package com.sevenlearn.nike.data

data class SubmitOrderResult(
    val bank_gateway_url: String,
    val order_id: Int
)