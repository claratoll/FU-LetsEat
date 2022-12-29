package com.example.letseat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
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
    private lateinit var editPoints: EditText
    private lateinit var saveButton: Button
    private lateinit var addButton: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private val restaurantList = mutableListOf<Restaurant>()
    private val mealList = mutableListOf<Meal>()


    // on below line we are creating variable for view pager,
    // viewpager adapter and the image list.
    lateinit var viewPager: ViewPager
    lateinit var viewPagerAdapter: ViewPagerAdapter




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_one_restaurant)

        db = Firebase.firestore
        auth = Firebase.auth

        nameView = findViewById(R.id.ResNameTextView)
        addressView = findViewById(R.id.AddressNameView)
        pointsView = findViewById(R.id.ResPointsTextView)
        editPoints = findViewById(R.id.editPointsView)
        saveButton = findViewById(R.id.savePointsButton)
        addButton = findViewById(R.id.AddMealButton)


        var restaurantId = 1

        restaurantId = intent.getIntExtra("documentID", 999)

        getUserData(restaurantId)

        // initializing variables
        // of below line with their id.
        viewPager = findViewById(R.id.idViewPager)

        // on below line we are initializing
        // our image list and adding data to it.
     //   mealList = ArrayList<Meal>()


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

        saveButton.setOnClickListener {
            savePoints()
        }

     //   val restaurant = restaurantList[restaurantId]

        addButton.setOnClickListener {
            val intent = Intent(this, AddMealActivity::class.java)
            Log.v("!!!", restaurantList.get(restaurantId).restaurantName.toString())
            intent.putExtra("RestaurantID", restaurantList.get(restaurantId).restaurantName)
            this.startActivity(intent)
        }

    }


    private fun savePoints(){
        val points = editPoints.text.toString().toInt()

        if (points < 1 || points > 10){
            Log.v("!!!", "wrong input")

        } else {
            Log.v("!!!", "correct input $points")
        }

    }

    private fun getMealData(restaurantId: Int){
        val docRef = auth.currentUser?.let {
            db.collection("meals")
                .addSnapshotListener { snapshot, e ->
                    mealList.clear()
                    Log.v("!!!", "tjena")
                    if (snapshot != null) {
                        val mealArray = mutableListOf<Meal>()
                        for (document in snapshot.documents) {
                            val mealDoc = document.toObject<Meal>()
                            if (mealDoc != null) {
                                Log.v("!!!", "tjena")
                                //mealArray.add(mealDoc)
                            } else {
                                viewPager.visibility = View.GONE
                                Log.v("!!!", "helo")
                            }
                        }
                        mealList.addAll(mealArray)
                        placeName(restaurantId)
                    }
                }

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