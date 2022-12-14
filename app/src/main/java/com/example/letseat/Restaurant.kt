package com.example.letseat

import com.google.android.gms.maps.model.LatLng

data class Restaurant(var restaurantName: String? = null, var visited: Boolean = false, var points: Int = 0, val position: LatLng?= LatLng(59.1, 18.0))
