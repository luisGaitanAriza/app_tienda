package com.example.tienda_online_proyecto.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tienda_online_proyecto.R
import com.example.tienda_online_proyecto.adapter.ProductoAdapter
import com.example.tienda_online_proyecto.model.Categoria
import com.example.tienda_online_proyecto.model.Producto
import com.example.tienda_online_proyecto.utils.FirebaseService
import java.util.UUID

class AdminActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductoAdapter
    private val listaDeProductos = mutableListOf<Producto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        // interacion hacia Menu
        findViewById<CardView>(R.id.im_atras).setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.recyclerProductosAdmin)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ProductoAdapter(
            productos = listaDeProductos,
            onEditar = { producto -> editarProducto(producto) },
            onEliminar = { producto -> eliminarProducto(producto) }
        )
        recyclerView.adapter = adapter

        FirebaseService.obtenerProductos(
            onSuccess = { productos ->
                listaDeProductos.clear()
                listaDeProductos.addAll(productos)
                adapter.actualizarLista(listaDeProductos)
            },
            onError = { error ->
                Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        )

        findViewById<Button>(R.id.btnAddProduct).setOnClickListener {
            val intent = Intent(this, FormularioActivity::class.java)
            launcherFormulario.launch(intent)
        }
    }

    private fun cargarProductosDeEjemplo() {
        listaDeProductos.addAll(
            listOf(
                Producto("1", "Bicicleta de Montaña", 1200.0, "https://http2.mlstatic.com/D_NQ_NP_2X_956544-MCO78038720371_072024-T.webp", Categoria.ACCESORIO),
                Producto("2", "Casco Profesional", 250.0, "https://giant-bicycles.com.co/pub/media/catalog/product/cache/7a8f5b56e46e9009957f5cc3fe54a0bd/g/i/giant_rev_comp_mips_gloss-metallic-white_angle_1.jpeg", Categoria.CASCO),
                Producto("3", "Guantes MTB Antideslizantes", 45.0, "https://http2.mlstatic.com/D_NQ_NP_733363-MCO76673995104_062024-O.webp", Categoria.GUANTES),
                Producto("4", "Luces LED Delantera y Trasera", 30.0, "https://http2.mlstatic.com/D_NQ_NP_605288-MCO44654818481_012021-O.webp", Categoria.ACCESORIO),
                Producto("5", "Timbre de Aleación", 15.0, "https://http2.mlstatic.com/D_NQ_NP_904954-CBT81053181857_112024-O.webp", Categoria.ACCESORIO),
                Producto("6", "Kit de Reparación Portátil", 40.0, "https://media.startech.com/cms/products/gallery_large/ctk200.main.jpg", Categoria.ACCESORIO),
                Producto("7", "Cadena Reforzada", 75.0, "https://atmopel.com.co/wp-content/uploads/2022/10/048685.jpg", Categoria.ACCESORIO),
                Producto("8", "Portabotellas Universal", 20.0, "https://http2.mlstatic.com/D_Q_NP_625634-CBT81406885451_122024-O.webp", Categoria.ACCESORIO),
                Producto("9", "Asiento Ergonómico Gel", 95.0, "https://media.falabella.com/falabellaCO/136513755_01/w=800,h=800,fit=pad", Categoria.ACCESORIO),
                Producto("10", "Bomba de Aire Manual", 35.0, "https://cdn1.totalcommerce.cloud/homesentry/product-zoom/es/bomba-aire-custom-57772-60-psi-manual-rojo-1.webp", Categoria.ACCESORIO)
            )
        )
    }

    private fun editarProducto(producto: Producto) {
        val intent = Intent(this, FormularioActivity::class.java)
        intent.putExtra("producto", producto)
        launcherFormulario.launch(intent)
    }

    private fun eliminarProducto(producto: Producto) {
        FirebaseService.eliminarProducto(
            id = producto.id,
            onSuccess = {
                listaDeProductos.remove(producto)
                adapter.actualizarLista(listaDeProductos)
                Toast.makeText(this, "${producto.nombre} eliminado", Toast.LENGTH_SHORT).show()
            },
            onFailure = {
                Toast.makeText(this, "Error al eliminar ${producto.nombre}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun agregarNuevoProducto() {
        // Puedes abrir aquí una nueva pantalla o dialogo para crear uno
        val nuevo = Producto(
            id = UUID.randomUUID().toString(),
            nombre = "Nuevo producto",
            precio = 0.0,
            imagenUrl = "",
            categoria = Categoria.ACCESORIO
        )
        listaDeProductos.add(nuevo)
        adapter.actualizarLista(listaDeProductos)
        Toast.makeText(this, "Producto creado", Toast.LENGTH_SHORT).show()
    }

    private val launcherFormulario = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val producto = result.data?.getSerializableExtra("producto_resultado") as? Producto
            producto?.let {
                val index = listaDeProductos.indexOfFirst { p -> p.id == it.id }
                if (index >= 0) {
                    FirebaseService.actualizarProducto(
                        it,
                        onSuccess = {
                            listaDeProductos[index] = it
                            adapter.actualizarLista(listaDeProductos)
                            Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show()
                        },
                        onFailure = {
                            Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                        }
                    )
                } else {
                    FirebaseService.agregarProducto(
                        it,
                        onSuccess = {
                            listaDeProductos.add(it)
                            adapter.actualizarLista(listaDeProductos)
                            Toast.makeText(this, "Producto agregado", Toast.LENGTH_SHORT).show()
                        },
                        onFailure = {
                            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}