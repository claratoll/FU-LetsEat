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

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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



        createPlaces()
        // Add a marker in Sydney and move the camera
       // val sydney = LatLng(-34.0, 151.0)
        //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    //add a place from adress:
    //https://stackoverflow.com/questions/24352192/android-google-maps-add-marker-by-address

    fun createPlaces(){

        val p1 = Restaurant("Hemma", true, 7, LatLng(59.1, 18.0))
        val p2 = Restaurant("Jobb", false,9, LatLng(58.1, 17.0),)


        val placeList = listOf<Restaurant>(p1,p2)

        for (place in placeList) {
            val marker = place.position?.let { MarkerOptions().position(it) }
                ?.let { mMap.addMarker(it) }
            marker?.tag = place
            mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(59.1, 18.0)))
            Log.v("!!!", "new place")
        }

    }
}