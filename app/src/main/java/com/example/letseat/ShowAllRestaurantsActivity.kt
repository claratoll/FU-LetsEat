package com.example.letseat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.firestore.auth.User

class ShowAllRestaurantsActivity : AppCompatActivity() {

    //activity to display all restaurants and also
    // make the restaurants clickable to "single view restaurants"
    // where they can go to the maps-activity if they want

    private lateinit var recyclerView: RecyclerView

    private lateinit var db : DatabaseReference

    private lateinit var userArrayList : ArrayList<Restaurant>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurants)


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = RestaurantsRecyclerAdapter(this, DataManager.restaurants)

        //recyclerView.adapter = RestaurantsRecyclerAdapter(this, )
        userArrayList = arrayListOf<Restaurant>()
        getUserData()

    }


    private fun getUserData(){
        db = FirebaseDatabase.getInstance().getReference("User")

        db.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                   for (userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(User::class.java)

                      // userArrayList.add(user!!)
                   }

                  //  recyclerView.adapter = RestaurantsRecyclerAdapter(this, userArrayList)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}