package com.example.letseat

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class RestaurantInfoAdapter(val context: Context) : GoogleMap.InfoWindowAdapter {

    val layoutInflater = LayoutInflater.from(context)



    override fun getInfoContents(p0: Marker): View? {
        return null
    }


    override fun getInfoWindow(marker: Marker): View? {

        val infoWindow = layoutInflater.inflate(R.layout.restaurant_item, null)

        val titleView = infoWindow.findViewById<TextView>(R.id.RestaurantNameView)
        val infoView = infoWindow.findViewById<TextView>(R.id.infoTextView)
        val pointsView = infoWindow.findViewById<TextView>(R.id.pointsDisplayTextView)

        val restaurant = marker.tag as? Restaurant

        titleView.text = restaurant?.restaurantName
        infoView.text = restaurant?.address
        pointsView.text = "The restaurant has ${restaurant?.points.toString()} points"

        return infoWindow
    }


}