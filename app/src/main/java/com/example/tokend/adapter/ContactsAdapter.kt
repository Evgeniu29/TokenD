package com.example.tokend.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.codility.contacts.model.Contact
import com.example.tokend.R
import com.example.tokend.model.OnCallListener

class ContactsAdapter(private var contactList: ArrayList<Contact>) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    private var onCallListener: OnCallListener<Contact>? = null

    fun setListener(onCallListener: OnCallListener<Contact>) {
        this.onCallListener = onCallListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contactList[position]
        holder.bindItems(contact)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(contact: Contact) {

            val txt = itemView.findViewById<TextView>(R.id.txt)

            val photo = itemView.findViewById<ImageView>(R.id.photo)

            txt.text = contact.name

            Glide.with(itemView)
                .load(contact.image)
                .placeholder(R.drawable.noimage).error(R.drawable.noimage).fallback(R.drawable.noimage)
                .into(photo);


        }}

    fun filterList(filteredNames: ArrayList < Contact > ) {
        Log.e("list", filteredNames.toString())
        Log.e("list", filteredNames.size.toString())
        // this.dataList.clear()
        contactList = filteredNames
        notifyDataSetChanged()
    }
}