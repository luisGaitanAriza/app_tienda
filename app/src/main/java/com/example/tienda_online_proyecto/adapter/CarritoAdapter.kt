package com.example.tienda_online_proyecto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tienda_online_proyecto.R
import com.example.tienda_online_proyecto.model.Producto
import com.example.tienda_online_proyecto.util.Carrito

class CarritoAdapter(private val productos: List<Producto>) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {
    var onCantidadCambiada: (() -> Unit)? = null

    class CarritoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProducto: ImageView = itemView.findViewById(R.id.ivProductoCarrito)
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombreCarrito)
        val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecioCarrito)
        val tvCantidad: TextView = itemView.findViewById(R.id.tvCantidad)
        val btnSumar: ImageButton = itemView.findViewById(R.id.btnSumar)
        val btnRestar: ImageButton = itemView.findViewById(R.id.btnRestar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carrito, parent, false)
        return CarritoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val producto = productos[position]
        holder.tvNombre.text = producto.nombre
        holder.tvPrecio.text = "$${producto.precio}" // puedes ajustar a tu formato

        Glide.with(holder.itemView.context)
            .load(producto.imagenUrl)
            .into(holder.ivProducto)

        holder.tvCantidad.text = producto.cantidad.toString()

        holder.btnSumar.setOnClickListener {
            if (producto.cantidad < 10) {
                producto.cantidad++
                holder.tvCantidad.text = producto.cantidad.toString()
            }
            onCantidadCambiada?.invoke()
        }

        holder.btnRestar.setOnClickListener {
            if (producto.cantidad > 1) {
                producto.cantidad--
                holder.tvCantidad.text = producto.cantidad.toString()
            } else {
                // 👇 Eliminar del carrito si la cantidad era 1
                Carrito.eliminar(producto)

                // Notificar al adaptador del cambio
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, Carrito.productos.size)
            }
            onCantidadCambiada?.invoke()
        }
    }

    override fun getItemCount(): Int = productos.size
}