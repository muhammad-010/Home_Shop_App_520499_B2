package com.example.uasolshop.listproduct.guest

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerGuestAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 4 // There are 4 tabs

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllGuestFragment()  // Tab All
            1 -> DekorasiGuestFragment()  // Tab Semen
            2 -> PeralatanGuestFragment()  // Tab Besi
            else -> LainnyaGuestFragment()  // Tab Lainnya
        }
    }
}