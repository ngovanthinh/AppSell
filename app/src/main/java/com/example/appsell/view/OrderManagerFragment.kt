package com.example.appsell.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.appsell.R
import com.example.appsell.adapter.PurchaseAdapter
import com.example.appsell.model.Purchase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_order_manager.*

class OrderManagerFragment : Fragment() {

    lateinit var adapter: PurchaseAdapter
    private val purchases: ArrayList<Purchase> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getOrderManager()

        btn_back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_order_manager, container, false)
    }

    private fun getOrderManager() {
        val allPost = Firebase.database.reference.child("purchase")

        allPost.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                purchases.clear()
                for (postSnapshot in snapshot.children) {
                    val product: Purchase? = postSnapshot.getValue(Purchase::class.java)
                    product?.let {
                        purchases.add(it)
                    }
                }
//
//                products.forEach {
//                    orderList.add(Order(it, 0))
//                }
//
                adapter = PurchaseAdapter(requireContext())
                list.adapter = adapter
                list.addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
                purchases.sortByDescending {
                    it.date
                }
                adapter.submitList(purchases, true)
//
//                adapter.updateCountListener { count, position ->
//                    orderList[position].count = count
//
//                    adapter.notifyItemChanged(position)
//                }
//
//                adapter.setOnItemClickListener {
//                    val isManger = arguments?.getBoolean(HomeFragment.MANAGER) ?: false
//                    if (isManger) {
//                        val product = orderList[it].product
//                        val gson = Gson()
//                        val json = gson.toJson(product)
//
//                        val bundle = Bundle().apply {
//                            putString(ListProductFragment.DATA, json)
//                        }
//                        findNavController().navigate(R.id.action_listProductFragment_to_newProductFragment, bundle)
//                    }
//                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
}