package com.example.passwordmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.passwordmanager.Utils.progressBarUtils
import com.example.passwordmanager.databinding.ActivityAddPasswordBinding
import com.example.passwordmanager.databinding.ActivityDashboardBinding
import com.example.passwordmanager.models.password
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import io.paperdb.Paper

class AddPasswordActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddPasswordBinding
    lateinit var database: FirebaseDatabase
    lateinit var user_id:String
    var passwords = arrayListOf<password>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Paper.init(this)
        val x:String? = Paper.book().read("user_id", null)

        if(x == null){
            val i: Intent = Intent(this, MainActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }else{
            user_id = x
        }

        binding.btnAddPassword.setOnClickListener{
            val service_txt:String = binding.etService.text.toString()
            val email_txt:String = binding.etEmail.text.toString()
            val password_txt:String = binding.etPassword.text.toString()

            if(service_txt.isEmpty() || email_txt.isEmpty() || password_txt.isEmpty()){
                Toast.makeText(this, "Please Enter all details", Toast.LENGTH_LONG).show()
            }else{
                val curr:password = password(service_txt, email_txt, password_txt)
                progressBarUtils.showProgress(this, this)
                addData(curr)
            }

        }

    }

    private fun addData(curr: password) {
        database = Firebase.database
        val myRef = database.getReference("psm").child(user_id)

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    passwords.add(postSnapshot.getValue<password>() as password)
                }

                passwords.add(curr)
                myRef.setValue(passwords).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this@AddPasswordActivity, "Password has been added sucessfully!", Toast.LENGTH_LONG).show()
                        val i: Intent = Intent(this@AddPasswordActivity, DashboardActivity::class.java)
                        progressBarUtils.hideProgress()
                        startActivity(i)
                        finish()
                    }else{
                        progressBarUtils.hideProgress()
                        Toast.makeText(this@AddPasswordActivity, "Some error occurred! Please try again!", Toast.LENGTH_LONG).show()
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DashboardActivity:failure", "loadPost:onCancelled", databaseError.toException())
            }
        }

        myRef.addListenerForSingleValueEvent(postListener)

    }
}