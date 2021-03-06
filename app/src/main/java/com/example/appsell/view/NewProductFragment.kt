package com.example.appsell.view

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.annotation.MenuRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.appsell.R
import com.example.appsell.base.Constant
import com.example.appsell.base.Until
import com.example.appsell.model.Product
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_new_product.*


class NewProductFragment : Fragment() {
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null
    var isManager: Boolean = true

    private var product: Product? = null
    private var type: String = Constant.PRODUCT_VEGETABLE
    private var startForResult: ActivityResultLauncher<String>? = null
    lateinit var uri: Uri
    var path: String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callBack: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragment?.findNavController()?.popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callBack)

        startForResult = registerForActivityResult(
            GetContent()
        ) { uri ->
            uri?.let {
                this.uri = it
                uploadImage()
            }
        }

        storage = FirebaseStorage.getInstance();
        storageReference = storage!!.reference;

        isManager = arguments?.getBoolean(HomeFragment.MANAGER) ?: true

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

        edt_cost_product.isEnabled = isManager
        edt_name_product.isEnabled = isManager
        edt_description_product.isEnabled = isManager

        if (isManager) {
            btn_updateImage.visibility = View.VISIBLE
            view_action.visibility = View.VISIBLE
        } else {
            btn_updateImage.visibility = View.GONE
            view_action.visibility = View.GONE
        }

        json?.apply {
            val gson = Gson()
            product = gson.fromJson(json, Product::class.java)
            product?.let {
                edt_name_product.setText(it.productName)
                edt_cost_product.setText("" + it.cost)
                edt_description_product.setText(it.description)
                txt_delete.isVisible = true
                txt_create.text = "C???p nh???t"
                txt_header.text = "S???a s???n ph???m"
                Glide.with(requireContext())
                    .load(it.urlImage)
                    .into(img_upload)
            }

            if (product != null) {
                if (!isManager){
                    txt_header.text = "Th??ng tin s???n ph???m"
                }

                if (product!!.urlImage.isEmpty()) {
                    if (!isManager){
                        txt_image_description.setText(R.string.picture_invisible)
                    }
                    txt_image_description.visibility = View.VISIBLE
                } else {
                    txt_image_description.visibility = View.GONE
                }
            } else {
                txt_image_description.visibility = View.VISIBLE
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
                        Until.message("X??a th??nh c??ng", requireActivity())
                    }
                    .addOnFailureListener {
                        Until.message(it.message ?: "L???i h??? th???ng vui l??ng th??? l???i", requireActivity())
                    }
            }

        }

        type_product.setOnClickListener { v: View ->
            if (isManager){
                showMenu(v, R.menu.menu)
            }
        }

        btn_updateImage.setOnClickListener {
            chooseImage()
        }
    }

    private fun createProduct() {
        val nameProduct = edt_name_product.text.toString().trim()
        val cost = edt_cost_product.text.toString().trim()
        val description = edt_description_product.text.toString().trim()

        if (nameProduct.isEmpty() || cost.isEmpty() || description.isEmpty()) {
            Until.message("Vui l??ng nh???p ????? c??c th??ng tin", requireActivity())
        } else {

            Until.showLoading(requireActivity())
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val reference: DatabaseReference = database.reference

            if (product != null) {
                // s???a s???n ph???m
                val productUpdate =
                    Product(nameProduct, cost.toLong(), description, product!!.key, type, product!!.urlImage)
                reference.child("products").child(type).child(productUpdate.key).setValue(productUpdate)
                    .addOnSuccessListener {
                        findNavController().popBackStack()
                        Until.message("C???p nh???t s???n ph???m th??nh c??ng", requireActivity())
                        Until.hideLoading()
                    }
                    .addOnFailureListener {
                        Until.message(it.message ?: "L???i h??? th???ng vui l??ng th??? l???i", requireActivity())
                        Until.hideLoading()
                    }

            } else {
                // T???o m???i s???n ph???m
                val key = database.reference.push().key!!
                val productCreate = Product(nameProduct, cost.toLong(), description, key, type, path)
                reference.child("products").child(type).child(key).setValue(productCreate)
                    .addOnSuccessListener {
                        Until.hideLoading()
                        findNavController().popBackStack()
                        Until.message("Th??m m???i s???n ph???m th??nh c??ng", requireActivity())
                    }
                    .addOnFailureListener {
                        Until.hideLoading()
                        Until.message(it.message ?: "L???i h??? th???ng vui l??ng th??? l???i", requireActivity())
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

    private fun uploadImage() {
        Until.showLoading(requireActivity())
        var uploadTask: UploadTask
        val storageReference: StorageReference = FirebaseStorage.getInstance().getReference("profile_manager")
        val local: StorageReference = storageReference.child("" + System.currentTimeMillis() + "." + getFile(uri))
        uploadTask = local.putFile(uri)

        uploadTask.continueWithTask { p0 ->
            if (!p0.isSuccessful) {
                Until.message(p0.exception?.message ?: "L???i h??? th???ng vui l??ng th??? l???i", requireActivity())
            }
            Until.hideLoading()
            local.downloadUrl
        }.addOnCompleteListener { value ->
            if (value.isSuccessful) {
                try {
                    path = value.result.toString()
                    Glide.with(requireActivity())
                        .load(value.result)
                        .apply(RequestOptions())
                        .fitCenter()
                        .into(img_upload)

                    product?.let {
                        product!!.urlImage = value.result.toString()
                    }

                    txt_image_description.visibility = View.GONE
                } catch (e: Exception) {
                    Until.message(e.message ?: "L???i h??? th???ng vui l??ng th??? l???i", requireActivity())
                }

                Until.hideLoading()
            }
        }
    }

    private fun getFile(uri: Uri): String {
        val contentResolver: ContentResolver = requireActivity().contentResolver
        val mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))!!
    }

    private fun chooseImage() {
        startForResult?.launch("image/*")
    }

}