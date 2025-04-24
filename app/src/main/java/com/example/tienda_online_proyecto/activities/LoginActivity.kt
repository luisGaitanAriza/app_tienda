package com.example.tienda_online_proyecto.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tienda_online_proyecto.R

class LoginActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        // interacion hacia RegistroActivity
        findViewById<TextView>(R.id.tv_login_registro).setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)}


        }
    }

