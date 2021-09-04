package com.example.cardapp

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    lateinit var side_1:RelativeLayout
    lateinit var side_2:RelativeLayout
    lateinit var flip:ImageView
    lateinit var flip_2:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()

        Toast.makeText(this, "Please click on the flip icon to flip the card!", Toast.LENGTH_LONG).show()
        Toast.makeText(this, "No other button is working", Toast.LENGTH_SHORT).show()

        flip.setOnClickListener{
            side_1.visibility = View.GONE
            side_2.visibility = View.VISIBLE
        }

        flip_2.setOnClickListener{
            side_2.visibility = View.GONE
            side_1.visibility = View.VISIBLE
        }


    }

    private fun setupViews() {
        side_1 = findViewById(R.id.side_1)
        side_2 = findViewById(R.id.side_2)
        flip = findViewById(R.id.flip)
        flip_2 = findViewById(R.id.flip_2)
    }
}