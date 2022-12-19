package com.example.letseat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ShowAllRestaurantsActivity : AppCompatActivity() {

    //activity to display all restaurants and also
    // make the restaurants clickable to "single view restaurants"
    // where they can go to the maps-activity if they want

    private lateinit var recyclerView: RecyclerView

    private lateinit var db : FirebaseFirestore
    lateinit var auth : FirebaseAuth

    private var listOfRestaurants = mutableListOf<Restaurant>()
    private lateinit var adapter : RestaurantsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurants)


        db = Firebase.firestore
        auth = Firebase.auth

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = RestaurantsRecyclerAdapter(this, listOfRestaurants)
        recyclerView.adapter = adapter

        //recyclerView.adapter = RestaurantsRecyclerAdapter(this, )
      //  userArrayList = arrayListOf<Restaurant>()
        Log.d("!!!", "before get user data")
        getUserData()
        Log.d("!!!", "after get user data")
    }


    private fun getUserData(){


       // val docRef = db.collection("users").document("restaurants")

        val docRef = auth.currentUser?.let {
            db.collection("users")
                .document(it.uid)
                .collection("restaurants")
                .get()
                .addOnSuccessListener { documents ->
                    val restArray = mutableListOf<Restaurant>()
                   for (document in documents){
                       val restaurantDoc = document.toObject(Restaurant::class.java)
                       Log.d("!!!", restaurantDoc.toString())
                       restArray.add(restaurantDoc)
                   }
                    listOfRestaurants.addAll(restArray)
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Log.d("!!!", "get failed with ", exception)
                }
        }

//recyclerView.adapter = RestaurantsRecyclerAdapter(this, DataManager.restaurants)




       /* db = FirebaseDatabase.getInstance().getReference("User")

        db.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                   for (userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(User::class.java)

                       Log.d("!!!", "Get user data")

                      // userArrayList.add(user!!)
                   }

                  //  recyclerView.adapter = RestaurantsRecyclerAdapter(this, userArrayList)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })*/

    }
}