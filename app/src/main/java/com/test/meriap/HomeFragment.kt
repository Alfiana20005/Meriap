package com.test.meriap

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.meriap.adapters.KategoriAdapter
import com.test.meriap.models.Kategori

class HomeFragment : Fragment(), KategoriAdapter.KategoriItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var listKategori: ArrayList<Kategori>
    private lateinit var kategoriAdapter: KategoriAdapter
    private lateinit var tvUsername: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        tvUsername = view.findViewById(R.id.tvHaloUser)
        sharedPreferences =
            requireActivity().getSharedPreferences("Preference", Context.MODE_PRIVATE)

        val username = getUsername()
        tvUsername.text = "$username"

        initialize(view)
        return view
    }

    private fun initialize(view: View) {
        recyclerView = view.findViewById(R.id.kategoriRecycleview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        listKategori = ArrayList()
        tambahKeList()

        kategoriAdapter = KategoriAdapter(listKategori, this)
        recyclerView.adapter = kategoriAdapter
    }

    private fun tambahKeList() {
        listKategori.add(Kategori(R.drawable.kategori_ayam, "ayam"))
        listKategori.add(Kategori(R.drawable.kategori_daging, "daging"))
        listKategori.add(Kategori(R.drawable.kategori_sate, "sate"))
        listKategori.add(Kategori(R.drawable.kategori_sayur, "sayur"))
        listKategori.add(Kategori(R.drawable.kategori_jajanan, "jajanan"))
        listKategori.add(Kategori(R.drawable.kategori_sambal, "sambal"))
    }

    private fun getUsername(): String {
        return sharedPreferences.getString("nama", "") ?: ""
    }

    override fun onKategoriItemClick(position: Int) {
        val selectedKategori = listKategori[position]
        val intent = Intent(activity, DaftarActivity::class.java)
        intent.putExtra("menuName", selectedKategori.name)
        startActivity(intent)

    }
}