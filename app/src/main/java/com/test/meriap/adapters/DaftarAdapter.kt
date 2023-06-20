package com.test.meriap.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.test.meriap.models.Daftar
import com.test.meriap.R

class DaftarAdapter(private val daftarList: ArrayList<Daftar>, private val onItemClick: (Daftar) -> Unit) : RecyclerView.Adapter<DaftarAdapter.DaftarViewHolder>() {

    class DaftarViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.findViewById(R.id.rembige)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaftarViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.item_daftar,
            parent,
            false
        )
        return DaftarViewHolder(view)
    }

    override fun getItemCount(): Int {
        return daftarList.size
    }

    override fun onBindViewHolder(holder: DaftarViewHolder, position: Int) {
        val daftar = daftarList[position]
        holder.imageView.setImageResource(daftar.image)

        // Set click listener for the item
        holder.itemView.setOnClickListener {
            onItemClick(daftar)
        }
    }
}
