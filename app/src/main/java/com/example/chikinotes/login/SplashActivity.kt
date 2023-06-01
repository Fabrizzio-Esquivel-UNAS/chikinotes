package com.example.chikinotes.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.chikinotes.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_splash)
        splashScreen.setKeepOnScreenCondition { true }
        val pantallaLogin = Intent(this, LoginActivity::class.java)
        startActivity(pantallaLogin)
        finish()
    }
}