package com.example.sollo.chat

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        registerButton.setOnClickListener {
            performRegister()
        }

        alreadyHaveAccountTextView.setOnClickListener {
            Log.d("RegisterActivity","Show login Screen")

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        photoButton.setOnClickListener {
            Log.d("RegisterActivity","Create Image profile")

            val intent  = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    var selectedPhotoUri: Uri? =null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            //see what selected image was..
            Log.d("RegisterActivity", "Image Selected")

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            selectImageViewRegister.setImageBitmap(bitmap)
            photoButton.alpha = 0f

  //          val bitmapDrawable = BitmapDrawable(bitmap)
  //          photoButton.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun performRegister(){

        val email = emailEditTextRegister.text.toString()
        val userName = usernameEditTextRegister.text.toString()
        val password = passwordEditTextRegiser.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Enter Email and Password", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("RegisterActivity","Email is: "+ email)
        Log.d("RegisterActivity", "Password : $password")

        //Firebase Authentication
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){task ->
                if(!task.isSuccessful) return@addOnCompleteListener

                Log.d("Register", "Successfully ${task.result} ")
                uploadImageToFirebaseStorage()
                Toast.makeText(this,"User Created Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Log.d("Register","Failed to create user ${it.message}")
                Toast.makeText(this,"Failed to create user ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageToFirebaseStorage(){
        if(selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/photos/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("Register", "Successfully uploaded photo: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    it.toString()
                    Log.d("Register", "File Location: $it")
                    saveUserToFirebaseDtabase(it.toString())
                }
            }
            .addOnFailureListener{
                Log.d("Register","Failed to putFile")
            }
    }

    private fun saveUserToFirebaseDtabase(profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, usernameEditTextRegister.text.toString(), profileImageUrl)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Register", "User saved to Firebase database")
            }
            .addOnFailureListener{
                Log.d("Register", "Failed to save user to firebase database ${it.message}")
            }
    }
}

class User(val uid:String, val username:String,val profileImageUrl:String)