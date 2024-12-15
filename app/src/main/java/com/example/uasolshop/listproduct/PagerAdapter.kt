package com.example.uasolshop.listproduct

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(fragment: Fragment, val parentFragment: FragmentManager) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 4 // There are 4 tabs

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllFragment(parentFragment)  // Tab All
            1 -> DekorasiFragment(parentFragment)  // Tab Semen
            2 -> PeralatanFragment(parentFragment)  // Tab Besi
            else -> LainnyaFragment(parentFragment)  // Tab Lainnya
        }
    }
}