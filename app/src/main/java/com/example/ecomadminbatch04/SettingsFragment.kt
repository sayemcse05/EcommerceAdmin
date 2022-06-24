package com.example.ecomadminbatch04

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.ecomadminbatch04.databinding.FragmentSettingsBinding
import com.example.ecomadminbatch04.utils.showInputDialog
import com.example.ecomadminbatch04.viewmodels.ProductViewModel

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val productViewModel: ProductViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        productViewModel.getOrderSettings().observe(viewLifecycleOwner) {
            binding.settings = it
        }
        binding.deliveryChargeSetBtn.setOnClickListener {
            showInputDialog(requireContext(), title = "Delivery Charge") {
                val deliveryCharge = it.toDouble()
                productViewModel.updateOrderSettingsField("deliveryCharge", deliveryCharge)
            }
        }
        binding.discountSetBtn.setOnClickListener {
            showInputDialog(requireContext(), title = "Discount") {
                val discount = it.toInt()
                productViewModel.updateOrderSettingsField("discount", discount)
            }
        }
        binding.vatSetBtn.setOnClickListener {
            showInputDialog(requireContext(), title = "Vat") {
                val vat = it.toInt()
                productViewModel.updateOrderSettingsField("vat", vat)
            }
        }
        return binding.root
    }

}