package com.example.letseat

import com.google.android.gms.maps.model.LatLng

object DataManager {

    val restaurants = mutableListOf<Restaurant>()

    init {
        createRestaurants()
    }

    fun createRestaurants(){
     //   restaurants.add(Restaurant("Pizzeria Elvan", false, 6, LatLng(59.1, 18.0)))
       // restaurants.add(Restaurant("Slottsrestaurangen", true, 4,  LatLng( 59.1, 18.0)))
    }
}