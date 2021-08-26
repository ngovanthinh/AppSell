package com.example.appsell.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appsell.R
import com.example.appsell.base.Until
import com.example.appsell.model.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.btn_back
import kotlinx.android.synthetic.main.fragment_register.edt_email
import kotlinx.android.synthetic.main.fragment_register.edt_password
import kotlinx.android.synthetic.main.fragment_register.txt_register

class RegisterFragment : Fragment() {

    private var isShowPass: Boolean = false
    private var isShowPassAgain: Boolean = false
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

        img_eye_register.setOnClickListener {
            isShowPass = !isShowPass
            Until.showHidePassword(isShowPass, edt_password, img_eye_register)
        }

        img_eye_again.setOnClickListener {
            isShowPassAgain = !isShowPassAgain
            Until.showHidePassword(isShowPassAgain, edt_password_again, img_eye_again)
        }

    }

    private fun register() {
        val pass = edt_password.text.toString().trim()
        val passAgain = edt_password_again.text.toString().trim()
        val email = edt_email.text.toString().trim()
        val userName = edt_user_name.text.toString().trim()

        if (((pass != passAgain) || pass.length < 6 || pass.isEmpty() || passAgain.isEmpty() || userName.isEmpty())) {
            Until.message("Vui lòng nhập đủ thông tin", requireActivity())
        } else {
            auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        updateProfile(email, userName)
                    } else {
                        Until.message(it.exception?.message ?: "Lỗi hệ thống vui lòng thử lại", requireActivity())
                    }
                }
        }
    }

    private fun updateProfile(email: String, userName: String) {
        val profile = Profile(userName, "", "")
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val reference: DatabaseReference = database.reference

        reference.child("username").child(email.replace(".", "")).setValue(profile)
            .addOnSuccessListener {
                val bundle = Bundle().apply {
                    putString(LoginFragment.EMAIL, email.replace(".", ""))
                }
                findNavController().navigate(R.id.action_registerFragment_to_homeFragment, bundle)
                Until.message("Đăng ký thành công", requireActivity())
            }
            .addOnFailureListener {
                Until.message(it.message ?: "Lỗi hệ thống vui lòng thử lại", requireActivity())
            }
    }

}