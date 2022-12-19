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


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val adapter = RestaurantInfoAdapter(this)
        mMap.setInfoWindowAdapter(adapter)

        getUserData()
        createPlaces()
        //  Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    //add a place from adress:
    //https://stackoverflow.com/questions/24352192/android-google-maps-add-marker-by-address

    fun createPlaces(){

        Log.v("!!!", "new place")

        for (restaurant in listOfRestaurants) {

            //val marker = mMap.addMarker(MarkerOptions().position(restaurant.position.latitude, restaurant.position.longitude))
          /*  val marker = restaurant.position?.let { MarkerOptions().position(restaurant.position) }
                ?.let { mMap.addMarker(it) }
            marker?.tag = restaurant
            mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(59.1, 18.0)))*/
            Log.v("!!!", "new place")
        }

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
                }
                .addOnFailureListener { exception ->
                    Log.d("!!!", "get failed with ", exception)
                }
        }
    }


}