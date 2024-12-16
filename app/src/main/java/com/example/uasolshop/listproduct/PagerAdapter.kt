package com.example.uasolshop.listproduct

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(fragment: Fragment, val parentFragment: FragmentManager) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllFragment(parentFragment)
            1 -> DekorasiFragment(parentFragment)
            2 -> PeralatanFragment(parentFragment)
            else -> LainnyaFragment(parentFragment)
        }
    }
}