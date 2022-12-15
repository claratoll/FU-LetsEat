package com.example.letseat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth
    lateinit var emailView : EditText
    lateinit var passwordView : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //login page
        auth = Firebase.auth

        emailView = findViewById(R.id.EmailEditText)
        passwordView = findViewById(R.id.PasswordEditText)

        val signUpButton = findViewById<Button>(R.id.createuserbutton)
        signUpButton.setOnClickListener {
            signUp()
        }

        val signInButton = findViewById<Button>(R.id.loginbutton)
        signInButton.setOnClickListener {
            signIn()
        }

        if (auth.currentUser != null){
            goToAddActivity()
        }
    }

    fun goToAddActivity(){
        val intent = Intent(this, AddRestaurantsActivity::class.java)
        startActivity(intent)
    }



    fun signIn() {
        val email = emailView.text.toString()
        val password = passwordView.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            return
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
            if (task.isSuccessful){
                Log.d("!!!", "login success")
                goToAddActivity()

            } else {
                Log.d("!!!", "user not created ${task.exception}")
            }

        }
    }


    fun signUp(){
        val email = emailView.text.toString()
        val password = passwordView.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            return
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
            if (task.isSuccessful){
                Log.d("!!!", "create success")
                goToAddActivity()
            } else {
                Log.d("!!!", "user not created ${task.exception}")
            }
        }
    }

}