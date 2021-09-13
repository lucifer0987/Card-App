package com.example.passwordmanager

import android.app.ActivityManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.passwordmanager.Utils.progressBarUtils
import com.example.passwordmanager.databinding.ActivityDashboardBinding
import com.example.passwordmanager.models.password
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import io.paperdb.Paper
import java.util.ArrayList

class DashboardActivity : AppCompatActivity() {

    lateinit var binding:ActivityDashboardBinding
    lateinit var database:FirebaseDatabase
    lateinit var user_id:String
    var passwords = arrayListOf<password>()
    lateinit var adapter: PasswordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Paper.init(this)
        val x:String? = Paper.book().read("user_id", null)

        if(x == null){
            val i: Intent = Intent(this, MainActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }else{
            progressBarUtils.showProgress(this, this)
            user_id = x
            val layoutManager = LinearLayoutManager(this)
            binding.psRec.layoutManager = layoutManager
            binding.psRec.hasFixedSize()
            adapter = PasswordAdapter(this)
            binding.psRec.adapter = adapter
            loadData()
        }

        binding.btnLogout.setOnClickListener{
            Toast.makeText(this, "You have been logged out!", Toast.LENGTH_LONG).show()
            Paper.book().destroy()
            this.cacheDir.deleteRecursively()
            val i: Intent = Intent(this, MainActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }

        binding.addPassword.setOnClickListener{
            val i: Intent = Intent(this, AddPasswordActivity::class.java)
            startActivity(i)
        }

        binding.searchTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }
        })

    }

    private fun loadData() {
        database = Firebase.database
        val myRef = database.getReference("psm").child(user_id)

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    passwords.add(postSnapshot.getValue<password>() as password)
                }

                for(password in passwords){
                    Log.e("this1", password.toString())
                }

                adapter.setData(passwords)
                progressBarUtils.hideProgress()
                binding.psRec.visibility = View.VISIBLE
                binding.noPasswordTxt.visibility = View.GONE

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DashboardActivity:failure", "loadPost:onCancelled", databaseError.toException())
            }
        }

        myRef.addListenerForSingleValueEvent(postListener)
    }

    fun filter(text: String) {
        val temp = ArrayList<password>()
        for (d in passwords) {
            if (d.service_name?.lowercase()?.contains(text) == true) {
                temp.add(d)
            }
        }
        adapter.setData(temp)
    }

}