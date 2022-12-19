package com.example.letseat

import android.content.Intent
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
import com.google.android.gms.maps.model.Marker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var auth : FirebaseAuth
    lateinit var db : FirebaseFirestore

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

        val restaurantNumber = intent.getIntExtra("documentID", 0)


        Log.v("!!!", restaurantNumber.toString())


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val adapter = RestaurantInfoAdapter(this)
        mMap.setInfoWindowAdapter(adapter)

        getUserData()

    }

    fun startPlaceFromIntent(){

        val intentPlace = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(intentPlace).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(intentPlace))

    }



    fun createPlaces(){

        Log.v("!!!", "new place")

        Log.v("!!!", "${listOfRestaurants.size}")

        val boundsBuilder = LatLngBounds.builder()

        for (restaurant in listOfRestaurants) {


            val latitude = restaurant.position?.latitude?.toDouble()
            val longitude = restaurant.position?.longitude?.toDouble()

            //val marker = mMap.addMarker(MarkerOptions().position(restaurant.position.latitude, restaurant.position.longitude))

            val location = latitude?.let { longitude?.let { it1 -> LatLng(it,it1) } }
            if (location!= null){
                boundsBuilder.include(location)
            }
            val marker = location?.let { MarkerOptions().position(it) }?.let { mMap.addMarker(it) }
            marker?.tag = restaurant


            Log.v("!!!", "restaurant")
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 1000, 1000, 0))
    }

    fun getUserData(){
        val docRef = auth.currentUser?.let {
            db.collection("users")
                .document(it.uid)
                .collection("restaurants")
                .get()
                .addOnSuccessListener { documents ->
                    val restArray = mutableListOf<Restaurant>()
                    for (document in documents){
                        val restaurantDoc = document.toObject(Restaurant::class.java)
                        restArray.add(restaurantDoc)
                    }
                    listOfRestaurants.addAll(restArray)
                    Log.v("!!!", "${listOfRestaurants.size}")
                    createPlaces()
                }
                .addOnFailureListener { exception ->
                    Log.d("!!!", "get failed with ", exception)
                }
        }
    }


}