package com.example.tienda_online_proyecto.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.tienda_online_proyecto.R
import com.example.tienda_online_proyecto.model.Producto
import android.app.Activity
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.tienda_online_proyecto.model.Categoria
import java.util.UUID

class FormularioActivity : AppCompatActivity() {
    private lateinit var etNombre: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etImagenUrl: EditText
    private lateinit var spinnerCategoria: Spinner
    private lateinit var btnGuardar: Button

    private var productoExistente: Producto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario)

        // Botón volver (ícono)
        findViewById<CardView>(R.id.im_atras).setOnClickListener {
            finish()
        }

        // Botón cancelar (texto)
        findViewById<Button>(R.id.btnCancelar).setOnClickListener {
            finish()
        }

        etNombre = findViewById(R.id.etNombre)
        etPrecio = findViewById(R.id.etPrecio)
        etImagenUrl = findViewById(R.id.etImagenUrl)
        spinnerCategoria = findViewById(R.id.spinnerCategoria)
        btnGuardar = findViewById(R.id.btnGuardar)

        // Spinner
        val categorias = Categoria.values().map { it.name.lowercase().replaceFirstChar { c -> c.uppercase() } }
        spinnerCategoria.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categorias)

        // Revisar si es edición
        productoExistente = intent.getSerializableExtra("producto") as? Producto
        productoExistente?.let { producto ->
            title = "Editar producto"
            etNombre.setText(producto.nombre)
            etPrecio.setText(producto.precio.toString())
            etImagenUrl.setText(producto.imagenUrl)
            spinnerCategoria.setSelection(producto.categoria.ordinal)
        } ?: run {
            title = "Nuevo producto"
        }

        btnGuardar.setOnClickListener {
            guardarProducto()
        }
    }

    private fun guardarProducto() {
        val nombre = etNombre.text.toString()
        val precioTexto = etPrecio.text.toString()
        val imagenUrl = etImagenUrl.text.toString()
        val categoriaSeleccionada = Categoria.values()[spinnerCategoria.selectedItemPosition]

        if (nombre.isBlank() || precioTexto.isBlank() || imagenUrl.isBlank()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val precio = precioTexto.toDoubleOrNull()
        if (precio == null) {
            Toast.makeText(this, "Precio inválido", Toast.LENGTH_SHORT).show()
            return
        }

        val producto = Producto(
            id = productoExistente?.id ?: UUID.randomUUID().toString(),
            nombre = nombre,
            precio = precio,
            imagenUrl = imagenUrl,
            categoria = categoriaSeleccionada
        )

        val intentResultado = Intent().apply {
            putExtra("producto_resultado", producto)
        }
        setResult(RESULT_OK, intentResultado)
        finish()
    }
}