package com.sevenlearn.nike.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.sevenlearn.nike.R
import com.sevenlearn.nike.feature.main.MainActivity

class SplashScreenActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val handler =Handler()
        val monitor :Runnable = object : Runnable {
            override fun run() {
                startActivity(Intent(this@SplashScreenActivity,MainActivity::class.java))
                finish()

            }

        }
        handler.postDelayed(monitor,3000)
    }




}