package com.example.tienda_online_proyecto.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.tienda_online_proyecto.R
import com.example.tienda_online_proyecto.model.Categoria

class FiltrosFragment : Fragment() {

    private var filtroListener: ((Categoria?) -> Unit)? = null
    private var filtroActivo: Categoria? = null

    fun setOnFiltroSeleccionado(listener: (Categoria?) -> Unit) {
        filtroListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_filtros, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCascos = view.findViewById<LinearLayout>(R.id.btnCascos)
        val btnGuantes = view.findViewById<LinearLayout>(R.id.btnGuantes)
        val btnAccesorios = view.findViewById<LinearLayout>(R.id.btnAccesorios)

        btnCascos.setOnClickListener { manejarFiltro(Categoria.CASCO) }
        btnGuantes.setOnClickListener { manejarFiltro(Categoria.GUANTES) }
        btnAccesorios.setOnClickListener { manejarFiltro(Categoria.ACCESORIO) }
    }

    private fun manejarFiltro(categoria: Categoria) {
        if (filtroActivo == categoria) {
            filtroActivo = null
        } else {
            filtroActivo = categoria
        }
        filtroListener?.invoke(filtroActivo)
    }
}
