package com.example.appsell.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.util.Util
import com.example.appsell.R
import com.example.appsell.base.Until
import com.example.appsell.model.Profile
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    var isManager : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callBack: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragment?.findNavController()?.popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callBack)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getProfile()

        btn_back.setOnClickListener {
            findNavController().popBackStack()
        }

        txt_create.setOnClickListener {
            updateProfile()
        }
    }

    private fun updateProfile() {
        val fullName = edt_full_name.text.toString().trim()
        val date = edt_date.text.toString().trim()
        val address = edt_address.text.toString().trim()
        val email: String = arguments?.getString(LoginFragment.EMAIL)!!
        val profile = Profile(fullName, date, address, isManager)

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val reference: DatabaseReference = database.reference

        reference.child("username").child(email).setValue(profile)
            .addOnSuccessListener {
                findNavController().popBackStack()
                Until.message("Cập nhật thông tin thành công", requireActivity())
            }
            .addOnFailureListener {
                Until.message(it.message ?: "Lỗi hệ thống vui lòng thử lại", requireActivity())
            }
    }

    private fun getProfile() {
        val email: String = arguments?.getString(LoginFragment.EMAIL)!!
        val allPost = Firebase.database.reference.child("username").child(email.replace(".",""))

        Until.showLoading(requireActivity())
        allPost.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val profile: Profile? = snapshot.getValue(Profile::class.java)
                edt_full_name.setText(profile?.userName)
                edt_date.setText(profile?.date)
                edt_address.setText(profile?.address)
                isManager = profile?.isManager!!
                Until.hideLoading()
            }

            override fun onCancelled(error: DatabaseError) {
                Until.hideLoading()
            }
        })
    }

//    private fun message(message: String) {
//        Alerter.create(requireActivity())
//            .setText(message)
//            .setBackgroundColorRes(R.color.colorPrimary)
//            .show()
//    }

}