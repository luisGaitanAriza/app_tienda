package com.example.tienda_online_proyecto.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import com.example.tienda_online_proyecto.R

class CarritoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)


        // interacion hacia Menu
        findViewById<ImageView>(R.id.im_atras).setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)   }


        // cantidad de productos
        val numberPicker = findViewById<NumberPicker>(R.id.numberPickerCantidad)
        numberPicker.minValue = 0
        numberPicker.maxValue = 10
        numberPicker.value = 1

        numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            println("Cantidad seleccionada: $newVal")
    }
        // cantidad de productos1
        val numberPicker1 = findViewById<NumberPicker>(R.id.numberPickerCantidad1)
        numberPicker1.minValue = 0
        numberPicker1.maxValue = 10
        numberPicker1.value = 1

        numberPicker1.setOnValueChangedListener { _, oldVal, newVal ->
            println("Cantidad seleccionada en primer producto: $newVal")
        }


    }
}
