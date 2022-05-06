package com.masterandroid.contactlist_sqlite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.masterandroid.contactlist_sqlite.R
import com.masterandroid.contactlist_sqlite.databinding.ItemContactBinding
import com.masterandroid.contactlist_sqlite.model.Contact

class ContactAdapter(var list:ArrayList<Contact>, var onItemClickListener:OnItemClickListener):RecyclerView.Adapter<ContactAdapter.Vh>() {

    inner class Vh(var itemContactBinding: ItemContactBinding):RecyclerView.ViewHolder(itemContactBinding.root){

        fun onBind(contact: Contact,position: Int){
            itemContactBinding.tvName.text = contact.name
            itemContactBinding.tvNumber.text = contact.phoneNumber
            itemContactBinding.ivMore.setOnClickListener {
                onItemClickListener.onItemClick(contact,position,itemContactBinding.ivMore)
            }
            itemContactBinding.root.setOnClickListener {
                onItemClickListener.onItemContactClick(contact)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemContactBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position],position)
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener{
        fun onItemClick(contact: Contact,position: Int,imageView: ImageView)
        fun onItemContactClick(contact: Contact)
    }
}