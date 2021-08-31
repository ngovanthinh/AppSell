package com.example.appsell.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.appsell.R
import com.example.appsell.adapter.PurchaseAdapter
import com.example.appsell.base.Constant
import com.example.appsell.base.Until
import com.example.appsell.model.Purchase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
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

        Until.showLoading(requireActivity())
        allPost.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                purchases.clear()
                for (postSnapshot in snapshot.children) {
                    val product: Purchase? = postSnapshot.getValue(Purchase::class.java)
                    product?.let {
                        if (it.statusOrder == Constant.ODER || it.statusOrder == Constant.SELL) {
                            purchases.add(it)
                        }
                    }
                }

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
                Until.hideLoading()

                adapter.setOnItemClickListener { position ->
                    if (adapter.data[position].statusOrder == Constant.ODER) {
                        updateStatusPayment(position)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Until.hideLoading()
            }
        })

    }

    private fun updateStatusPayment(position: Int) {
        val dialog = ManagerHandleProductDialog()
        dialog.show(childFragmentManager, "")

        dialog.handlePayment { isPayment ->
            val data = adapter.data[position]

            if (isPayment) {
                data.statusOrder = Constant.SELL
            } else {
                data.statusOrder = Constant.CANCEL
            }

            Until.showLoading(requireActivity())

            FirebaseDatabase.getInstance().reference.child("purchase").child("" + data.date).setValue(data)
                .addOnSuccessListener {
                    if (isPayment) {
                        adapter.data[position].statusOrder = Constant.SELL
                        adapter.notifyItemChanged(position)
                        Until.message(
                            requireContext().getString(R.string.accept_payment_message),
                            requireActivity()
                        )
                    } else {
                        adapter.notifyItemRemoved(position)
                        purchases.removeAt(position)
                        Until.message(
                            requireContext().getString(R.string.cancel_payment_message),
                            requireActivity()
                        )
                    }

                    Until.hideLoading()
                }
                .addOnFailureListener {
                    Until.message(it.message ?: "Lỗi hệ thống vui lòng thử lại", requireActivity())
                    Until.hideLoading()
                }
        }
    }

}