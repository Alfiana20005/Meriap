package com.test.meriap

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.meriap.adapters.DaftarAdapter
import com.test.meriap.models.Daftar

class DaftarActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var listDaftar: ArrayList<Daftar>
    private lateinit var daftarAdapter: DaftarAdapter
    private lateinit var daftarTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar)

        initialize()
    }

    private fun initialize(){
        daftarTextView = findViewById(R.id.daftarTextView)
        recyclerView = findViewById(R.id.daftarRecycleview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val menuName = intent.getStringExtra("menuName")

        daftarTextView.text = "Daftar Menu $menuName"

        listDaftar = ArrayList()
        tambahKeList()

        daftarAdapter = DaftarAdapter(listDaftar) { daftar ->
            navigateToDetail(daftar)
        }
        recyclerView.adapter = daftarAdapter
    }

    private fun tambahKeList(){
        listDaftar.add(Daftar(R.drawable.rembige, "Sate Rembige"))
        listDaftar.add(Daftar(R.drawable.pusut, "Sate Pusut"))
        listDaftar.add(Daftar(R.drawable.bulayak, "Sate Bulayak"))
        listDaftar.add(Daftar(R.drawable.tanjung, "Sate Tanjung"))
        listDaftar.add(Daftar(R.drawable.pencok, "Sate Pencok"))
        listDaftar.add(Daftar(R.drawable.sagu, "Sate Sagu"))
    }

    private fun navigateToDetail(daftar: Daftar) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("menuName", daftar.name)
        startActivity(intent)
    }
}