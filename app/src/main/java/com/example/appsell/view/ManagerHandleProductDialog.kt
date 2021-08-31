package com.example.appsell.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appsell.R
import com.example.appsell.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_manager_handle_product.*

class ManagerHandleProductDialog : BaseDialog() {
    private lateinit var onClickAccept: (isPayment: Boolean) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_manager_handle_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_accept_payment.setOnClickListener {
            selectPayment()
        }

        btn_cancel_payment.setOnClickListener {
            selectCancel()
        }

        txtHandle.setOnClickListener {
            onClickAccept.invoke(btn_accept_payment.isChecked)
            dismiss()
        }

        btnClose.setOnClickListener {
            dismiss()
        }
    }

    private fun selectPayment() {
        btn_accept_payment.isChecked = true
        btn_cancel_payment.isChecked = false
    }

    private fun selectCancel() {
        btn_accept_payment.isChecked = false
        btn_cancel_payment.isChecked = true
    }

    fun handlePayment(onItemClicked: (isPayment: Boolean) -> Unit) {
        this.onClickAccept = onItemClicked
    }
}