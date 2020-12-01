package com.example.factslistapplication.facts.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.factslistapplication.R

class HomeActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_layout)

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.factsFrameLayout, FactsListFragment()).commit()
        }
    }
}