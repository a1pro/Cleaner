package com.applications.cleaner.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import com.applications.cleaner.R
import com.applications.cleaner.Shareprefrence.My_Sharepreferences


class Home_Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_home_, container, false)
        val sharedPreferences =  My_Sharepreferences(requireContext())
        val orders =view.findViewById<LinearLayout>(R.id.orders);
        val Name =view.findViewById<TextView>(R.id.Name);

        Name.setText("Hi "+sharedPreferences.getusername() )

        orders.setOnClickListener {

//            val ordersFragment=Orders_Fragment()
//            val transaction = activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.frame_home, ordersFragment,null)?.commit()

//            findNavController().navigate(R.id.action_home_Fragment_to_getdirection_Fragment)

                findNavController().navigate(R.id.action_home_Fragment_to_orders_Fragment)

        }


        return view ;

    }




}