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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var db: FirebaseFirestore

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private var listOfRestaurants = mutableListOf<Restaurant>()

    private lateinit var restaurantId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        mMap.setOnInfoWindowClickListener { ClickOnRestaurant ->
            for (restaurant in listOfRestaurants){
                if (restaurant.restaurantName.equals(ClickOnRestaurant.title)){
                    restaurantId = restaurant.documentId
                    onInfoWindowClick(restaurantId)

                }
            }

        }
    }

    fun onInfoWindowClick(resId : String) {
        val intent = Intent(this, DisplayOneRestaurantActivity::class.java)
        Log.v("!!!", resId)
        intent.putExtra("documentID", resId)
        this.startActivity(intent)
    }


    fun fetchDataFromServer() {

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
            val marker =
                location?.let { MarkerOptions().position(it).title(restaurant.restaurantName) }?.let { mMap.addMarker(it) }!!
            marker.tag = restaurant

        }


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
