package com.example.tokend.adapter

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

import com.codility.contacts.model.Contact
import com.example.tokend.R
import com.example.tokend.model.OnCallListener
import android.graphics.BitmapFactory
import android.provider.ContactsContract
import android.content.ContentUris
import android.graphics.Bitmap
import java.io.IOException


class ContactsAdapter(private val contactList: ArrayList<Contact>) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

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
}