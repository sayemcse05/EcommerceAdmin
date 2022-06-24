package com.example.ecomadminbatch04

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import com.example.ecomadminbatch04.customdialogs.DatePickerFragment
import com.example.ecomadminbatch04.databinding.FragmentProductDetailsBinding
import com.example.ecomadminbatch04.databinding.FragmentViewProductBinding
import com.example.ecomadminbatch04.databinding.RepurchaseLayoutBinding
import com.example.ecomadminbatch04.models.Purchase
import com.example.ecomadminbatch04.utils.getFormattedDate
import com.example.ecomadminbatch04.viewmodels.ProductViewModel
import com.google.firebase.Timestamp

class ProductDetailsFragment : Fragment() {
    private lateinit var binding: FragmentProductDetailsBinding
    private val productViewModel: ProductViewModel by activityViewModels()
    private var id: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        id = arguments?.getString("id")
        id?.let {
            productViewModel.getProductByProductId(it).observe(viewLifecycleOwner) {
                binding.product = it
            }
        }
        id?.let {
            productViewModel.getPurchasesByProductId(it).observe(viewLifecycleOwner) {
                var purchaseTxt = ""
                it.forEach {
                    purchaseTxt += "${getFormattedDate(it.purchaseDate!!.seconds * 1000, "dd/MM/yyyy")} - Qty: ${it.quantity} - Price: ${it.purchasePrice}\n"
                }
                binding.purchaseListTV.text = purchaseTxt
            }
        }
        binding.repurchaseBtn.setOnClickListener {
            showRepurchaseDialog()
        }
        return binding.root
    }

    private fun showRepurchaseDialog() {
        var purchaseDate: Timestamp? = null
        val builder = AlertDialog.Builder(requireActivity()).apply {
            setTitle("Re-Purchase Product")
            val binding = RepurchaseLayoutBinding.inflate(LayoutInflater.from(requireContext()))
            binding.repurchaseDateBtn.setOnClickListener {
                DatePickerFragment{
                    purchaseDate = it
                    binding.repurchaseDateBtn.text = getFormattedDate(it.seconds * 1000, "dd/MM/yyyy")
                }.show(childFragmentManager, null)
            }
            setView(binding.root)
            setPositiveButton("Save") {dialog, value ->
                val price = binding.repurchasePriceET.text.toString()
                val qty = binding.repurchaseQuantityET.text.toString()
                // TODO: validate field
                val purchase = Purchase(
                    productId = id,
                    purchasePrice = price.toDouble(),
                    purchaseDate = purchaseDate,
                    quantity = qty.toDouble()
                )
                productViewModel.addPurchase(purchase)
            }
            setNegativeButton("Close", null)
        }
        builder.create().show()
    }

}