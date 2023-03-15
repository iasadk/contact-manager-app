package com.example.humin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.humin.databinding.ActivityDashboardBinding
import com.example.humin.databinding.ActivityUserProfileBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserProfile : AppCompatActivity() {
    private lateinit var binding : ActivityUserProfileBinding;
    private lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val loginUser = intent.getStringExtra(DashboardActivity.userKey)
        if (loginUser != null) {
            setUserDataFields(loginUser)
        }

        binding.saveProfile.setOnClickListener {
            val username = binding.usernameEditTxt.text.toString()
            val email = binding.emailEditTxt.text.toString()
            val phone = binding.phoneEditTxt.text.toString()
            val password = binding.passwordEditTxt.text.toString()
            if (loginUser != null) {
                updateProfile(User(username,email,phone,password),loginUser)
            }
        }
    }

    private fun updateProfile(user : User,loginUser: String){
        database = FirebaseDatabase.getInstance("https://humin-23201-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(loginUser)

        database.setValue(user).addOnSuccessListener {
            Toast.makeText(this,"Profile Updated Successfully",Toast.LENGTH_SHORT).show()
            setUserDataFields(loginUser);

        }
            .addOnFailureListener {
                Toast.makeText(this,"Failed to Load User Data $it",Toast.LENGTH_SHORT).show()

            }
    }

    private fun setUserDataFields(loginUser: String){
        val username = binding.usernameEditTxt
        val email = binding.emailEditTxt
        val phone = binding.phoneEditTxt
        val password = binding.passwordEditTxt

        database = FirebaseDatabase.getInstance("https://humin-23201-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(loginUser)

        database.get().addOnSuccessListener {
            if(it.exists()){
                username.setText(it.child("username").value.toString())
                email.setText(it.child("email").value.toString())
                phone.setText(it.child("phone").value.toString())
                password.setText(it.child("password").value.toString())
            }
            else{
                Toast.makeText(this,"Failed to Load User Data",Toast.LENGTH_SHORT).show()
            }
        }
            .addOnFailureListener {
                Toast.makeText(this,"Failed to Load User Data $it",Toast.LENGTH_SHORT).show()

            }
    }
}