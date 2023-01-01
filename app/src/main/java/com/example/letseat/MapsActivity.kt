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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

abstract class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private var listOfRestaurants = mutableListOf<Restaurant>()
   // private var restaurant: Restaurant? = null

    private lateinit var restaurantId: String




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = Firebase.firestore
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


        mMap.setOnInfoWindowClickListener {
            val intent = Intent(this, DisplayOneRestaurantActivity::class.java)
            Log.v("!!!", restaurantId)
            intent.putExtra("documentID", restaurantId)
            this.startActivity(intent)
        }
    }



    fun fetchDataFromServer() {

        /*    db.collection("restaurants").document(restaurantId).addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.v("!!!", "Listen failed", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("!!!", "Current data: ${snapshot.data}")
                    val restaurantDoc = snapshot.toObject<Restaurant>()
                   // restaurant = restaurantDoc
                    createPlaces()
                }else {
                    Log.d("!!!", "Current data: null")
                }
                }
*/
        db.collection("restaurants")
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
        val restaurantNumber = intent.getStringExtra("documentID")
        if (restaurantNumber != null) {
            Log.v("!!!", "restaurant number is $restaurantNumber")
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    boundsBuilder.build(),
                    1000,
                    1000,
                    0
                )
            )
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
}