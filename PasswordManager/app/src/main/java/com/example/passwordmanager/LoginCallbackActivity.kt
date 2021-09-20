package com.example.passwordmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.sawolabs.androidsdk.LOGIN_SUCCESS_MESSAGE
import io.paperdb.Paper
import org.json.JSONObject

class LoginCallbackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_callback)

        val message = intent.getStringExtra(LOGIN_SUCCESS_MESSAGE)

        lateinit var user_data:JSONObject

        try {
            user_data = JSONObject(message)
        }catch (e:Exception){
            Toast.makeText(this, "Some Error Occurred! Please try again.", Toast.LENGTH_LONG).show()
            val i:Intent = Intent(this, MainActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }

        Paper.init(this)
        val user_id:String = user_data.getString("user_id")
        Paper.book().write("user_id", user_id)
        Toast.makeText(this, "You have been logged In!", Toast.LENGTH_LONG).show()
        val i:Intent = Intent(this, DashboardActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)

    }
}