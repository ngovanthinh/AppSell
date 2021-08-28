package com.example.appsell.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.MenuRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appsell.R
import com.example.appsell.base.Constant
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
    private var type: String = Constant.PRODUCT_VEGETABLE

    @SuppressLint("SetTextI18n")
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

        if (product != null) {
            type = product!!.type
            val text = when (product!!.type) {
                Constant.PRODUCT_VEGETABLE -> requireContext().getString(R.string.type_product) + " " + requireContext().getString(
                    R.string.vegetable
                )
                Constant.PRODUCT_PACKAGED -> requireContext().getString(R.string.type_product) + " " + requireContext().getString(
                    R.string.packaged_food
                )
                Constant.PRODUCT_MEAT -> requireContext().getString(R.string.type_product) + " " + requireContext().getString(
                    R.string.fresh_meat
                )
                Constant.PRODUCT_DIFFERENT -> requireContext().getString(R.string.type_product) + " " + requireContext().getString(
                    R.string.different
                )
                else -> {
                    type = Constant.PRODUCT_VEGETABLE
                    requireContext().getString(R.string.type_product) + " " + requireContext().getString(
                        R.string.vegetable
                    )
                }
            }
            type_product.text = text

        } else {
            type_product.text =
                requireContext().getString(R.string.type_product) + " " + requireContext().getString(R.string.vegetable)
        }

        btn_back.setOnClickListener {
            findNavController().popBackStack()
        }

        txt_create.setOnClickListener {
            createProduct()
        }

        txt_delete.setOnClickListener {
            if (product != null) {
                FirebaseDatabase.getInstance().reference.child("products").child(type).child(product!!.key)
                    .removeValue()
                    .addOnSuccessListener {
                        findNavController().popBackStack()
                        Until.message("Xóa thành công", requireActivity())
                    }
                    .addOnFailureListener {
                        Until.message(it.message ?: "Lỗi hệ thống vui lòng thử lại", requireActivity())
                    }
            }

        }

        type_product.setOnClickListener { v: View ->
            showMenu(v, R.menu.menu)
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
                val productUpdate = Product(nameProduct, cost.toLong(), description, product!!.key, type)
                reference.child("products").child(type).child(productUpdate.key).setValue(productUpdate)
                    .addOnSuccessListener {
                        findNavController().popBackStack()
                        Until.message("Cập nhật sản phẩm thành công", requireActivity())
                    }
                    .addOnFailureListener {
                        Until.message(it.message ?: "Lỗi hệ thống vui lòng thử lại", requireActivity())
                    }

            } else {
                val key = database.reference.push().key!!
                val productCreate = Product(nameProduct, cost.toLong(), description, key, type)
                reference.child("products").child(type).child(key).setValue(productCreate)
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

    @SuppressLint("SetTextI18n")
    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener {
            val text = when (it!!.itemId) {
                R.id.header1 -> {
                    type = Constant.PRODUCT_VEGETABLE
                    requireContext().getString(R.string.type_product) + " " + requireContext().getString(R.string.vegetable)
                }

                R.id.header2 -> {
                    type = Constant.PRODUCT_PACKAGED
                    requireContext().getString(R.string.type_product) + " " + requireContext().getString(R.string.packaged_food)
                }

                R.id.header3 -> {
                    type = Constant.PRODUCT_MEAT
                    requireContext().getString(R.string.type_product) + " " + requireContext().getString(R.string.fresh_meat)
                }

                R.id.header4 -> {
                    type = Constant.PRODUCT_DIFFERENT
                    requireContext().getString(R.string.type_product) + " " + requireContext().getString(R.string.different)
                }
                else -> ""
            }

            type_product.text = text

            true
        }

        popup.show()
    }

}