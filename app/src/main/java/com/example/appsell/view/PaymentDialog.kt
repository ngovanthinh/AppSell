package com.example.appsell.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appsell.R
import com.example.appsell.base.BaseDialog
import com.example.appsell.base.Constant
import kotlinx.android.synthetic.main.dialog_payment.*

class PaymentDialog : BaseDialog() {

    private lateinit var onPayment: (type_payment: String) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnClose.setOnClickListener {
            dismiss()
        }

        btnCash.setOnClickListener {
            selectCash()
        }

        btnDelivery.setOnClickListener {
            selectDelivery()
        }

        btnTransfer.setOnClickListener {
            selectTransfer()
        }

        txtPaymentSuccess.setOnClickListener {
            dismiss()
            var typePayment: String = Constant.TYPE_CART
            if (btnCash.isChecked) {
                typePayment = Constant.TYPE_CART
            }
            if (btnDelivery.isChecked) {
                typePayment = Constant.TYPE_RECEIPT
            }

            if (btnTransfer.isChecked) {
                typePayment = Constant.TYPE_TRANSFER
            }
            onPayment.invoke(typePayment)
        }
    }

    private fun selectCash() {
        btnCash.isChecked = true
        btnDelivery.isChecked = false
        btnTransfer.isChecked = false
    }

    private fun selectDelivery() {
        btnCash.isChecked = false
        btnDelivery.isChecked = true
        btnTransfer.isChecked = false
    }

    private fun selectTransfer() {
        btnCash.isChecked = false
        btnDelivery.isChecked = false
        btnTransfer.isChecked = true
    }

    fun setOnItemClickListener(onItemClicked: (type_payment: String) -> Unit) {
        this.onPayment = onItemClicked
    }

}