package com.example.letseat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
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

    private lateinit var db: FirebaseFirestore

    private var restaurant: Restaurant? = null

    private val mealList = mutableListOf<Meal>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter : MealRecyclerAdapter

    private var restaurantId = ""

    private var points: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_one_restaurant)

        db = Firebase.firestore

        nameView = findViewById(R.id.ResNameTextView)
        addressView = findViewById(R.id.AddressNameView)
        pointsView = findViewById(R.id.ResPointsTextView)
        editPoints = findViewById(R.id.editPointsView)
        saveButton = findViewById(R.id.savePointsButton)
        addButton = findViewById(R.id.AddMealButton)

        recyclerView = findViewById(R.id.mealrecyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = MealRecyclerAdapter(this, mealList)
        recyclerView.adapter = adapter

        restaurantId = intent.getStringExtra("documentID")!!

        getUserData()
        getMealData()


        addressView.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("documentID", restaurantId)
            this.startActivity(intent)
        }

        saveButton.setOnClickListener {
            savePoints()
        }


        addButton.setOnClickListener {
            val intent = Intent(this, AddMealActivity::class.java)
            intent.putExtra("RestaurantID", restaurantId)
            this.startActivity(intent)
        }
    }

    fun placeName(){
        nameView.text = restaurant!!.restaurantName.toString()
        addressView.text = restaurant!!.address.toString()
        pointsView.text = "The restaurant has ${restaurant!!.points}"
    }


    private fun savePoints(){

        val inputPoint = editPoints.text.toString().toInt()


        if (editPoints.toString() != "") {

            Log.v("!!!", "correct input")
            editPoints.setText("")

        }  else {
            Log.v("!!!", "wrong input")
            editPoints.setText("")

        }

        if (inputPoint <1 || inputPoint> 10) {
            Log.v("!!!", "wrong input")
            editPoints.setText("")
        }  else {
            Log.v("!!!", "correct input $points")
            points = inputPoint
            db.collection("restaurants").document(restaurantId)
                .update(mapOf(
                    "points" to points
                ))

        }
    }

    private fun getMealData(){
            db.collection("meals")
                .addSnapshotListener { snapshot, e ->
                    mealList.clear()
                    if (snapshot != null) {
                        val mealArray = mutableListOf<Meal>()
                        for (document in snapshot.documents) {
                            val mealDoc = document.toObject<Meal>()
                            if (mealDoc != null) {
                                Log.v("!!!", "meal: ${mealDoc.restaurantID}")
                                if (mealDoc.restaurantID.equals(restaurantId)){
                                mealArray.add(mealDoc)
                                }
                            } else {

                                Log.v("!!!", "no images")
                            }
                        }
                        mealList.addAll(mealArray)
                        adapter.notifyDataSetChanged()

                    }
        }
    }

    private fun getUserData() {

        db.collection("restaurants").document(restaurantId).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("!!!", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d("!!!", "Current data: ${snapshot.data}")
                val restaurantDoc = snapshot.toObject<Restaurant>()
                restaurant = restaurantDoc
                placeName()
            } else {
                Log.d("!!!", "Current data: null")
            }
        }
    }
}