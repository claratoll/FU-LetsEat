package com.example.letseat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class DisplayOneRestaurantActivity : AppCompatActivity() {

    private lateinit var nameView: TextView
    private lateinit var addressView: TextView
    private lateinit var pointsView: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private val restaurantList = mutableListOf<Restaurant>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_one_restaurant)

        db = Firebase.firestore
        auth = Firebase.auth

        nameView = findViewById(R.id.ResNameTextView)
        addressView = findViewById(R.id.AddressNameView)
        pointsView = findViewById(R.id.ResPointsTextView)
        var dishImage = findViewById<ImageView>(R.id.DishDisplayImageView)

        val restaurantId = intent.getIntExtra("documentID", 999)

        getUserData(restaurantId)


        addressView.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            Log.v("!!!" , "restaurantPosition $restaurantId")
            intent.putExtra("documentID", restaurantId)
            this.startActivity(intent)
        }


    }


    private fun getUserData(restaurantId: Int) {


        val docRef = auth.currentUser?.let {
            db.collection("restaurants")
                .addSnapshotListener { snapshot, e ->
                    restaurantList.clear()
                    if (snapshot != null) {
                        val restArray = mutableListOf<Restaurant>()
                        for (document in snapshot.documents) {
                            val restaurantDoc = document.toObject<Restaurant>()
                            if (restaurantDoc != null) {
                                restArray.add(restaurantDoc)
                            }
                        }
                        restaurantList.addAll(restArray)
                        placeName(restaurantId)
                    }
                }

        }
    }

    fun placeName(restaurantId: Int) {

        val restaurant = restaurantList[restaurantId]

        nameView.text = restaurant.restaurantName.toString()
        addressView.text = restaurant.address.toString()
        pointsView.text = "The restaurant has ${restaurant.points}"

    }
}