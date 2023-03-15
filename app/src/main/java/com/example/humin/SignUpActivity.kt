package com.example.humin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.humin.databinding.ActivitySignUpBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import kotlin.math.log

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpBtn.setOnClickListener {
//            Get Data
            val username = binding.usernameEditTxt.text.toString()
            val email = binding.emailEditTxt.text.toString()
            val phone = binding.phoneEditTxt.text.toString()
            val password = binding.passwordEditTxt.text.toString()
//            Validate Data
//
            if(username.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty() && password.isNotEmpty()){
//                Check if username is unique or not
                val user = User(username,email,phone,password)
                checkUser(username,user)
            }else{
                Toast.makeText(this,"Please fill all the information",Toast.LENGTH_LONG).show()

            }
        }

        binding.loginTxt.setOnClickListener {
            redirectToLogin()
        }
    }

    private fun checkUser(username : String,userData: User){
        database = FirebaseDatabase.getInstance("https://humin-23201-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
        var isExist = false
        database.child(username).get().addOnSuccessListener  {
//            Check if user exist
//            Log.i("Error: ","isUserExist............${it.exists()}")
//            Log.i("Error: ","snapShot............${it}")
            if(!it.exists()){
                addUserToDB(username,userData);
            }else{
                Toast.makeText(this,"Username is taken or please try to login",Toast.LENGTH_LONG).show()

            }

        }
            .addOnFailureListener {
                Toast.makeText(this,"Server Not Responding 400",Toast.LENGTH_SHORT).show()
            }

    }

    private fun addUserToDB(username: String,userData: User){
        database = FirebaseDatabase.getInstance("https://humin-23201-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
        database.child(username).setValue(userData).addOnSuccessListener {
            Toast.makeText(this,"User Registered Successfully. Redirecting to login screen.",Toast.LENGTH_SHORT).show();
            binding.usernameEditTxt.setText("")
            binding.emailEditTxt.setText("")
            binding.phoneEditTxt.setText("")
            binding.passwordEditTxt.setText("")
            redirectToLogin()

        }.addOnFailureListener {
            Toast.makeText(this,"Failed to Save data ${it.message}",Toast.LENGTH_SHORT).show()
        }
    }

    private fun redirectToLogin(){
        intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }
}