package com.test.meriap

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class PlannerFragment : Fragment() {

    private val dbHelper: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var containerLayout: ViewGroup
    private  lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedPreferences = requireActivity().getSharedPreferences("Preference", Context.MODE_PRIVATE)
        val view = inflater.inflate(R.layout.fragment_planner, container, false)
        containerLayout = view.findViewById(R.id.containerLayout)

        fetchPlannerData()

        return view
    }

    private fun fetchPlannerData() {
        dbHelper.collection("planner")
            .whereEqualTo("username", sharedPreferences.getString("username", "") ?: "")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (documentSnapshot in querySnapshot) {
                    dbHelper.collection("planner")
                        .document(documentSnapshot.id)
                        .collection("savedMenu")
                        .get()
                        .addOnSuccessListener { savedMenuSnapshot ->
                            for (savedMenuDoc in savedMenuSnapshot.documents) {
                                val savedMenuData = savedMenuDoc.data

                                val menuName = savedMenuData?.get("name") as? String
                                val planDateTimestamp = savedMenuData?.get("planDate") as? Timestamp

                                val plannerItemView = layoutInflater.inflate(
                                    R.layout.item_planner,
                                    containerLayout,
                                    false
                                )

                                val nameTextView =
                                    plannerItemView.findViewById<TextView>(R.id.nameTextView)
                                val dateEditText =
                                    plannerItemView.findViewById<EditText>(R.id.dateEditText)
                                val iconImageView =
                                    plannerItemView.findViewById<ImageView>(R.id.iconImageView)

                                nameTextView.text = menuName
                                dateEditText.setText(formatTimestamp(planDateTimestamp))

                                iconImageView.setOnClickListener {
                                    onEditClicked(dateEditText.text.toString(), savedMenuDoc.id)
                                }

                                containerLayout.addView(plannerItemView)
                            }
                        }
                        .addOnFailureListener { exception ->
                            // Handle any errors that occur while fetching data
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors that occur while fetching data
            }
    }

    private fun onEditClicked(planDate: String, documentId: String) {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = try {
            val parsedDate = dateFormatter.parse(planDate)
            parsedDate?.let {
                Timestamp(it)
            }
        } catch (e: ParseException) {
            null
        }

        dbHelper.collection("planner")
            .whereEqualTo("username", sharedPreferences.getString("username", "") ?: "")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (documentSnapshot in querySnapshot) {
                    dbHelper.collection("planner")
                        .document(documentSnapshot.id)
                        .collection("savedMenu")
                        .document(documentId)
                        .update("planDate", formattedDate)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Tanggal berhasil diganti", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(requireContext(), "Tanggal gagal diganti", Toast.LENGTH_SHORT).show()
                        }
                }
            }
    }

    private fun formatTimestamp(timestamp: Timestamp?): String {
        if (timestamp != null) {
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return dateFormatter.format(timestamp.toDate())
        }
        return ""
    }
}