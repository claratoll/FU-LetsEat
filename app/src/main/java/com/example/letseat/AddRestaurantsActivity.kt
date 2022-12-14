package com.example.letseat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddRestaurantsActivity : AppCompatActivity() {

    lateinit var db : FirebaseFirestore
    lateinit var nameView : EditText
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_restaurants)


        auth = Firebase.auth
        db = Firebase.firestore


        nameView = findViewById(R.id.EditRestaurantsView)

        val saveButton = findViewById<Button>(R.id.SaveButton)
        saveButton.setOnClickListener {
            saveItem()
        }

        val mapsButton = findViewById<Button>(R.id.MapButton)
        mapsButton.setOnClickListener {
            goToMapsActivity()
        }

        val restaurantsButton = findViewById<Button>(R.id.restaurantsButton)
        restaurantsButton.setOnClickListener {
            goToRestaurantsActivity()
        }
    }

    fun saveItem(){
        val item = Restaurant(nameView.text.toString())
        nameView.setText("")

        val user = auth.currentUser
        if (user == null) {
            return
        }

        db.collection("users").document(user.uid)
            .collection("restaurants").add(item)

    }


    fun goToMapsActivity(){
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    fun goToRestaurantsActivity(){
        val intent = Intent(this, RestaurantsActivity::class.java)
        startActivity(intent)
    }


}
