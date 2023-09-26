package com.example.robotpurchaseactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var rewardButtonA : Button;
    private lateinit var rewardButtonB : Button;
    private lateinit var rewardButtonC : Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        rewardButtonA = findViewById(R.id.rewardButtonB);
        rewardButtonB = findViewById(R.id.rewardButtonB);
        rewardButtonC = findViewById(R.id.rewardButtonC);
    }
}


// Toast.makeText(this, "message to display", TOAST_LENGTH_LONG/SHORT).showc)