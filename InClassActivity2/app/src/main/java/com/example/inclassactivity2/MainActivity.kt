package com.example.inclassactivity2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var toastButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toastButton = findViewById(R.id.magic_button)

        toastButton.setOnClickListener { _ : View ->
            makeToast()
        }


    }

    private fun makeToast() {
        Toast.makeText(this, "Mr. Strimpleton", Toast.LENGTH_LONG).show()
    }
}