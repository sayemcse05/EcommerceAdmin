package com.example.ecomadminbatch04

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecomadminbatch04.adapters.ProductAdapter
import com.example.ecomadminbatch04.databinding.FragmentViewProductBinding
import com.example.ecomadminbatch04.viewmodels.ProductViewModel

class ViewProductFragment : Fragment() {
    private lateinit var binding: FragmentViewProductBinding
    private val productViewModel: ProductViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewProductBinding.inflate(inflater, container, false)
        val adapater = ProductAdapter {productId ->
            findNavController()
                .navigate(R.id.action_viewProductFragment_to_productDetailsFragment,
                args = bundleOf("id" to productId))
        }
        binding.productRV.layoutManager = LinearLayoutManager(requireActivity())
        binding.productRV.adapter = adapater
        productViewModel.getProducts().observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.emptyListMsgTV.visibility = View.VISIBLE
            }else {
                binding.emptyListMsgTV.visibility = View.GONE
            }
            adapater.submitList(it)
        }
        return binding.root
    }

}