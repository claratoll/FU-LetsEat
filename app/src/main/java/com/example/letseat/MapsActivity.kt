package com.example.letseat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.letseat.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.LatLngBounds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private var listOfRestaurants = mutableListOf<Restaurant>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        db = Firebase.firestore

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val adapter = RestaurantInfoAdapter(this)
        mMap.setInfoWindowAdapter(adapter)


        fetchDataFromServer()

    }



    fun fetchDataFromServer() {
        val docRef = auth.currentUser?.let {
            db.collection("users")
                .document(it.uid)
                .collection("restaurants")
                .get()
                .addOnSuccessListener { documents ->
                    val restArray = mutableListOf<Restaurant>()
                    for (document in documents) {
                        val restaurantDoc = document.toObject(Restaurant::class.java)
                        restArray.add(restaurantDoc)
                    }
                    listOfRestaurants.addAll(restArray)
                    createPlaces()
                }
                .addOnFailureListener { exception ->
                    Log.d("!!!", "get failed with ", exception)
                }
        }
    }


    fun createPlaces() {

        val boundsBuilder = LatLngBounds.builder()

        for (restaurant in listOfRestaurants) {

            val latitude = restaurant.position?.latitude?.toDouble()
            val longitude = restaurant.position?.longitude?.toDouble()

            val location = latitude?.let { longitude?.let { it1 -> LatLng(it, it1) } }
            if (location != null) {
                boundsBuilder.include(location)
            }
            val marker = location?.let { MarkerOptions().position(it) }?.let { mMap.addMarker(it) }
            marker?.tag = restaurant

        }

        //if user has pressed on a specific restaurant
        val restaurantNumber = intent.getIntExtra("documentID", 999)
        if (restaurantNumber != 999) {
            startPlaceFromIntent(restaurantNumber)
            Log.v("!!!", "restaurant number is $restaurantNumber")
        } else {
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    boundsBuilder.build(),
                    1000,
                    1000,
                    0
                )
            )
        }
    }

    fun startPlaceFromIntent(restaurantNumber: Int) {


        val boundsBuilder = LatLngBounds.builder()

        val latitude = listOfRestaurants[restaurantNumber].position?.latitude?.toDouble()
        val longitude = listOfRestaurants[restaurantNumber].position?.longitude?.toDouble()

        Log.v("!!!", "restaurant latitude is $latitude")

        val location = latitude?.let { longitude?.let { it1 -> LatLng(it, it1) } }
        if (location != null) {
            boundsBuilder.include(location)
        }
        val marker = location?.let { MarkerOptions().position(it) }?.let { mMap.addMarker(it) }
        marker?.tag = listOfRestaurants[restaurantNumber]

        location?.let { CameraUpdateFactory.newLatLng(it) }?.let { mMap.moveCamera(it) }

    }

}