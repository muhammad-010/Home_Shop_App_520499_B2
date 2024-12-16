package com.example.uasolshop.history

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uasolshop.HistoryAdapter.HistoryAdapter
import com.example.uasolshop.auth.PrefManager
import com.example.uasolshop.database.History
import com.example.uasolshop.database.HistoryDao
import com.example.uasolshop.database.HistoryRoomDatabase
import com.example.uasolshop.databinding.FragmentHistoryBinding
import com.example.uasolshop.databinding.FragmentHomeBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var executorService: ExecutorService
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var mHistoriDao: HistoryDao
    private var updateId: Int = 0
    val productList = ArrayList<History>()
    private lateinit var adapterGuestproduct: HistoryAdapter
    lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefManager = PrefManager.getInstance(requireContext())
        // Inisialisasi ExecutorService
        executorService = Executors.newSingleThreadExecutor()

        // Aktifkan database
        val db = HistoryRoomDatabase.getDatabase(requireContext())
        mHistoriDao = db?.historiDao() ?: return // Exit if database or DAO is null
        adapterGuestproduct = HistoryAdapter(productList)
        with(binding) {
            recyclerViewproduct.apply {
                adapter = adapterGuestproduct
                layoutManager = GridLayoutManager(requireContext(), 2) // Grid 2 kolom
            }
        }
        getAllNotes()

    }

    private fun getAllNotes() {
        with(binding) {
            binding.recyclerViewproduct.layoutManager = LinearLayoutManager(requireContext())
            mHistoriDao.getHistoriesByUsername(prefManager.getUsername()).observe(viewLifecycleOwner) { produk ->
                try {
                    productList.clear()
                    productList.addAll(produk.reversed())
                    adapterGuestproduct.notifyDataSetChanged()
                } catch (e: Exception) {
                    // Log the error or show a user-friendly message
                    Log.e("HistoryFragment", "Error updating product list", e)
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        getAllNotes()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}