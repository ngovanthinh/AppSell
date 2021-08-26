package com.example.appsell.view

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
import com.example.appsell.model.Order
import com.example.appsell.model.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_list_product.*

/**
 * Created by ThinhNV on 23/08/2021.
 */
class ListProductFragment : Fragment() {

    lateinit var adapter: ProductAdapter
    private val products: ArrayList<Product> = ArrayList()
    val orderList = ArrayList<Order>()

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
        return inflater.inflate(R.layout.fragment_list_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val allPost = Firebase.database.reference.child("products")

        allPost.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    val product: Product? = postSnapshot.getValue(Product::class.java)
                    product?.let {
                        products.add(it)
                    }
                }

                products.forEach {
                    orderList.add(Order(it, 0))
                }

                adapter = ProductAdapter(requireContext(), false)
                list.adapter = adapter
                list.addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
                adapter.submitList(orderList, true)

                adapter.updateCountListener { count, position ->
                    orderList[position].count = count

                    adapter.notifyItemChanged(position)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        btn_back.setOnClickListener {
            findNavController().popBackStack()
        }

        txtOpenCart.setOnClickListener {
            val carts = ArrayList<Order>()
            val bundle = Bundle().apply {
                val gson = Gson()
                orderList.forEach {
                    if (it.count > 0) {
                        carts.add(it)
                    }
                }

                putString(CART, gson.toJson(carts))
            }

            findNavController().navigate(R.id.action_listProductFragment_to_cartFragment, bundle)
        }
    }

    companion object {
        const val CART: String = "cart_data"
    }

}