package com.example.appsell.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appsell.R
import com.example.appsell.model.Profile
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val profile = Profile(fullName, fullName, date, address)

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val reference: DatabaseReference = database.reference
        reference.child("username").child("email").setValue(profile)
            .addOnSuccessListener {
                findNavController().popBackStack()
                message("Cập nhật thông tin thành công")
            }
            .addOnFailureListener {
                message(it.message ?: "Lỗi hệ thống vui lòng thử lại")
            }
    }

    private fun getProfile() {
        val allPost = Firebase.database.reference.child("username").child("email")

        allPost.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val profile: Profile? = snapshot.getValue(Profile::class.java)
                edt_full_name.setText(profile?.userName)
                edt_date.setText(profile?.date)
                edt_address.setText(profile?.address)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun message(message: String) {
        Alerter.create(requireActivity())
            .setText(message)
            .setBackgroundColorRes(R.color.colorPrimary)
            .show()
    }

}