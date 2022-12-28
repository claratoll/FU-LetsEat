package com.example.letseat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class DisplayOneRestaurantActivity : AppCompatActivity() {

    private lateinit var nameView: TextView
    private lateinit var addressView: TextView
    private lateinit var pointsView: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private val restaurantList = mutableListOf<Restaurant>()


    // on below line we are creating variable for view pager,
    // viewpager adapter and the image list.
    lateinit var viewPager: ViewPager
    lateinit var viewPagerAdapter: ViewPagerAdapter
    lateinit var mealList: List<Meal>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_one_restaurant)

        db = Firebase.firestore
        auth = Firebase.auth

        nameView = findViewById(R.id.ResNameTextView)
        addressView = findViewById(R.id.AddressNameView)
        pointsView = findViewById(R.id.ResPointsTextView)


        val restaurantId = intent.getIntExtra("documentID", 999)

        getUserData(restaurantId)

        // initializing variables
        // of below line with their id.
        viewPager = findViewById(R.id.idViewPager)

        // on below line we are initializing
        // our image list and adding data to it.
        mealList = ArrayList<Meal>()


      /*  imageList = imageList + R.drawable.firstimage
        imageList = imageList + R.drawable.secondimage
        imageList = imageList + R.drawable.thirdimage*/

        // on below line we are initializing our view
        // pager adapter and adding image list to it.
        viewPagerAdapter = ViewPagerAdapter(this, mealList)

        // on below line we are setting
        // adapter to our view pager.
        viewPager.adapter = viewPagerAdapter


        addressView.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("documentID", restaurantId)
            this.startActivity(intent)
        }
    }


    private fun getUserData(restaurantId: Int) {

        val docRef = auth.currentUser?.let {
            db.collection("restaurants")
                .addSnapshotListener { snapshot, e ->
                    restaurantList.clear()
                    if (snapshot != null) {
                        val restArray = mutableListOf<Restaurant>()
                        for (document in snapshot.documents) {
                            val restaurantDoc = document.toObject<Restaurant>()
                            if (restaurantDoc != null) {
                                restArray.add(restaurantDoc)
                            }
                        }
                        restaurantList.addAll(restArray)
                        placeName(restaurantId)
                    }
                }

        }
    }

    fun placeName(restaurantId: Int) {

        val restaurant = restaurantList[restaurantId]

        nameView.text = restaurant.restaurantName.toString()
        addressView.text = restaurant.address.toString()
        pointsView.text = "The restaurant has ${restaurant.points}"

    }
}