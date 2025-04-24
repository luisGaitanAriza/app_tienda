package com.example.tienda_online_proyecto.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.tienda_online_proyecto.R

class RegistroActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)


        // interacion hacia login
        findViewById<Button>(R.id.btn_ir_login).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)   }
    }

}