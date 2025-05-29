package com.example.tienda_online_proyecto.model
import java.io.Serializable

enum class Categoria {
    CASCO,
    GUANTES,
    ACCESORIO
}


data class Producto(
    val id: String,
    val nombre: String,
    val precio: Double,
    val imagenUrl: String,
    val categoria: Categoria, // "casco", "guantes", "accesorio"
    var cantidad: Int = 1
): Serializable
