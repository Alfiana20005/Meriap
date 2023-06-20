package com.test.meriap

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

class DetailActivity: AppCompatActivity() {
    private lateinit var menuTextView: TextView
    private lateinit var menuTextView2: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var descriptionTextView2: TextView
    private lateinit var descriptionTextView3: TextView
    private lateinit var saveButton: FloatingActionButton
    private lateinit var dbHelper: FirebaseFirestore
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        menuTextView = findViewById(R.id.menuTextView)
        menuTextView2 = findViewById(R.id.menuTextView2)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        descriptionTextView2 = findViewById(R.id.descriptionTextView2)
        descriptionTextView3 = findViewById(R.id.descriptionTextView3)
        saveButton = findViewById(R.id.saveButton)
        dbHelper = FirebaseFirestore.getInstance()
        sharedPreferences = getSharedPreferences("Preference", Context.MODE_PRIVATE)

        fetchDescriptionFromDatabase()
    }

    private fun fetchDescriptionFromDatabase() {
        dbHelper.collection("menus")
            .whereEqualTo("name", intent.getStringExtra("menuName"))
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val menuData = querySnapshot.documents[0]
                    menuData.reference.collection("bahan")
                        .get()
                        .addOnSuccessListener { bahanQuerySnapshot ->
                            val desc = bahanQuerySnapshot.documents.mapNotNull { it.getString("name") }
                            val formattedDesc = formatDescriptionWithHeader("Bahan - bahan", desc)
                            descriptionTextView.text = formattedDesc
                        }

                    menuData.reference.collection("bumbuHalus")
                        .get()
                        .addOnSuccessListener { bumbuHalusQuerySnapshot ->
                            val desc2 = bumbuHalusQuerySnapshot.documents.mapNotNull { it.getString("name") }
                            val formattedDesc2 = formatDescriptionWithHeader("Bumbu Halus", desc2)
                            descriptionTextView2.text = formattedDesc2
                            descriptionTextView2.visibility =
                                if (desc2.isNotEmpty()) View.VISIBLE else View.GONE
                        }

                    menuData.reference.collection("langkah")
                        .get()
                        .addOnSuccessListener { langkahQuerySnapshot ->
                            val desc3 = langkahQuerySnapshot.documents.mapNotNull { it.getString("name") }
                            val formattedDesc3 = formatDescriptionAsNumbered("Cara Membuat", desc3)
                            descriptionTextView3.text = formattedDesc3
                        }

                    menuTextView.text = menuData.getString("name")
                    menuTextView2.text = menuData.getString("name")

                }
            }
            .addOnFailureListener {

            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveDataToPlanner(view: View) {
        val username = getCurrentUsername()
        val now = LocalDate.now()

        val plannerCollectionRef = dbHelper.collection("planner")
        val query = plannerCollectionRef.whereEqualTo("username", username)

        query.get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // jika dokumen sudah ada
                    val documentSnapshot = querySnapshot.documents[0]
                    val savedMenuCollectionRef = documentSnapshot.reference.collection("savedMenu")
                    val menuName = menuTextView.text.toString()

                    val menuQuery = savedMenuCollectionRef.whereEqualTo("name", menuName)
                    menuQuery.get()
                        .addOnSuccessListener { menuQuerySnapshot ->
                            if (!menuQuerySnapshot.isEmpty) {
                                // jika menu sudah ada di planner
                                Toast.makeText(this, "Menu sudah ada di planner", Toast.LENGTH_SHORT).show()
                            } else {
                                val menuDocument = savedMenuCollectionRef.document()

                                val menuData = hashMapOf(
                                    "name" to menuName,
                                    "planDate" to Timestamp(Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                                )

                                menuDocument.set(menuData)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Berhasil ditambahkan ke Planner", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Gagal ditambahkan ke Planner", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                        .addOnFailureListener {

                        }
                } else {
                    val plannerData = hashMapOf(
                        "username" to username
                    )

                    plannerCollectionRef.add(plannerData)
                        .addOnSuccessListener { documentReference ->
                            val savedMenuCollectionRef = documentReference.collection("savedMenu")
                            val menuName = menuTextView.text.toString()

                            val menuData = hashMapOf(
                                "name" to menuName,
                                "planDate" to Timestamp(Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                            )

                            savedMenuCollectionRef.add(menuData)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Berhasil ditambahkan ke Planner", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Gagal ditambahkan ke Planner. Error: $e", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Gagal ditambahkan ke Planner", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {

            }
    }

    private fun formatDescription(descriptions: List<String>): String {
        val description = StringBuilder()
        for (i in descriptions.indices) {
            description.append("${descriptions[i]}\n")
        }
        return description.toString().trim()
    }

    private fun formatNumberedDescription(descriptions: List<String>): String {
        val description = StringBuilder()
        for (i in descriptions.indices) {
            val numberedItem = "${i + 1}. ${descriptions[i]}"
            description.append("$numberedItem\n")
        }
        return description.toString().trim()
    }

    private fun formatDescriptionAsNumbered(header: String, descriptions: List<String>): CharSequence {
        val numberedDescription = formatNumberedDescription(descriptions)

        val boldText = SpannableString(header)
        boldText.setSpan(StyleSpan(Typeface.BOLD), 0, boldText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val formattedText = "$boldText\n\n$numberedDescription"

        return formattedText
    }

    private fun formatDescriptionWithHeader(header: String, descriptions: List<String>): CharSequence {
        val description = formatDescription(descriptions)

        val boldText = SpannableString(header)
        boldText.setSpan(StyleSpan(Typeface.BOLD), 0, boldText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val formattedText = "$boldText\n\n$description"

        return formattedText
    }

    private fun getCurrentUsername(): String {
        return sharedPreferences.getString("username", "") ?: ""
    }
}