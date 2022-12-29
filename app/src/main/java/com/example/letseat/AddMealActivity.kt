package com.example.letseat

import android.app.ProgressDialog
import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.SimpleAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.example.letseat.databinding.ActivityAddMealBinding
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

class AddMealActivity : AppCompatActivity() {

    lateinit var db : FirebaseFirestore
    lateinit var auth : FirebaseAuth

    lateinit var mealView : EditText

    lateinit var binding : ActivityAddMealBinding
    lateinit var imageUri : Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = Firebase.firestore

        mealView = findViewById(R.id.EditMealNameView)

        val restaurantID = intent.getStringExtra("RestaurantID")

        Log.v("!!!", restaurantID.toString())

        val saveButton = findViewById<Button>(R.id.anotherSaveButton)
        saveButton.setOnClickListener {
            if (restaurantID != null) {
                saveItem(restaurantID)
            }
        }

        val restaurantsButton = findViewById<Button>(R.id.anotherRestaurantsButton)
        restaurantsButton.setOnClickListener {
            goToRestaurantsActivity()
        }

        binding.selectImageBtn.setOnClickListener{
            selectImage()
        }

        binding.uploadImageBtn.setOnClickListener{
            uploadImage()
        }
    }

    private fun selectImage(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }

    private fun uploadImage(){
       val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading file...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

        storageReference.putFile(imageUri)
            .addOnSuccessListener {

                binding.imageView2.setImageURI(null)
                Toast.makeText(this@AddMealActivity, "Successfully uploaded", Toast.LENGTH_SHORT).show()
                if (progressDialog.isShowing)progressDialog.dismiss()


            }.addOnFailureListener {
                if (progressDialog.isShowing) progressDialog.dismiss()
                Toast.makeText(this@AddMealActivity, "Failed upload", Toast.LENGTH_SHORT).show()

            }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK){

            imageUri = data?.data!!
            binding.imageView2.setImageURI(imageUri)


        }
    }

    fun saveItem(restaurantID : String){
        //function to add a new meal to the database

        val itemName = mealView.text.toString()

        mealView.setText("")

        val user = auth.currentUser
        if (user == null) {
            return
        }

        val item = Meal(mealName = itemName, restaurantID = restaurantID)

        //adds location to database
        db.collection("meals").add(item)
    }


    fun goToRestaurantsActivity(){
        val intent = Intent(this, ShowAllRestaurantsActivity::class.java)
        startActivity(intent)
    }
}