package com.example.appsell.base

import android.app.Activity
import android.text.method.PasswordTransformationMethod
import android.text.method.SingleLineTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import com.example.appsell.R
import com.tapadoo.alerter.Alerter

object Until {

    fun showHidePassword(isShowPassword: Boolean, edtInput: EditText, imgShowHide: ImageView) {
        if (isShowPassword) {
            imgShowHide.setImageResource(R.drawable.ic_eye_off)
            edtInput.transformationMethod = SingleLineTransformationMethod()
        } else {
            imgShowHide.setImageResource(R.drawable.ic_eye)
            edtInput.transformationMethod = PasswordTransformationMethod()
        }
        edtInput.setSelection(edtInput.text.length)
    }

    fun message(message: String, activity: Activity) {
        Alerter.create(activity)
            .setText(message)
            .setBackgroundColorRes(R.color.colorPrimary)
            .show()
    }
}