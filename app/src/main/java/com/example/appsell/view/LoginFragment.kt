package com.example.appsell.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.appsell.R
import com.google.firebase.auth.FirebaseAuth
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
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

    }

    private fun login() {
        val email = edt_email.text.toString()
        val pass = edt_password.text.toString()

        if (email.isNotBlank() && pass.isNotBlank()) {
            auth.signInWithEmailAndPassword(edt_email.text.toString(), edt_password.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        findNavController().navigate(R.id.action_LoginFragment_to_homeFragment)
                    } else {
                        message(it.exception?.message ?: "Login fail")
                    }
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

}