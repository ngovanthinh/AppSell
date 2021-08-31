package com.example.appsell.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.text.method.SingleLineTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.util.Util
import com.example.appsell.R
import com.example.appsell.base.Constant
import com.example.appsell.base.Until
import com.google.firebase.auth.FirebaseAuth
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.fragment_login.*
import java.lang.Exception

class LoginFragment : Fragment() {


    private var isShowPass = false

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        val callBack: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callBack)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    @SuppressLint("ShowToast")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txt_login.setOnClickListener {
            login()
        }

        txt_register.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_registerFragment)
        }

        img_eye.setOnClickListener {
            isShowPass = !isShowPass
            Until.showHidePassword(isShowPass, edt_password, img_eye)
        }

        txt_reset_password.setOnClickListener {
            val email = edt_email.text.toString().trim()
            if (email.isEmpty()) {
                Until.message("Vui lòng nhập email bạn muốn khôi phục mật khẩu", requireActivity())
            } else {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Until.message("Chúng tôi đã trả mật khẩu trong email đăng ký!", requireActivity())
                        } else {
                            Until.message("Failed to send reset email!", requireActivity())
                        }
                    }
            }
        }

    }

    private fun login() {
        val email = edt_email.text.toString().trim()
        val pass = edt_password.text.toString().trim()

        if (email.isNotBlank() && pass.isNotBlank()) {
            Until.showLoading(requireActivity())
            auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@addOnCompleteListener
                        with(sharedPref.edit()) {
                            putString(Constant.USER_PROFILE, email)
                            apply()
                        }

                        val bundle = Bundle().apply {
                            putString(EMAIL, email.replace(".", ""))
                        }
                        try {
                            findNavController().navigate(R.id.action_LoginFragment_to_homeFragment, bundle)
                        } catch (e: Exception) {

                        }
                    } else {
                        message(it.exception?.message ?: "Login fail")
                    }

                    Until.hideLoading()
                }
        } else {
            message("Vui lòng nhập mật khẩu và email")
        }

    }

    override fun onResume() {
        super.onResume()
        edt_email.setText("")
        edt_password.setText("")
    }

    private fun message(message: String) {
        Alerter.create(requireActivity())
            .setText(message)
            .setBackgroundColorRes(R.color.colorPrimary)
            .show()
    }

    companion object {
        const val EMAIL: String = "email_user"
    }
}