package com.example.uasolshop.listproduct

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import com.example.uasolshop.R
import com.example.uasolshop.crud.DetailDataFragment
import com.example.uasolshop.crud.EditDataFragment
import com.example.uasolshop.dataclass.Products
import com.example.uasolshop.productAdapter.ProductAdapter
import com.example.uasolshop.databinding.FragmentAllBinding
import com.example.uasolshop.loading.LoadingFragment
import com.example.uasolshop.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ref.WeakReference

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AllFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllFragment(parentFragment: FragmentManager) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentAllBinding? =null
    private val binding get() = _binding!!
    private val productList = ArrayList<Products>() // Gunakan ArrayList


//    private lateinit var productAdapter: ProductAdapter

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
        // Inflate the layout for this fragment
        _binding = FragmentAllBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchProducts(binding)
    }

    private fun fetchProducts(binding: FragmentAllBinding) {
        val apiService = ApiClient.getInstance()

        apiService.getAllProducts().enqueue(object : Callback<List<Products>> {
            override fun onResponse(
                call: Call<List<Products>>,
                response: Response<List<Products>>
            ) {
                if (response.isSuccessful) {
                    binding.textLoading.visibility = View.GONE;
                    val products = response.body()

                    if (!products.isNullOrEmpty()) {
                        // Clear existing list before adding new products
                        productList.clear()

                        productList.addAll(products.reversed())

                        Log.d("api ini", "body: $productList")

                        // Prepare adapter with click listener
                        val adapterRetrofit =
                            ProductAdapter(productList, onEditProduct = { product ->
                                try {
                                    // Create EditDataFragment
                                    val editDataFragment = EditDataFragment.newInstance(
                                        id = product.id,
                                        namaProduk = product.namaProduk,
                                        kategori = product.kategori,
                                        harga = product.harga,
                                        stok = product.stok,
                                        deskripsiBarang = product.deskripsiBarang,
                                        fotoBarang = product.fotoBarang
                                    )

                                    // Perform fragment transaction
                                    parentFragment?.parentFragmentManager?.beginTransaction()
                                        ?.replace(R.id.fragment_container, editDataFragment)
                                        ?.addToBackStack(null)
                                        ?.commit()

                                    Log.d(
                                        "Navigation",
                                        "Navigating to EditDataFragment for product: ${product.namaProduk}"
                                    )
                                } catch (e: Exception) {
                                    Log.e(
                                        "Navigation Error",
                                        "Failed to navigate to EditDataFragment",
                                        e
                                    )
                                    Toast.makeText(
                                        requireContext(),
                                        "Error navigating to edit page",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }, onClickProduk = { product ->
                                val detaildataFragment = DetailDataFragment.newInstance(
                                    id = product.id,
                                    namaProduk = product.namaProduk,
                                    kategori = product.kategori,
                                    harga = product.harga,
                                    stok = product.stok,
                                    deskripsiBarang = product.deskripsiBarang,
                                    fotoBarang = product.fotoBarang
                                )
                                // Use parentFragmentManager to replace the fragment
                                parentFragment?.parentFragmentManager?.beginTransaction()
                                    ?.replace(R.id.fragment_container, detaildataFragment)
                                    ?.addToBackStack(null)
                                    ?.commit()

                            })
                        binding.recyclerViewtopproduct.apply {
                            layoutManager = GridLayoutManager(context, 2)
                            adapter = adapterRetrofit
                        }

                        Log.d("FetchProducts", "Product list size: ${productList.size}")
                    } else {
                        Log.e("FetchProducts", "Product list is empty")
                        Toast.makeText(
                            requireContext(),
                            "No products found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.e("API Error", "Error response: ${response.errorBody()?.string()}")
                    Toast.makeText(
                        requireContext(),
                        "Failed to fetch products",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onFailure(call: Call<List<Products>>, t: Throwable) {
                        Log.e("Network Error", "Error fetching products: ${t.message}")
                        Toast.makeText(
                            requireContext(),
                            "Network error: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AllFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(parentFragmentManager: FragmentManager,param1: String, param2: String) =
            AllFragment(parentFragmentManager).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}