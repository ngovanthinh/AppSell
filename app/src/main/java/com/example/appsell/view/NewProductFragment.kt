package com.example.appsell.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appsell.R
import com.example.appsell.model.Product
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.fragment_new_product.*
import java.util.*

/**
 * Created by ThinhNV on 23/08/2021.
 */
class NewProductFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callBack: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragment?.findNavController()?.popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callBack)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_back.setOnClickListener {
            findNavController().popBackStack()
        }

        txt_create.setOnClickListener {
            createProduct()
        }
    }

    private fun createProduct() {
        val nameProduct = edt_name_product.text.toString().trim()
        val cost = edt_cost_product.text.toString().trim()
        val description = edt_description_product.text.toString().trim()

        if (nameProduct.isEmpty() || cost.isEmpty() || description.isEmpty()) {
            message("Vui lòng nhập đủ các thông tin")
        } else {
            val product = Product(nameProduct, cost.toLong(), description)
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val reference: DatabaseReference = database.reference
            reference.child("products").push().setValue(product)
                .addOnSuccessListener {
                    findNavController().popBackStack()
                    message("Thêm mới sản phẩm thành công")
                }
                .addOnFailureListener {
                    message(it.message ?: "Lỗi hệ thống vui lòng thử lại")
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