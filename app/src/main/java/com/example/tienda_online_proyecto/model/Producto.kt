package com.example.tienda_online_proyecto.model

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
)
