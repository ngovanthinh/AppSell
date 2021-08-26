package com.example.appsell.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appsell.R
import com.google.firebase.auth.FirebaseAuth
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txt_register.setOnClickListener {
            register()
        }

        btn_back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun register() {
        val pass = edt_password.text.toString().trim()
        val passAgain = edt_password_again.text.toString().trim()
        val email = edt_email.text.toString().trim()

        if (((pass != passAgain) || pass.length >= 6 || pass.isEmpty() || passAgain.isEmpty() || email.isEmpty())) {
            message("Vui lòng nhập đủ thông tin")
        } else {
            auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                    } else {

                    }
                }
        }
    }

    private fun message(message: String) {
        Alerter.create(requireActivity())
            .setText(message)
            .setBackgroundColorRes(R.color.colorPrimary)
            .show()
    }

}