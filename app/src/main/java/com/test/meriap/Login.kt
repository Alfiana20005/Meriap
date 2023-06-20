package com.test.meriap

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {
    private lateinit var loginBtn: Button
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvRegister: TextView
    private lateinit var dbHelper: FirebaseFirestore
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginBtn = findViewById(R.id.button)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        tvRegister = findViewById(R.id.textView8)
        dbHelper = FirebaseFirestore.getInstance()
        sharedPreferences = getSharedPreferences("Preference", Context.MODE_PRIVATE)


        loginBtn.setOnClickListener {
            val usernameEdit = etUsername.text.toString()
            val passwordEdit = etPassword.text.toString()

            clearSharedPreferences()

            if (TextUtils.isEmpty(usernameEdit) || TextUtils.isEmpty(passwordEdit)) {
                Toast.makeText(this, "Silahkan Masukkan Data Anda!", Toast.LENGTH_SHORT).show()
            } else {
                dbHelper.collection("users")
                    .whereEqualTo("username", usernameEdit)
                    .whereEqualTo("password", passwordEdit)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (querySnapshot.isEmpty) {
                            Toast.makeText(this, "Username dan Password Salah!", Toast.LENGTH_SHORT).show()
                        } else {
                            val userDocument = querySnapshot.documents[0]
                            val nama = userDocument.getString("nama")
                            val role = userDocument.getString("role")
                            val username = userDocument.getString("username")

                            Toast.makeText(this, "Login Berhasil!", Toast.LENGTH_SHORT).show()

                            simpanData(nama as String, role as String, username as String)

                            if(role == "user")
                            {
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            }
                            else
                            {
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            }
                            finish()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Gagal melakukan login: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        tvRegister.setOnClickListener { view ->
            view.post {
                val intent = Intent(this, RegisterAkun::class.java)
                startActivity(intent)
            }
        }
    }

    private fun simpanData(nama: String, role: String, username: String){
        val editor = sharedPreferences.edit()
        editor.putString("nama", nama)
        editor.putString("role", role)
        editor.putString("username", username)
        editor.apply()
    }

    private fun clearSharedPreferences() {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}