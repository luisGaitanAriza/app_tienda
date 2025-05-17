package com.example.tienda_online_proyecto.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tienda_online_proyecto.R
import com.google.firebase.auth.FirebaseAuth

class RegistroActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etContrasena: EditText
    private lateinit var btnCrearCuenta: Button
    private lateinit var btnIrLogin: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Referencias a vistas
        etNombre = findViewById(R.id.et_nombre)
        etApellido = findViewById(R.id.et_apellido)
        etCorreo = findViewById(R.id.et_correo)
        etContrasena = findViewById(R.id.et_contrasena)
        btnCrearCuenta = findViewById(R.id.btn_crear_cuenta)
        btnIrLogin = findViewById(R.id.btn_ir_login)

        // Navegar a login
        btnIrLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // Crear cuenta
        btnCrearCuenta.setOnClickListener {
            registrarUsuario()
        }
    }

    private fun registrarUsuario() {
        val nombre = etNombre.text.toString().trim()
        val apellido = etApellido.text.toString().trim()
        val correo = etCorreo.text.toString().trim()
        val contrasena = etContrasena.text.toString().trim()

        // Validaciones
        if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            showAlert("Todos los campos son obligatorios.")
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            showAlert("Correo electrónico no válido.")
            return
        }

        if (contrasena.length < 6) {
            showAlert("La contraseña debe tener al menos 6 caracteres.")
            return
        }

        // Crear usuario en Firebase
        auth.createUserWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registro exitoso
                    val intent = Intent(this, MenuActivity::class.java)
                    intent.putExtra("nombre", nombre)
                    startActivity(intent)
                    finish()
                } else {
                    // Fallo al registrarse
                    showAlert("No se pudo registrar. Intente con otro correo.")
                }
            }
    }

    private fun showAlert(mensaje: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        builder.create().show()
    }
}
