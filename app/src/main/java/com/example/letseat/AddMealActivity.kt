package com.example.letseat

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.example.letseat.databinding.ActivityAddMealBinding
import java.text.SimpleDateFormat
import java.util.*

class AddMealActivity : AppCompatActivity() {

    lateinit var db: FirebaseFirestore

    lateinit var mealView: EditText

    lateinit var binding: ActivityAddMealBinding
    lateinit var imageUri: Uri

    lateinit var fileName: String
    lateinit var imageFileName: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Firebase.firestore

        mealView = findViewById(R.id.EditMealNameView)

        val restaurantID = intent.getStringExtra("RestaurantID")

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

        binding.selectImageBtn.setOnClickListener {
            selectImage()
        }

        binding.uploadImageBtn.setOnClickListener {
            uploadImage()
        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }

    private fun uploadImage() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading file...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
        val now = Date()
        fileName = formatter.format(now)
        imageFileName = fileName
        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

        storageReference.putFile(imageUri)
            .addOnSuccessListener {

                Toast.makeText(this@AddMealActivity, "Successfully uploaded", Toast.LENGTH_SHORT)
                    .show()
                if (progressDialog.isShowing) progressDialog.dismiss()


            }.addOnFailureListener {
                if (progressDialog.isShowing) progressDialog.dismiss()
                Toast.makeText(this@AddMealActivity, "Failed upload", Toast.LENGTH_SHORT).show()

            }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {

            imageUri = data?.data!!
            binding.imageView2.setImageURI(imageUri)

        }
    }

    fun saveItem(restaurantID: String) {
        //function to add a new meal to the database
        val itemName = mealView.text.toString()

        mealView.setText("")

        val item =
            Meal(mealName = itemName, image = imageUri.toString(), restaurantID = restaurantID, glideImageUrl = "images/${imageFileName}")

        //adds location to database
        db.collection("meals").add(item)
        finish()
    }


    fun goToRestaurantsActivity() {
        val intent = Intent(this, ShowAllRestaurantsActivity::class.java)
        startActivity(intent)
    }
}