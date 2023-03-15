package com.example.humin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.humin.databinding.ActivityContactListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.EventListener

class ContactList : AppCompatActivity() {
    private lateinit var binding : ActivityContactListBinding
    private lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val txtView = binding.list
        val username = intent.getStringExtra(DashboardActivity.userKey)

        database = username?.let {
            FirebaseDatabase.getInstance("https://humin-23201-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Contacts").child(it)
        }!!

        database.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot){
                var s = "";
                dataSnapshot.children.forEach {
                    val name = it.child("name").value.toString()
                    val email = it.child("email").value.toString()
                    val phone = it.child("phone").value.toString()

                    s+= "\n name: $name\n" +
                            "email: $email\n" +
                            "phone: $phone\n"

                    txtView.text = s;
                }


            }
            override fun onCancelled(error: DatabaseError){
                //Failed to read value
                Log.e("TAG","Failed to read user",error.toException())
            }
        })

    }
}