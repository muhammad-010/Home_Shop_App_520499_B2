package com.example.uasolshop.listproduct.guest

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.uasolshop.BookingFragment.BookingFragment
import com.example.uasolshop.R
import com.example.uasolshop.crud.DetailDataFragment
import com.example.uasolshop.databinding.FragmentPeralatanGuestBinding
import com.example.uasolshop.dataclass.Products
import com.example.uasolshop.network.ApiClient
import com.example.uasolshop.productAdapter.ProductGuestAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PeralatanGuestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PeralatanGuestFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentPeralatanGuestBinding? =null
    private val binding get() = _binding!!
    private lateinit var adapterRetrofit: ProductGuestAdapter

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
        _binding = FragmentPeralatanGuestBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setupRecyclerView(binding)
        // Initialize Adapter
//        productAdapter = ProductAdapter()
        fetchProducts(binding)
        // Setup RecyclerView
//        with(binding.recyclerViewtopproduct) {
//            layoutManager = GridLayoutManager(context, 2) // Menggunakan GridLayoutManager
//            adapter = productAdapter
//        }
    }



    private fun fetchProducts(binding: FragmentPeralatanGuestBinding) {
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
                                val filteredProducts = products.filter { it.kategori == "Peralatan" }
                                // Tambahkan data ke ArrayList
                                productList.clear()
                                productList.addAll(filteredProducts.reversed())
                                ////                        for (i in products) {
////                            var data = Products(idProduk = i.idProduk, namaProduk   = i.namaProduk, deskripsiBarang = i.deskripsiBarang, harga = i.harga, stok = i.stok, kategori = i.kategori)
////                            productList.add(data)
                                Log.d("api ini Peralatan", "body:{$productList}")
                                adapterRetrofit =
                                    ProductGuestAdapter(productList, onBookProduk = { product ->
                                        val bookFragment = BookingFragment.newInstance(
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
                                            ?.replace(R.id.mainGuest, bookFragment)
                                            ?.addToBackStack(null) // Add to back stack so you can navigate back
                                            ?.commit()
                                        Log.d(
                                            "FragmentTransaction",
                                            "Navigating to BookingFragment"
                                        )
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
                                            ?.replace(R.id.mainGuest, detaildataFragment)
                                            ?.addToBackStack(null)
                                            ?.commit()

                                    })
                                binding.recyclerViewtopproduct.apply {
                                    layoutManager = GridLayoutManager(context, 2)
                                    adapter = adapterRetrofit
                                }
                                // Siapkan adapter dan set ke RecyclerView
                                adapterRetrofit.updateData(filteredProducts)

                                // Log untuk debug
                                Log.d("FetchProducts", "Product list size: ${productList.size}")
                            } else {
                                Log.e("FetchProducts", "Product list is empty")
                            }
                        } else {
                            Log.e("API Error", "Error response: ${response.errorBody()?.string()}")
                        }
                    }

            override fun onFailure(call: Call<List<Products>>, t: Throwable) {

                        Log.e("Network Error", "Error fetching products: ${t.message}")
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
         * @return A new instance of fragment PeralatanGuestFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PeralatanGuestFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}