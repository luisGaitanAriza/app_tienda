package com.example.tienda_online_proyecto.utils

import com.google.firebase.firestore.FirebaseFirestore
import com.example.tienda_online_proyecto.model.Producto

object FirebaseService {
    private val db = FirebaseFirestore.getInstance()
    private val productosRef = db.collection("productos")

    fun agregarProducto(producto: Producto, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        productosRef.document(producto.id)
            .set(producto)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun obtenerProductos(
        onSuccess: (List<Producto>) -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("productos")
            .get()
            .addOnSuccessListener { result ->
                val productos = result.mapNotNull { it.toObject(Producto::class.java) }
                onSuccess(productos)
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Error desconocido al obtener productos")
            }
    }

    fun actualizarProducto(producto: Producto, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        productosRef.document(producto.id)
            .set(producto)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun eliminarProducto(id: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        productosRef.document(id)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}
