package com.example.letseat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ShowAllRestaurantsActivity : AppCompatActivity() {

    //activity to display all restaurants and also
    // make the restaurants clickable to "single view restaurants"
    // where they can go to the maps-activity if they want
    private lateinit var mAddFab: FloatingActionButton

    private lateinit var recyclerView: RecyclerView

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private var listOfRestaurants = mutableListOf<Restaurant>()
    private lateinit var adapter: RestaurantsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurants)


        db = Firebase.firestore
        auth = Firebase.auth

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = RestaurantsRecyclerAdapter(this, listOfRestaurants)
        recyclerView.adapter = adapter

        getRestaurantData()

        mAddFab = findViewById(R.id.fab)


        mAddFab.setOnClickListener {
            val intent = Intent(this, AddRestaurantsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getRestaurantData() {

            db.collection("restaurants")
                .addSnapshotListener { snapshot, e ->
                    listOfRestaurants.clear()
                    if (snapshot != null) {
                        val restArray = mutableListOf<Restaurant>()
                        for (document in snapshot.documents) {
                            val restaurantDoc = document.toObject<Restaurant>()
                            if (restaurantDoc != null) {
                                restArray.add(restaurantDoc)
                            }
                        }
                        listOfRestaurants.addAll(restArray)
                        adapter.notifyDataSetChanged()
                    }

        }
    }
}
