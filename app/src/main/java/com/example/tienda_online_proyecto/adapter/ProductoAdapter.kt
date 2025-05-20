package com.example.tienda_online_proyecto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tienda_online_proyecto.R
import com.example.tienda_online_proyecto.model.Producto
import com.bumptech.glide.Glide
import com.example.tienda_online_proyecto.util.Carrito

class ProductoAdapter(private var productos: List<Producto>) :
    RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.tvNombre)
        val precio: TextView = view.findViewById(R.id.tvPrecio)
        val imagen: ImageView = view.findViewById(R.id.imgProducto)
        val btnAgregar: ImageButton = view.findViewById(R.id.btnAgregar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.nombre.text = producto.nombre
        holder.precio.text = "$${producto.precio}"
        // Usa Glide o Picasso para cargar imágenes si usas URL reales
        Glide.with(holder.itemView.context)
            .load(producto.imagenUrl)
            .placeholder(R.drawable.logo) // opcional
            .error(R.drawable.logo)             // opcional
            .into(holder.imagen)

        val productoEnCarrito = Carrito.productos.any { it.id == producto.id }

        if (productoEnCarrito) {
            holder.btnAgregar.visibility = View.GONE
        } else {
            holder.btnAgregar.visibility = View.VISIBLE
        }

        holder.btnAgregar.setOnClickListener {
            Carrito.agregar(producto)
            holder.btnAgregar.visibility = View.GONE
            Toast.makeText(holder.itemView.context, "Producto agregado al carrito", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = productos.size

    fun actualizarLista(nuevaLista: List<Producto>) {
        productos = nuevaLista
        notifyDataSetChanged()
    }


}