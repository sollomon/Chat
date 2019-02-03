package com.example.sollo.chat

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.apollographql.apollo.ApolloClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_logout.*
import okhttp3.*
import java.io.IOException

class LoginActivity:AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_logout)


        loginButton.setOnClickListener {
            val email = emailEditTextLogin.text.toString()
            val password = passwordEditTextLogin.text.toString()

            Log.d("Login Activity", "Trying to Login: $email")
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    if(!it.isSuccessful) return@addOnCompleteListener
                    Log.d("Login", "Login Successfull")
                    Toast.makeText(this,"Login Successfull", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    Log.d("Login", "Login Failed")
                    Toast.makeText(this,"Failed to login ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }


        backtoRegisterButton.setOnClickListener {
            finish()
        }

        skipTextView.setOnClickListener {
            Log.d("Login Activity", "Skipping to Home")
            val intent  = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    fun fetchJson(){
        println("Fetching")
        val url = "http://192.168.43.233"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback{
            override fun onResponse(call: Call, response: Response) {
                val body = response?.body()?.string()
                println(body)


            }

            override fun onFailure(call: Call, e: IOException) {
                println("failed to fetch")
            }
        })

    }
    fun apollofetch(){
        val url = "http://localhost:3001/graphql"
        val okHttpClient = OkHttpClient.Builder().build()
        val apolloClient = ApolloClient.builder().serverUrl(url).okHttpClient(okHttpClient).build()

    }
}

