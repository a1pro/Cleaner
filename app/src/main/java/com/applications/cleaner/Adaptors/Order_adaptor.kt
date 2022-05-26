package com.applications.cleaner.Adaptors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.applications.cleaner.Models.Order_Data
import com.applications.cleaner.R

class Order_adaptor(val list: List<Order_Data>) : RecyclerView.Adapter<Order_adaptor.viewholder>(){
    class viewholder(ItemView :View) : RecyclerView.ViewHolder(ItemView){
        val phone_orderlist : TextView = ItemView.findViewById(R.id.phone_orderlist)
        val name_orderlist : TextView = ItemView.findViewById(R.id.name_orderlist)
        val order_id : TextView = ItemView.findViewById(R.id.order_id)


        val more_info : TextView = ItemView.findViewById(R.id.more_info)


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val ItemView=LayoutInflater.from(parent.context).inflate(R.layout.item_orders,parent,false)
        return viewholder(ItemView)
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        val currentItem=list[position]
        holder.order_id.text = currentItem.bookingId
        holder.name_orderlist.text = currentItem.fname+" "+currentItem.lname
        holder.phone_orderlist.text = currentItem.phone

        holder.more_info.setOnClickListener {view->

            view.findNavController().navigate(R.id.action_orders_Fragment_to_order_Detail_Fragment,
                Bundle().apply {
                    putSerializable("data",currentItem)
                    putString("bookingId",currentItem.bookingId)
                    putString("bookingId",currentItem.bookingId)
                    putString("status", currentItem.status?.toString())
                    putString("preferredTime",currentItem.preferredTime)
                    putString("name", currentItem.fname+" "+currentItem.lname)
                    putString("phone", currentItem.phone)
                    putString("address", currentItem.address)
                    putString("lat", currentItem.latitude)
                    putString("lng", currentItem.longitude)
                    putString("address", currentItem.address)
                    putString("singleOven", currentItem.singleOven)
                    putString("extraProduct", currentItem.extraProduct)
                    putString("totalAmout", currentItem.totalAmout)
                    putString("otherNotes", currentItem.otherNotes?.toString())

                }
            )

        }
    }


    override fun getItemCount()=list.size

}
