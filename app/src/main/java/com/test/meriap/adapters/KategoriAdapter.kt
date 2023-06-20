package com.test.meriap.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.test.meriap.models.Kategori
import com.test.meriap.R

class KategoriAdapter(private val kategoriList: ArrayList<Kategori>,
                      private val itemClickListener: KategoriItemClickListener) : RecyclerView.Adapter<KategoriAdapter.KategoriViewHolder>() {

    interface KategoriItemClickListener {
        fun onKategoriItemClick(position: Int)
    }

    class KategoriViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.findViewById(R.id.kategoriayam)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KategoriViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.activity_kategori_item,
            parent,
            false
        )
        return KategoriViewHolder(view)
    }

    override fun getItemCount(): Int {
        return kategoriList.size
    }

    override fun onBindViewHolder(holder: KategoriViewHolder, position: Int) {
        val kategori = kategoriList[position]
        holder.imageView.setImageResource(kategori.image)

        holder.itemView.setOnClickListener {
            itemClickListener.onKategoriItemClick(position)
        }
    }
}