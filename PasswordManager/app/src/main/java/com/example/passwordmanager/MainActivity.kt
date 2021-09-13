package com.example.passwordmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.passwordmanager.databinding.ActivityMainBinding
import com.sawolabs.androidsdk.Sawo

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignIn.setOnClickListener{
            Sawo(
                this,
                "8dd2db0e-13f0-4a02-8cff-652debc41535", // your api key
                "6120f1432f5bde7b00073200JZD6ZarI3KUUczOL4ssZhipK"  // your api key secret
            ).login(
                "phone_number_sms", // can be one of 'email' or 'phone_number_sms'
                LoginCallbackActivity::class.java.name // Callback class name
            )
        }


    }
}