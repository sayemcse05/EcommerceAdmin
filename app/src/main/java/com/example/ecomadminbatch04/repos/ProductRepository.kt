package com.example.ecomadminbatch04.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ecomadminbatch04.models.OrderSettings
import com.example.ecomadminbatch04.models.Product
import com.example.ecomadminbatch04.models.Purchase
import com.example.ecomadminbatch04.utils.*
import com.google.firebase.firestore.FirebaseFirestore

class ProductRepository {

    val db = FirebaseFirestore.getInstance()

    fun addProduct(product: Product, purchase: Purchase, callback: (String) -> Unit) {
        val wb = db.batch()
        val productDocRef = db.collection(collectionProduct).document()
        val purchaseDocRef = db.collection(collectionPurchase).document()
        product.id = productDocRef.id
        purchase.purchaseId = purchaseDocRef.id
        purchase.productId = product.id

        wb.set(productDocRef, product)
        wb.set(purchaseDocRef, purchase)
        wb.commit().addOnSuccessListener {
            callback("Success")
        }.addOnFailureListener {
            callback("Failure")
        }
    }

    fun addNewPurchase(purchase: Purchase) {
        val purchaseDoc = db.collection(collectionPurchase).document()
        purchase.purchaseId = purchaseDoc.id
        purchaseDoc.set(purchase)
            .addOnFailureListener {

            }.addOnSuccessListener {

            }
    }

    fun getOrderSettings() : LiveData<OrderSettings> {
        val settingsLD = MutableLiveData<OrderSettings>()
        db.collection(collectionOrderSettings).document(documentOrderSettings)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                settingsLD.value = value?.toObject(OrderSettings::class.java)
            }
        return settingsLD
    }

    fun updateOrderSettingsField(field: String, value: Any) {
        db.collection(collectionOrderSettings).document(documentOrderSettings)
            .update(field, value)
            .addOnSuccessListener {

            }.addOnFailureListener {

            }
    }

    fun getAllCategories() : LiveData<List<String>> {
        val catLD = MutableLiveData<List<String>>()
        db.collection(collectionCategory)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                val tempList = mutableListOf<String>()
                for (doc in value!!.documents) {
                    tempList.add(doc.get("name").toString())
                }
                catLD.value = tempList
            }
        return catLD
    }

    fun getAllProducts() : LiveData<List<Product>> {
        val productLD = MutableLiveData<List<Product>>()
        db.collection(collectionProduct)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                val tempList = mutableListOf<Product>()
                for (doc in value!!.documents) {
                    doc.toObject(Product::class.java)?.let { tempList.add(it) }
                }
                productLD.value = tempList
            }
        return productLD
    }

    fun getProductByProductId(id: String) : LiveData<Product> {
        val productLD = MutableLiveData<Product>()
        db.collection(collectionProduct).document(id)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                productLD.value = value?.toObject(Product::class.java)
            }
        return productLD
    }

    fun getPurchaseHistoryByProductId(id: String) : LiveData<List<Purchase>> {
        val purchaseLD = MutableLiveData<List<Purchase>>()
        db.collection(collectionPurchase)
            .whereEqualTo("productId", id)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                val tempList = mutableListOf<Purchase>()
                for (doc in value!!.documents) {
                    doc.toObject(Purchase::class.java)?.let { tempList.add(it) }
                }
                purchaseLD.value = tempList
            }
        return purchaseLD
    }
}