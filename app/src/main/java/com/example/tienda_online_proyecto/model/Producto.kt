package com.example.tienda_online_proyecto.model
import java.io.Serializable

enum class Categoria {
    CASCO,
    GUANTES,
    ACCESORIO
}


data class Producto(
    var id: String = "",
    var nombre: String = "",
    var precio: Double = 0.0,
    var imagenUrl: String = "",
    var categoria: Categoria = Categoria.ACCESORIO,
    var cantidad: Int = 1
): Serializable
