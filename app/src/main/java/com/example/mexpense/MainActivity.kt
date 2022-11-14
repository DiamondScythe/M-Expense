package com.example.mexpense

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mexpense.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //sets the support action bar
        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val botNavView: BottomNavigationView = binding.bottomNavigation.bottomNavigation

        //sets up the navHostFragment and navController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Passing each menu ID as a set of Ids because each
        // menu should be considered top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.selectionFragment, R.id.enterTripFragment, R.id.viewDataFragment, R.id.settingsFragment
            ), drawerLayout
        )

        //setup UI components with navController
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        botNavView.setupWithNavController(navController)

        //this block of code is used to dynamically hide the bottom navigation bar
        //depending on the current fragment the user is on
        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            if (nd.id == R.id.selectionFragment || nd.id == R.id.enterTripFragment
                || nd.id == R.id.viewDataFragment || nd.id == R.id.settingsFragment) {
                botNavView.visibility = View.VISIBLE
            } else {
                botNavView.visibility = View.GONE
            }
        }
    }

    //If drawer is already open, back button press will close the drawer instead
    override fun onBackPressed() {
        val layout = binding.drawerLayout
        if (layout.isDrawerOpen(GravityCompat.START)) {
            layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    //sets up the up button
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}