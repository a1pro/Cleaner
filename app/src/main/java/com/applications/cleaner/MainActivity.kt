package com.applications.cleaner


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    lateinit var bottem_nav: BottomNavigationView
    lateinit var navController: NavController
    lateinit var navhost: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(null)
        bottem_nav = findViewById<BottomNavigationView>(R.id.bottem_nav)
        navController = findNavController(R.id.fragmentContainerView)
        bottem_nav.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(object :
            NavController.OnDestinationChangedListener {
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?
            ) {
                /*if (destination.id == R.id.nav_home) {
                    toolbar.setVisibility(View.GONE)
                } else {
                    toolbar.setVisibility(View.GONE)
                }*/
                destination.id
            }

        })

//        val appbar= AppBarConfiguration(setOf(R.id.home_Fragment,R.id.history_Fragment,R.id.profile_Fragment))
//        setupActionBarWithNavController(navController,appbar)


//        bottem_nav.setOnNavigationItemSelectedListener { it ->
//            when (it.itemId) {
//                R.id.Home_bottem -> replace(homeFragment)
//                R.id.history_bottem -> replace(history_fragment)
//
//                R.id.profile_bottem -> replace(profileFragment)
//
//
//            }
//
//            true
//
//        }


    }


}