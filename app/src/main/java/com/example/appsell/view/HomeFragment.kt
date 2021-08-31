package com.example.appsell.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.appsell.R
import com.example.appsell.adapter.SliderAdapter
import com.example.appsell.base.Constant
import com.example.appsell.model.Profile
import com.example.appsell.model.SliderItem
import com.example.appsell.viewmodel.MainViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    lateinit var viewModel: MainViewModel

    private var isManager: Boolean = false
    lateinit var adapter: SliderAdapter
    private val sliderItems: ArrayList<SliderItem> = ArrayList()

    var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        sliderItems.add(SliderItem(R.drawable.img_1, ""))
        sliderItems.add(SliderItem(R.drawable.img_2, ""))
        sliderItems.add(SliderItem(R.drawable.img_3, ""))
        sliderItems.add(SliderItem(R.drawable.img_4, ""))

        adapter = SliderAdapter(requireContext(), sliderItems)

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        sharedPref?.let {
            email = sharedPref.getString(Constant.USER_PROFILE, null)
            if (email.isNullOrEmpty()) {
                email = arguments?.getString(LoginFragment.EMAIL)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageSlider.apply {
            setSliderAdapter(adapter)
            setIndicatorAnimation(IndicatorAnimationType.WORM)
            setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
            indicatorSelectedColor = Color.WHITE
            indicatorUnselectedColor = ContextCompat.getColor(requireContext(), R.color.white);
            imageSlider.scrollTimeInSec = 3;
            imageSlider.startAutoCycle();
        }

        btn_logout.setOnClickListener {
            val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
            with(sharedPref!!.edit()) {
                putString(Constant.USER_PROFILE, null)
                apply()
            }

            findNavController().navigate(R.id.action_homeFragment_to_LoginFragment)
        }

        btn_new_product.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_newProductFragment)
        }

        btn_call.setOnClickListener {
            makeCall(requireContext().getString(R.string.phone_number))
        }

        btn_list_product.setOnClickListener {
            val bundle = Bundle().apply {
                putBoolean(MANAGER, isManager)
                putString(LoginFragment.EMAIL, email)
            }

            findNavController().navigate(R.id.action_homeFragment_to_productFragment, bundle)
        }

        btnOpeOrderManager.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_orderManagerFragment)
        }

        btnOpenProfile.setOnClickListener {
            email?.let {
                val bundle = Bundle().apply {
                    putString(LoginFragment.EMAIL, it)
                }
                findNavController().navigate(R.id.action_homeFragment_to_profileFragment, bundle)
            }
        }

    }

    private fun makeCall(number: String) {
        if (!TextUtils.isEmpty(number)) {
            val call = Intent(Intent.ACTION_DIAL)
            call.data = Uri.parse("tel:$number")
            startActivity(call)
        }
    }

    override fun onResume() {
        super.onResume()
        getUserProfile()
    }

    companion object {
        const val MANAGER: String = "is_manager"
    }

    private fun getUserProfile() {
        val allPost = Firebase.database.reference.child("username").child(email!!.replace(".", ""))

        allPost.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val profile: Profile? = snapshot.getValue(Profile::class.java)
                isManager = profile?.isManager ?: false
                if (lytNewProduct != null) {

                    if (profile?.isManager!!) {
                        lytNewProduct.visibility = View.VISIBLE
                        lytOrderManager.visibility = View.VISIBLE
                    } else {
                        lytNewProduct.visibility = View.INVISIBLE
                        lytOrderManager.visibility = View.GONE
                    }

                    txt_user_name.text = profile.userName
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}