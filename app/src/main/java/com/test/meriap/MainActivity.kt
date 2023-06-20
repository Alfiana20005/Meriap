package com.test.meriap

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    switchFragment(HomeFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.action_search -> {
                    switchFragment(SearchFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.action_planner -> {
                    switchFragment(PlannerFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.action_profile -> {
                    switchFragment(AccountFragment())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(
            R.id.fragmentContainer,
            fragment
        ).commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        switchFragment(HomeFragment())
    }
}