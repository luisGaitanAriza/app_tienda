package com.example.tienda_online_proyecto.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tienda_online_proyecto.R
import com.example.tienda_online_proyecto.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    // Código de solicitud para el inicio de sesión con Google
    private companion object {
        private const val RC_SIGN_IN = 9001
        private const val TAG = "GoogleActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Configurar Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setupViews()
    }

    private fun setupViews() {
        // Ir a registro
        binding.tvLoginRegistro.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }

        // Login con email
        binding.btnLoginIniciar.setOnClickListener {
            val email = binding.etCorreo.text.toString().trim()
            val password = binding.etContrasena.text.toString().trim()

            when {
                email.isEmpty() || password.isEmpty() -> {
                    showAlert("Por favor complete todos los campos")
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    showAlert("El correo no tiene un formato válido")
                }
                password.length < 6 -> {
                    showAlert("La contraseña debe tener al menos 6 caracteres")
                }
                else -> {
                    performLogin(email, password)
                }
            }
        }

        // Login con Google
        binding.google.setOnClickListener {
            signInWithGoogle()
        }

        // Login con Facebook (puedes implementarlo similarmente)
        binding.facebook.setOnClickListener {
            Toast.makeText(this, "Login con Facebook (pendiente)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performLogin(email: String, password: String) {
        binding.btnLoginIniciar.isEnabled = false

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                binding.btnLoginIniciar.isEnabled = true

                if (task.isSuccessful) {
                    // Login exitoso
                    startActivity(Intent(this, MenuActivity::class.java))
                    finish()
                } else {
                    showAlert("Error al iniciar sesión: ${task.exception?.message ?: "Error desconocido"}")
                }
            }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Resultado devuelto al lanzar el Intent de Google Sign In
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In fue exitoso, autenticar con Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In falló
                Log.w(TAG, "Google sign in failed", e)
                val errorMessage = when (e.statusCode) {
                    GoogleSignInStatusCodes.SIGN_IN_CANCELLED -> "Inicio de sesión cancelado"
                    GoogleSignInStatusCodes.SIGN_IN_FAILED -> "Error en el inicio de sesión"
                    else -> "Error desconocido: ${e.message}"
                }
                showAlert(errorMessage)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    Log.d(TAG, "signInWithCredential:success")
                    startActivity(Intent(this, MenuActivity::class.java))
                    finish()
                } else {
                    // If sign in fails
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    showAlert("Error al autenticar con Google: ${task.exception?.message}")
                }
            }
    }

    private fun showAlert(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("Aceptar", null)
            .create()
            .show()
    }
}