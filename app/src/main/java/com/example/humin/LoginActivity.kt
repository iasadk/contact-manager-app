package com.example.humin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.humin.databinding.ActivityLoginBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var database : DatabaseReference
    companion object {
        val userKey = "userKey"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            val username = binding.usernameEditTxt.text.toString();
            val password = binding.passwordEditTxt.text.toString();
//          match the value with DB value
            verfitUserData(username,password)
        }
        binding.signUpTxt.setOnClickListener {
            redirectToSignUp()
        }
    }

    private fun verfitUserData(username : String, password : String){
        database = FirebaseDatabase.getInstance("https://humin-23201-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
        database.child(username).get().addOnSuccessListener {
            if(it.exists() && it.child("password").value.toString() == password){
                // if value match redirect to dashboard
                redirectToDasboard(username)
            }else{
                Toast.makeText(this,"Username or Password is Invalid", Toast.LENGTH_LONG).show()

            }
        }.addOnFailureListener {
            Toast.makeText(this,"Server Not Responding 400 $it",Toast.LENGTH_SHORT).show()
        }
    }

    private fun redirectToDasboard(username: String){
        intent = Intent(this,DashboardActivity::class.java);
        intent.putExtra(userKey,username);
        finish();
        startActivity(intent);
    }
    private fun redirectToSignUp(){
        intent = Intent(this,SignUpActivity::class.java);
        finish();
        startActivity(intent);
    }
}