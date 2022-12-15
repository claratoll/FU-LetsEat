package com.example.letseat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DisplayOneRestaurantActivity : AppCompatActivity() {

    lateinit var nameView : TextView
    lateinit var addressView: TextView
    lateinit var pointsView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_one_restaurant)

        var db = Firebase.database.reference

        nameView = findViewById(R.id.ResNameTextView)
        addressView = findViewById(R.id.AddressNameView)
        pointsView = findViewById(R.id.ResPointsTextView)
        var dishImage = findViewById<ImageView>(R.id.DishDisplayImageView)


        db.child("users").child("Restaurants").get().addOnSuccessListener {
            Log.i("!!!", "Got value ${it.value}")
        }.addOnFailureListener{
            Log.e("!!!", "Error getting data", it)
        }

    }
}