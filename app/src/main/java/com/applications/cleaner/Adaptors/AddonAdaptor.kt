package com.applications.cleaner.Adaptors

import android.system.Os.remove
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.applications.cleaner.Interface.Addon_product
import com.applications.cleaner.Models.DataAddOns
import com.applications.cleaner.R

class AddonAdaptor(val list: List<DataAddOns>,  var addonProduct: Addon_product):
    RecyclerView.Adapter<AddonAdaptor.viewholder>() {
    class viewholder(itemview: View) : RecyclerView.ViewHolder(itemview){
        val price_addon : TextView = itemview.findViewById(R.id.price_addon)
        val addon_plus : ImageView = itemview.findViewById(R.id.addon_plus)
        val tv_name : TextView = itemview.findViewById(R.id.tv_name)




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val  itemview=LayoutInflater.from(parent.context).inflate(R.layout.item_addon,parent,false)
        return viewholder(itemview)
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        val model=list[position]
        holder.price_addon.text="£ "+model.price
        holder.tv_name.text="£ "+model.nameProduct
        holder.addon_plus.setOnClickListener { it ->

            it.setSelected(!it.isSelected())
//                holder.view.setBackgroundColor(model.isSelected() ? Color.LTGRAY : Color.WHITE);
            //                holder.view.setBackgroundColor(model.isSelected() ? Color.LTGRAY : Color.WHITE);
            if (it.isSelected()) {
                addonProduct.product(model.id.toString(),model.price!!.toDouble(), model.wages_price!!.toDouble())
                holder.addon_plus.setImageResource(R.drawable.remove);

//                    Toast.makeText(context, ""+list1.size(), Toast.LENGTH_SHORT).show();
            }
            if (!it.isSelected()) {
                addonProduct.product_dissmiss(model.id.toString(),model.price!!.toDouble(), model.wages_price!!.toDouble())
                holder.addon_plus.setImageResource(R.drawable.add);

            }


        }

    }


    override fun getItemCount(): Int = list.size

}