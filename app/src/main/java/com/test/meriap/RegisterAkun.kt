package com.test.meriap

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore


class RegisterAkun : AppCompatActivity() {
    private lateinit var nama : EditText
    private lateinit var email: EditText
    private lateinit var username : EditText
    private lateinit var password : EditText
    private lateinit var konfirmasiPassword : EditText
    private lateinit var tvLogin : TextView
    private lateinit var btnregis: Button
    private lateinit var db: FirebaseFirestore
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()
        setContentView(R.layout.activity_register_akun)

        nama = findViewById(R.id.inputNama)
        email = findViewById(R.id.inputEmail)
        username = findViewById(R.id.inputUsername)
        password = findViewById(R.id.inputPassword)
        konfirmasiPassword = findViewById(R.id.inputKonfirmasiPassword)
        tvLogin = findViewById(R.id.textView8)
        btnregis = findViewById(R.id.button)
        db = FirebaseFirestore.getInstance()
        sharedPreferences = getSharedPreferences("Preference", Context.MODE_PRIVATE)

        btnregis.setOnClickListener {
            val namaTeks = nama.text.toString()
            val emailTeks = email.text.toString()
            val usernameTeks = username.text.toString()
            val passTeks = password.text.toString()
            val konfirmasiPassword = konfirmasiPassword.text.toString()

            if(TextUtils.isEmpty(namaTeks) || TextUtils.isEmpty(emailTeks) || TextUtils.isEmpty(usernameTeks) || TextUtils.isEmpty(passTeks) || TextUtils.isEmpty(konfirmasiPassword)) {
                Toast.makeText(this, "Silahkan isi data!", Toast.LENGTH_SHORT).show()
            }
            else {
                if(passTeks.equals(konfirmasiPassword)) {
                    val userData = hashMapOf(
                        "nama" to namaTeks,
                        "email" to emailTeks,
                        "username" to usernameTeks,
                        "password" to passTeks,
                        "role" to "user"
                    )

                    db.collection("users")
                        .add(userData)
                        .addOnSuccessListener { documentReference ->
                            Toast.makeText(this, "Berhasil Registrasi!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(applicationContext, Login::class.java)
                            startActivity(intent)
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Gagal menyimpan data: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                else{
                    Toast.makeText(this, "Password dan Konfirmasi Password harus sama", Toast.LENGTH_SHORT).show()
                }
            }
        }

        tvLogin.setOnClickListener { view ->
            view.post {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }
        }
    }
}