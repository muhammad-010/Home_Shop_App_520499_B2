package com.example.uasolshop.mainactivity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.uasolshop.profile.ProfileFragment
import com.example.uasolshop.R
import com.example.uasolshop.databinding.ActivityMainAdminBinding
import com.example.uasolshop.history.HistoryFragment
import com.example.uasolshop.home.HomeAdminFragment
import com.example.uasolshop.listproduct.ListProdukFragment

class MainActivityAdmin : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0
    private lateinit var images: List<Int>
    companion object{
        lateinit var binding: ActivityMainAdminBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Set default fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeAdminFragment())
            .commit()

        // Handle BottomNavigationView navigation
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeAdminFragment -> {
                    setFragment(HomeAdminFragment(),keepNavOnHome = true)
                    true
                }
                R.id.listProdukFragment3 -> {
                    Log.d("hai","eror")

                    setFragment(ListProdukFragment(),keepNavOnHome = true)
                    true
                }
                R.id.profileFragment -> {
                    setFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }
    fun setFragment(fragment: Fragment, keepNavOnHome: Boolean = false) {
        Log.d("FragmentTransaction", "Replacing with fragment: ${fragment::class.java.simpleName}")

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()


        // Update bottom navigation selection
        if (!keepNavOnHome) {
            when (fragment) {
                is HomeAdminFragment -> binding.bottomNavigationView.selectedItemId =
                    R.id.homeAdminFragment
                is ListProdukFragment -> binding.bottomNavigationView.selectedItemId =
                    R.id.listProdukFragment3
            }
        }
    }

}
