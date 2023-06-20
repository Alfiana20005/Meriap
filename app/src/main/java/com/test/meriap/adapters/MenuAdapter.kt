package com.test.meriap.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.test.meriap.R
import com.test.meriap.models.Menu

class MenuAdapter(private val menus: List<Menu>) :
    RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menu = menus[position]
        holder.menuName.text = menu.name
        holder.menuDescription.text = menu.description
    }

    override fun getItemCount(): Int {
        return menus.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val menuName: TextView = view.findViewById(R.id.menuName)
        val menuDescription: TextView = view.findViewById(R.id.menuDescription)
    }
}