package com.example.ecomadminbatch04.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginViewModel : ViewModel() {
    enum class Authentication {
        AUTHENTICATED, UNAUTHENTICATED
    }
    val authLD: MutableLiveData<Authentication> = MutableLiveData()
    val errMsgLD: MutableLiveData<String> = MutableLiveData()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    init {
        if (auth.currentUser != null) {
            authLD.value = Authentication.AUTHENTICATED
        } else {
            authLD.value = Authentication.UNAUTHENTICATED
        }
    }

    fun loginAdmin(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                val uid = auth.currentUser!!.uid
                db.collection("Admins")
                    .document(uid)
                    .get()
                    .addOnSuccessListener {
                        if (it.exists()) {
                            authLD.value = Authentication.AUTHENTICATED
                        } else {
                            errMsgLD.value = "You are not an Admin"
                            auth.signOut()
                        }
                    }.addOnFailureListener {
                        errMsgLD.value = "You are not an Admin"
                        auth.signOut()
                    }
                //authLD.value = Authentication.AUTHENTICATED
            }.addOnFailureListener {
                errMsgLD.value = it.localizedMessage
            }
    }
}