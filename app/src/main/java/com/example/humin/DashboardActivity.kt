package com.example.humin

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.humin.databinding.ActivityDashboardBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Objects

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDashboardBinding;
    private lateinit var database : DatabaseReference
    companion object {
        val userKey = "userKey"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val usernameColor = ContextCompat.getColor(this, R.color.btnColorDark);
        setContentView(binding.root)
        var username = intent.getStringExtra(LoginActivity.userKey)
        val logginUser = username;
        val dashboardTitleTxtView = binding.dashHeading;

        username = if (username?.length!! > 10) username.substring(0,10) + "..." else username

        val dashBoardTitle = "Hola, Welcome\n$username"
        val heading = SpannableString(dashBoardTitle);
        heading.setSpan(ForegroundColorSpan(usernameColor),13,(13 + username?.length!!+1), Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
        heading.setSpan(ForegroundColorSpan(Color.BLACK),0,13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        dashboardTitleTxtView.text = heading

//        Code to Add new Contact to contact collection in firebase

        binding.addContactBtn.setOnClickListener {
            val contactName : String = binding.contactUsernameEditTxt.text.toString();
            val contactEmail : String = binding.contactEmailEditTxt.text.toString();
            val contactPhone : String = binding.contactPhoneEditTxt.text.toString();

            if(contactName.isEmpty()){
                Toast.makeText(this,"Contact Name is Required", Toast.LENGTH_SHORT).show();
            }
            if(contactPhone.isEmpty()){
                Toast.makeText(this,"Phone Number is required to save contact", Toast.LENGTH_SHORT).show();
            }
            if(contactPhone.isNotEmpty() && contactName.isNotEmpty()){
                val dataToSave = hashMapOf<String,String>()
                dataToSave["name"] = contactName
                dataToSave["email"] = contactEmail
                dataToSave["phone"] = contactPhone
                intent.getStringExtra(LoginActivity.userKey)?.let { name -> addUserToDB(name, dataToSave) }
            }


        }

        binding.listAllContacts.setOnClickListener {
            intent = Intent(this,ContactList::class.java)
            intent.putExtra(userKey,logginUser);
            startActivity(intent)
        }

        binding.profileTxtView.setOnClickListener {
            intent = Intent(this,UserProfile::class.java)
            intent.putExtra(userKey,logginUser);
            startActivity(intent)

        }

    }

    private fun addUserToDB(username: String,contact:HashMap<String,String>){
        database = FirebaseDatabase.getInstance("https://humin-23201-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Contacts")
        val newData = database.child(username).push()

        newData.setValue(contact).addOnSuccessListener {
            Toast.makeText(this,"Contact Added Successfully.", Toast.LENGTH_SHORT).show();
            binding.contactUsernameEditTxt.setText("")
            binding.contactEmailEditTxt.setText("")
            binding.contactPhoneEditTxt.setText("")

        }.addOnFailureListener {
            Toast.makeText(this,"Failed to Save data $it", Toast.LENGTH_SHORT).show()
        }
    }
}