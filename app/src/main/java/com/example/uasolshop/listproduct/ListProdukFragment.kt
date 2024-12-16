package com.example.uasolshop.listproduct

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.uasolshop.home.HomeAdminFragment
import com.example.uasolshop.R
import com.example.uasolshop.crud.TambahDataFragment
import com.example.uasolshop.databinding.FragmentListProdukBinding
import com.google.android.material.tabs.TabLayoutMediator

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListProdukFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListProdukFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentListProdukBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListProdukBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)


        with(binding) {
            tambahProduk.setOnClickListener {
                val tambahDataFragment = TambahDataFragment()

                // Lakukan fragment transaction
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, tambahDataFragment) // Ganti dengan fragment container ID yang sesuai
                    .addToBackStack(null) // Tambahkan ke backstack agar bisa kembali
                    .commit()
            }

            back.setOnClickListener {
                if (parentFragmentManager.backStackEntryCount > 0) {
                    // Jika ada fragment sebelumnya di back stack
                    parentFragmentManager.popBackStack()
                } else {
                    // Jika tidak ada fragment di back stack
                    Toast.makeText(requireContext(), "No previous fragment to go back to", Toast.LENGTH_SHORT).show()
                }
            }

            val tabIndex = arguments?.getInt("TAB_INDEX", 0) ?: 0

            // Initialize TabLayout and ViewPager
            val pagerAdapter = PagerAdapter(this@ListProdukFragment, parentFragmentManager) // Replace with actual FragmentPagerAdapter
            viewPager.adapter = pagerAdapter

                                                           // Set up TabLayout with ViewPager
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = "Semua"
                        tab.setIcon(R.drawable.baseline_grid_view_24)
                    }
                    1 -> {
                        tab.text = "Dekorasi"
                        tab.setIcon(R.drawable.cement__1_)
                    }
                    2 -> {
                        tab.text = "Peralatan"
                        tab.setIcon(R.drawable.beam)
                    }
                    3 -> {
                        tab.text = "Lainnya"
                        tab.setIcon(R.drawable.baseline_more_horiz_24)
                    }
                }
            }.attach()

            viewPager.post {
                viewPager.currentItem = tabIndex
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListProdukFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListProdukFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}