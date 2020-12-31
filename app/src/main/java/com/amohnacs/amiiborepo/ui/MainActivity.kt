package com.amohnacs.amiiborepo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.amohnacs.amiiborepo.R
import com.amohnacs.amiiborepo.databinding.ActivityMainBinding
import com.amohnacs.amiiborepo.ui.details.DetailsFragmentDirections

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.apply {
            setSupportActionBar(this)
            setupNavigationUI(this)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val actions = DetailsFragmentDirections.actionDetailsFragmentToMainFragment()
        getNavController().navigate(actions)
        return super.onSupportNavigateUp()
    }

    private fun setupNavigationUI(toolbar: Toolbar) {
        val navController = getNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun getNavController(): NavController {
        val navHostFrag = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFrag.navController
    }
}