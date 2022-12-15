package com.example.letseat

import android.content.ClipData
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.jar.Attributes
import kotlin.math.log

class DisplayOneRestaurantActivity : AppCompatActivity() {

    lateinit var nameView : TextView
    lateinit var addressView: TextView
    lateinit var pointsView: TextView

    lateinit var auth : FirebaseAuth
    lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_one_restaurant)

        db = Firebase.firestore
        auth = Firebase.auth

        nameView = findViewById(R.id.ResNameTextView)
        addressView = findViewById(R.id.AddressNameView)
        pointsView = findViewById(R.id.ResPointsTextView)
        var dishImage = findViewById<ImageView>(R.id.DishDisplayImageView)

        Log.v("!!!", "hejhej")

        val user = auth.currentUser

        val restaurantList = mutableListOf<Restaurant>()


        if (user != null) {
            db.collection("users").document(user.uid).collection("restaurants")
                .addSnapshotListener { snapshot, e ->
                    restaurantList.clear()
                    if (snapshot != null) {
                        for (document in snapshot.documents) {
                            val item = document.toObject<Restaurant>()
                            if (item != null) {
                                restaurantList.add(item)
                            }
                        }
                    }
                    for (item in restaurantList) {
                        Log.d("!!!", "$item")
                    }
                }
        }


        //if (user != null) {
            /*      db.collection("users").document(user.uid)
                .collection("restaurants").get().addOnCompleteListener(){
                    Log.v("!!!", "get restaurant")
                    nameView = colle
            }
        */

      /*  val docRef = db.collection("users").document("restaurants")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("!!!", "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d("!!!", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("!!!", "get failed with ", exception)
            }

*/



    }
}