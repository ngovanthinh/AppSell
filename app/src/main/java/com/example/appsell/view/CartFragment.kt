package com.example.appsell.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.appsell.R
import com.example.appsell.adapter.ProductAdapter
import com.example.appsell.base.Constant
import com.example.appsell.base.Until
import com.example.appsell.model.Order
import com.example.appsell.model.Profile
import com.example.appsell.model.Purchase
import com.example.appsell.viewmodel.MainViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_cart.btn_back
import kotlinx.android.synthetic.main.fragment_cart.list
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_list_product.*
import java.util.*
import kotlin.collections.ArrayList

class CartFragment : Fragment() {
    lateinit var viewModel: MainViewModel
    var profile: Profile = Profile()
    lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callBack: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragment?.findNavController()?.popBackStack(R.id.homeFragment, false)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callBack)

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        getProfile()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val json = arguments?.getString(ListProductFragment.CART)
        val gson = Gson()

        val typeToken = object : TypeToken<ArrayList<Order>>() {}.type
//        val orders = gson.fromJson<ArrayList<Order>>(json, typeToken)

        var totalCost: Int = 0
        viewModel.listProduct.forEach {
            totalCost += it.count * it.product?.cost?.toInt()!!
        }

        txt_total.text = "$totalCost VND"

        adapter = ProductAdapter(requireContext(), true)
        list.adapter = adapter
        list.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        adapter.submitList(viewModel.listProduct, true)

        btn_back.setOnClickListener {
            findNavController().popBackStack(R.id.homeFragment, false)
        }

        txtPayment.setOnClickListener {
            val dialog = PaymentDialog()
            dialog.show(childFragmentManager, this.tag)

            dialog.setOnItemClickListener {
                payment(viewModel.listProduct, it)
            }
        }
    }

    private fun payment(orders: ArrayList<Order>, typePayment: String) {
        val calendar = Calendar.getInstance()
        val time: Long = calendar.time.time
        val purchase = Purchase(time, profile, orders, Constant.ODER, typePayment)

        FirebaseDatabase.getInstance().reference.child("purchase").child("" + time).setValue(purchase)
            .addOnSuccessListener {
                Until.message(requireContext().getString(R.string.payment_message), requireActivity())
                findNavController().popBackStack(R.id.homeFragment, false)
                viewModel.listProduct.clear()
            }
            .addOnFailureListener {
                Until.message(it.message ?: "Lỗi hệ thống vui lòng thử lại", requireActivity())
            }
    }

    private fun getProfile() {
        val email: String = arguments?.getString(LoginFragment.EMAIL)!!
        val allPost = Firebase.database.reference.child("username").child(email.replace(".", ""))

        allPost.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                profile = snapshot.getValue(Profile::class.java)!!
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}