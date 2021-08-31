package com.example.appsell.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appsell.R
import com.example.appsell.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_remove_prorduct.*

class DeleteProductDialog : BaseDialog() {
    private lateinit var onClickDelete :()->Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_remove_prorduct, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnClose.setOnClickListener {
            dismiss()
        }

        txtSubmit.setOnClickListener {
            onClickDelete.invoke()
            dismiss()
        }
    }

    fun onClickDeleteListener(onClickDelete :()->Unit){
        this.onClickDelete = onClickDelete
    }
}