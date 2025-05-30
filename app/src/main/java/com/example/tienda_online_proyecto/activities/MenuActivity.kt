package com.example.tienda_online_proyecto.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tienda_online_proyecto.R
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tienda_online_proyecto.adapter.ProductoAdapter
import com.example.tienda_online_proyecto.model.Categoria
import com.example.tienda_online_proyecto.model.Producto
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.tienda_online_proyecto.utils.FirebaseService
import com.example.tienda_online_proyecto.utils.LocationHelper
import com.google.firebase.FirebaseApp

class MenuActivity :AppCompatActivity(){
    private var productoAdapter = ProductoAdapter(emptyList())

    private lateinit var locationHelper: LocationHelper
    private var productosCache: List<Producto> = emptyList()

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
        setContentView(R.layout.activity_menu)

        FirebaseApp.initializeApp(this)

        // Inicializar el helper DESPUÉS de registrar el permiso
        val tvDireccion = findViewById<TextView>(R.id.tvDireccion)
        locationHelper = LocationHelper(this, locationPermissionRequest)
        locationHelper.checkLocationPermission(tvDireccion)

        // carrito
        findViewById<ImageView>(R.id.ivCarrito).setOnClickListener {
            val intent = Intent(this, CarritoActivity::class.java)
            startActivity(intent)
        }

<<<<<<< HEAD
        // perfil
        findViewById<ImageView>(R.id.ivperfil).setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }


=======
>>>>>>> c654334ac77b8474192a1fb96a06772433f86dca
        // admin
        findViewById<CardView>(R.id.ic_lista).setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
        }

        // productos
        val listaCompleta = listOf(
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

        val recyclerProductos = findViewById<RecyclerView>(R.id.recyclerProductos)
        recyclerProductos.layoutManager = GridLayoutManager(this, 2)

        productoAdapter = ProductoAdapter(emptyList())
        recyclerProductos.adapter = productoAdapter

        // Obtener productos de Firestore
        FirebaseService.obtenerProductos(
            onSuccess = { productos ->
                productosCache = productos
                productoAdapter.actualizarLista(productos)
            },
            onError = { error ->
                Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        )

        val fragment = supportFragmentManager.findFragmentById(R.id.filtrosFragmentContainer) as? com.example.tienda_online_proyecto.fragments.FiltrosFragment

        fragment?.setOnFiltroSeleccionado { categoriaSeleccionada ->
            val filtrados = if (categoriaSeleccionada == null) {
                productosCache
            } else {
                productosCache.filter { it.categoria == categoriaSeleccionada }
            }
            productoAdapter.actualizarLista(filtrados)
        }
    }

    override fun onResume() {
        super.onResume()
        productoAdapter.notifyDataSetChanged()
    }


}