package com.example.tienda_online_proyecto.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tienda_online_proyecto.R
import com.example.tienda_online_proyecto.adapter.CarritoAdapter
import com.example.tienda_online_proyecto.util.Carrito
import com.example.tienda_online_proyecto.utils.LocationHelper

class CarritoActivity : AppCompatActivity() {
    private var adapter: CarritoAdapter? = null

    private lateinit var locationHelper: LocationHelper

    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                val tvDireccion = findViewById<TextView>(R.id.tvDireccion)
                locationHelper.getCurrentLocation(tvDireccion)
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        // Inicializar el helper DESPUÉS de registrar el permiso
        val tvDireccion = findViewById<TextView>(R.id.tvDireccion)
        locationHelper = LocationHelper(this, locationPermissionRequest)
        locationHelper.checkLocationPermission(tvDireccion)

        val recyclerCarrito = findViewById<RecyclerView>(R.id.recyclerCarrito)
        recyclerCarrito.layoutManager = LinearLayoutManager(this)
        adapter = CarritoAdapter(Carrito.productos).apply {
            onCantidadCambiada = { calcularTotal() }
        }
        recyclerCarrito.adapter = adapter

        // interacion hacia Menu
        findViewById<CardView>(R.id.im_atras).setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        calcularTotal()
    }

    private fun calcularTotal() {
        val total = Carrito.productos.sumOf { it.precio * it.cantidad }
        val tvTotal = findViewById<TextView>(R.id.tvTotal)
        tvTotal.text = "Total: $${"%.2f".format(total)}"
    }
}
