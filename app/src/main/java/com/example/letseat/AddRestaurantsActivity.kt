package com.example.letseat

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.IOException


class AddRestaurantsActivity : AppCompatActivity() {

    lateinit var db : FirebaseFirestore
    lateinit var auth : FirebaseAuth


    lateinit var nameView : EditText
    lateinit var addressView : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_restaurants)


        auth = Firebase.auth
        db = Firebase.firestore


        nameView = findViewById(R.id.EditRestaurantNameView)
        addressView = findViewById(R.id.EditAddressView)

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
        //function to add a new restaurant to the database

        val itemName = nameView.text.toString()
        val itemAddress = addressView.text.toString()

        nameView.setText("")
        addressView.setText("")


        val user = auth.currentUser
        if (user == null) {
            return
        }

        //send address to function to get the GeoPoints
        val geoPointAddress = getLocationFromAddress(itemAddress)

        Log.v("!!!", "$geoPointAddress")

        val item = Restaurant(restaurantName = itemName, address = itemAddress, position = geoPointAddress)

        //adds location to database
        db.collection("restaurants").add(item)

    }


    fun goToMapsActivity(){
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    fun goToRestaurantsActivity(){
        val intent = Intent(this, ShowAllRestaurantsActivity::class.java)
        startActivity(intent)
    }



    fun getLocationFromAddress(strAddress: String?): GeoPoint? {
        //gets GeoPoint Location from Address

        Log.v("!!!", "$strAddress")
        var lat = 0.0
        var long = 0.0

        val coder = Geocoder(this)
        val address: List<Address>?
        var p1: GeoPoint? = null
        try {
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null) {
                return null
            }
            val location: Address = address[0]
            lat = location.getLatitude()
            long = location.getLongitude()
            Log.v("!!!", "latitude $lat")
            Log.v("!!!", "longitude $long")
            p1 = GeoPoint(lat, long)
            return p1
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null

    }

}