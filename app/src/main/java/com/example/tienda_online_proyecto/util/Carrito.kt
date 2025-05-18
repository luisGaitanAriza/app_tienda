package com.example.tienda_online_proyecto.util

import com.example.tienda_online_proyecto.model.Producto

object Carrito {
    val productos = mutableListOf<Producto>()

    fun agregar(producto: Producto) {
        productos.add(producto)
    }

    fun eliminar(producto: Producto) {
        productos.remove(producto)
    }
}