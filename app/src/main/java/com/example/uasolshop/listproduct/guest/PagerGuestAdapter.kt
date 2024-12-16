package com.example.uasolshop.listproduct.guest

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerGuestAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllGuestFragment()
            1 -> DekorasiGuestFragment()
            2 -> PeralatanGuestFragment()
            else -> LainnyaGuestFragment()
        }
    }
}