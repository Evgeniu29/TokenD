package com.example.tokend.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tokend.R


class ContactsAdapter(data: ArrayList<String>?): RecyclerView.Adapter<ContactViewHolder>() {
    private val data: ArrayList<String>? = data
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflator: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflator.inflate(R.layout.list_item, parent, false)

        return ContactViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val title: String = data!!.get(position)
        holder.txt.text = title
    }

}