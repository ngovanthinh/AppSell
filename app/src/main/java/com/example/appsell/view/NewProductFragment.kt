package com.example.appsell.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appsell.R
import com.example.appsell.base.Until
import com.example.appsell.model.Product
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.fragment_new_product.*
import java.util.*

class NewProductFragment : Fragment() {
    private var product: Product? = null

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


        val json = arguments?.getString(ListProductFragment.DATA)

        json?.apply {
            val gson = Gson()
            product = gson.fromJson(json, Product::class.java)
            product?.let {
                edt_name_product.setText(it.productName)
                edt_cost_product.setText("" + it.cost)
                edt_description_product.setText(it.description)
                txt_delete.isVisible = true
                txt_create.text = "Cập nhật"
                txt_header.text = "Sửa sản phẩm"
            }
        }

        btn_back.setOnClickListener {
            findNavController().popBackStack()
        }

        txt_create.setOnClickListener {
            createProduct()
        }

        txt_delete.setOnClickListener {
            if (product!=null){
                FirebaseDatabase.getInstance().reference.child("products").child(product!!.key).removeValue()
                    .addOnSuccessListener {
                        findNavController().popBackStack()
                        Until.message("Xóa thành công", requireActivity())
                    }
                    .addOnFailureListener {
                        Until.message(it.message ?: "Lỗi hệ thống vui lòng thử lại", requireActivity())
                    }
            }

        }
    }

    private fun createProduct() {
        val nameProduct = edt_name_product.text.toString().trim()
        val cost = edt_cost_product.text.toString().trim()
        val description = edt_description_product.text.toString().trim()

        if (nameProduct.isEmpty() || cost.isEmpty() || description.isEmpty()) {
            Until.message("Vui lòng nhập đủ các thông tin", requireActivity())
        } else {

            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val reference: DatabaseReference = database.reference

            if (product != null) {
                val productUpdate = Product(nameProduct, cost.toLong(), description, product!!.key)
                reference.child("products").child(productUpdate.key).setValue(productUpdate)
                    .addOnSuccessListener {
                        findNavController().popBackStack()
                        Until.message("Cập nhật sản phẩm thành công", requireActivity())
                    }
                    .addOnFailureListener {
                        Until.message(it.message ?: "Lỗi hệ thống vui lòng thử lại", requireActivity())
                    }

            } else {
                val key = database.reference.push().key!!
                val productCreate = Product(nameProduct, cost.toLong(), description, key)
                reference.child("products").child(key).setValue(productCreate)
                    .addOnSuccessListener {
                        findNavController().popBackStack()
                        Until.message("Thêm mới sản phẩm thành công", requireActivity())
                    }
                    .addOnFailureListener {
                        Until.message(it.message ?: "Lỗi hệ thống vui lòng thử lại", requireActivity())
                    }
            }


        }
    }

}