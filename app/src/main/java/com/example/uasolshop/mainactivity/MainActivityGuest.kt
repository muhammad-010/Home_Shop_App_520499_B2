package com.example.uasolshop.mainactivity

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.uasolshop.BookingFragment.BookingFragment
import com.example.uasolshop.profile.ProfileFragment
import com.example.uasolshop.R
import com.example.uasolshop.databinding.ActivityMainGuestBinding
import com.example.uasolshop.history.HistoryFragment
import com.example.uasolshop.home.HomeAdminFragment
import com.example.uasolshop.home.HomeGuestFragment
import com.example.uasolshop.listproduct.ListProdukFragment
import com.example.uasolshop.listproduct.guest.ListProdukGuestFragment

class MainActivityGuest : AppCompatActivity() , DatePickerDialog.OnDateSetListener {

    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0
    private lateinit var images: List<Int>
    companion object{
        lateinit var binding: ActivityMainGuestBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainGuestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Set default fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainGuest, HomeGuestFragment())
            .commit()

        // Handle BottomNavigationView navigation
        binding.bottomNavigationViewGuest.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeGuestFragment3 -> {
                    Log.d("home","eror")
                    setFragment(HomeGuestFragment(),keepNavOnHome = true)
                    true
                }
                R.id.listProdukGuestFragment -> {
                    Log.d("hai","eror")

                    setFragment(ListProdukGuestFragment(),keepNavOnHome = true)
                    true
                }
                R.id.historyFragment -> {
                    setFragment(HistoryFragment())
                    true
                }
                R.id.profileFragmentGuest -> {
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
            .replace(R.id.mainGuest, fragment)
            .commit()

        // Update bottom navigation selection
        if (!keepNavOnHome) {
            when (fragment) {
                is HomeGuestFragment -> binding.bottomNavigationViewGuest.selectedItemId =
                    R.id.homeGuestFragment3
                is ListProdukGuestFragment -> binding.bottomNavigationViewGuest.selectedItemId =
                    R.id.listProdukGuestFragment
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        val selectedDate = "$day/${month + 1}/$year"
    }
}