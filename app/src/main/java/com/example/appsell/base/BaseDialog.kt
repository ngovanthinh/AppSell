package com.example.appsell.base

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.appsell.R

open class BaseDialog : DialogFragment() {

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
}