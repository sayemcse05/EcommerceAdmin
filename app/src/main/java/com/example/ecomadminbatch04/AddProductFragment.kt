package com.example.ecomadminbatch04

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.example.ecomadminbatch04.databinding.FragmentAddProductBinding
import com.example.ecomadminbatch04.viewmodels.ProductViewModel
import com.example.ecomadminbatch04.customdialogs.DatePickerFragment
import com.example.ecomadminbatch04.models.Product
import com.example.ecomadminbatch04.models.Purchase
import com.example.ecomadminbatch04.utils.getFormattedDate
import com.google.firebase.Timestamp

class AddProductFragment : Fragment() {
    private val productViewModel: ProductViewModel by activityViewModels()
    private lateinit var binding: FragmentAddProductBinding
    private var category: String? = null
    private var imageUrl: String? = null
    private var bitmap: Bitmap? = null
    private var timeStamp: Timestamp? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductBinding.inflate(inflater, container, false)
        productViewModel.getCategories().observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                val adapter = ArrayAdapter<String>(requireActivity(),
                android.R.layout.simple_spinner_dropdown_item, it)
                binding.catSP.adapter = adapter
            }
        }

        binding.catSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                category = parent!!.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        binding.dateBtn.setOnClickListener {
            DatePickerFragment {
                timeStamp = it
                binding.dateBtn.text = getFormattedDate(it.seconds * 1000, "dd/MM/yyyy")
            }.show(childFragmentManager, null)
        }

        binding.captureBtn.setOnClickListener {
            dispatchTakePictureIntent()
        }

        binding.saveBtn.setOnClickListener {
            val name = binding.nameInputET.text.toString()
            val description = binding.descriptionInputET.text.toString()
            val purchaseprice = binding.purchasePriceET.text.toString()
            val salePrice = binding.salePriceET.text.toString()
            val qty = binding.quantityET.text.toString()
            // TODO: validate fields
            binding.mProgressbar.visibility = View.VISIBLE
            productViewModel.uploadImage(bitmap!!) {downloadUrl ->
                imageUrl = downloadUrl
                val product = Product(
                    name = name,
                    description = description,
                    salePrice = salePrice.toDouble(),
                    category = category,
                    imageUrl = imageUrl
                )
                val purchase = Purchase(
                    purchaseDate = timeStamp,
                    purchasePrice = purchaseprice.toDouble(),
                    quantity = qty.toDouble()
                )
                productViewModel.addProduct(product, purchase)
            }
        }

        productViewModel.statusLD.observe(viewLifecycleOwner) {
            if (it == "Success") {
                binding.mProgressbar.visibility = View.GONE
                resetFields()
            }else {
                binding.mProgressbar.visibility = View.GONE
                Toast.makeText(requireActivity(), "failed to save", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun resetFields() {
        binding.nameInputET.text = null
        binding.descriptionInputET.text = null
        binding.quantityET.text = null
        binding.purchasePriceET.text = null
        binding.salePriceET.text = null
    }

    val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            bitmap = it.data?.extras?.get("data") as Bitmap
            binding.productIV.setImageBitmap(bitmap)
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            resultLauncher.launch(takePictureIntent)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

}