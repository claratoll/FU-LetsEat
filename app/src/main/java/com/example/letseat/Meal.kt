package com.example.letseat

import android.net.Uri
import android.widget.ImageView

data class Meal(
    var restaurantID: String? = null,
    var mealName: String? = null,
    var image: String? = null,
var glideImageUrl: String = ""
)