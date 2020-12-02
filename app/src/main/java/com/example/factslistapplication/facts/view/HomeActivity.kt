package com.example.factslistapplication.facts.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.example.factslistapplication.R
import com.example.factslistapplication.facts.model.Row

class HomeActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var navController: NavController
    private lateinit var navGraph: NavGraph

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_layout)

        val destination = R.id.factsListFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainNavigationFragment) as NavHostFragment

        val graphInflater = navHostFragment.navController.navInflater
        navGraph = graphInflater.inflate(R.navigation.system_nav_graph)
        navController = navHostFragment.navController
        navGraph.startDestination = destination
        navController.graph = navGraph
    }

    override fun onItemClick(row: Row) {
        val destination = R.id.factsDetailsFragment
        val bundle = bundleOf(EXTRA_ROW_DATA to row.imageHref)
        navController.navigate(destination, bundle)
    }

    companion object {
        var EXTRA_ROW_DATA = "EXTRA_ROW_DATA"
    }
}