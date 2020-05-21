package com.example.tokend.adapter



import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.tokend.R


class ContactViewHolder(itemView: View) : ViewHolder(itemView) {
    internal var m: ImageView
    internal var txt: TextView

    init {
        m = itemView.findViewById(R.id.ImgIcon)
        txt = itemView.findViewById(R.id.txt)
    }
}