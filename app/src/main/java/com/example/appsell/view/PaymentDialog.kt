package com.example.appsell.view

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.example.appsell.R
import com.example.appsell.base.Constant
import kotlinx.android.synthetic.main.dialog_payment.*

class PaymentDialog : DialogFragment() {

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

    override fun onStart() {
        super.onStart()

        dialog?.window?.decorView?.setBackgroundResource(android.R.color.transparent)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.bg_rounded)

        val width = resources.getDimensionPixelSize(R.dimen._260sdp)
        val height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
        dialog?.window?.setGravity(Gravity.CENTER)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCanceledOnTouchOutside(false)
            window?.attributes?.windowAnimations = R.style.DialogAnimationNormal
        }

        return dialog
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