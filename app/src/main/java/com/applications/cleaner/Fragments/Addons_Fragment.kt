package com.applications.cleaner.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.applications.cleaner.Models.Addon_model
import com.applications.cleaner.R


   class Addons_Fragment : Fragment() {
    private lateinit var list:ArrayList<Addon_model>

    private lateinit var addon_recycler: RecyclerView
    private lateinit var Add_to_order: Button

    private lateinit var  bundle:Bundle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_addons_, container, false)


        list = arrayListOf<Addon_model>()
        addon()

        Add_to_order = view.findViewById(R.id.Add_to_order)
        Add_to_order.setOnClickListener {


            findNavController().navigate(R.id.action_addons_Fragment_to_order_Detail_Fragment,Bundle().apply {
                putString("value","1")
            })



        }


       /* addon_recycler = view.findViewById(R.id.addon_recycler)
        addon_recycler.adapter=Addon_adaptor(list)
        addon_recycler.layoutManager=GridLayoutManager(context,2)*/

        return view
    }

    private fun addon() {
        var model =Addon_model("45")
        list.add(model)
         model =Addon_model("40")
        list.add(model)
         model =Addon_model("55")
        list.add(model)
         model =Addon_model("86")
        list.add(model)
         model =Addon_model("38")
        list.add(model)
         model =Addon_model("92")
        list.add(model)
         model =Addon_model("15")
        list.add(model)
    }

}