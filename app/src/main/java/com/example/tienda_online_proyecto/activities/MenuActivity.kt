package com.example.tienda_online_proyecto.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tienda_online_proyecto.R
import android.widget.ImageView
import android.widget.TextView

class MenuActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)


        // carrito
        findViewById<ImageView>(R.id.ivCarrito).setOnClickListener {
            val intent = Intent(this, CarritoActivity::class.java)
            startActivity(intent)
        }

    }
}