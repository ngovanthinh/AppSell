package com.example.appsell.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.appsell.R
import com.example.appsell.adapter.ProductAdapter
import com.example.appsell.base.Until
import com.example.appsell.model.Order
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_cart.btn_back
import kotlinx.android.synthetic.main.fragment_cart.list
import kotlinx.android.synthetic.main.fragment_list_product.*

class CartFragment : Fragment() {

    lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callBack: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragment?.findNavController()?.popBackStack(R.id.homeFragment, false)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callBack)
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
        val topic = gson.fromJson<ArrayList<Order>>(json, typeToken)

        var totalCost: Int = 0
        topic.forEach {
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

        adapter.submitList(topic, true)

        btn_back.setOnClickListener {
            findNavController().popBackStack(R.id.homeFragment, false)
        }

        txtPayment.setOnClickListener {
            val dialog = PaymentDialog()
            dialog.show(childFragmentManager, this.tag)

            dialog.setOnItemClickListener {
                Until.message(requireContext().getString(R.string.payment_message), requireActivity())
                findNavController().popBackStack(R.id.homeFragment, false)
            }
        }
    }

}