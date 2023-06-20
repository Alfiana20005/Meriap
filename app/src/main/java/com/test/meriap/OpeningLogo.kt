package com.test.meriap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class OpeningLogo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opening)

        supportActionBar?.hide()
    }
}