package com.example.letseat

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
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

        //send address to function to get the adress
        val latLng = getLocationFromAddress(itemAddress)

        Log.v("!!!", "$latLng")

      //  val item = Restaurant(itemName, itemAddress)
        val item = Restaurant(restaurantName = itemName, address = itemAddress, position = latLng)

        db.collection("users").document(user.uid)
            .collection("restaurants").add(item)

    }


    fun goToMapsActivity(){
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    fun goToRestaurantsActivity(){
        val intent = Intent(this, ShowAllRestaurantsActivity::class.java)
        startActivity(intent)
    }





    fun getLocationFromAddress(strAddress: String?): LatLng? {

        //i don't really know how to get this to work but :) hey

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
            Log.v("!!!", "$lat")
            Log.v("!!!", "$long")
            p1 = GeoPoint(
                (location.getLatitude() * 1E6) as Double,
                (location.getLongitude() * 1E6) as Double
            )
           // return p1????
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null

    }

}