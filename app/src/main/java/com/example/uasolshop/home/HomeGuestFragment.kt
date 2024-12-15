package com.example.uasolshop.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.uasolshop.mainactivity.MainActivityGuest
import com.example.uasolshop.productAdapter.ProductGuestAdapter
import com.example.uasolshop.R
import com.example.uasolshop.BookingFragment.BookingFragment
import com.example.uasolshop.carousel.CarouselAdapter
import com.example.uasolshop.crud.DetailDataFragment
import com.example.uasolshop.databinding.FragmentHomeGuestBinding
import com.example.uasolshop.dataclass.Products
import com.example.uasolshop.listproduct.guest.ListProdukGuestFragment
import com.example.uasolshop.loading.LoadingFragment
import com.example.uasolshop.network.ApiClient
import com.google.android.material.tabs.TabLayoutMediator
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
 * Use the [HomeGuestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeGuestFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentHomeGuestBinding? = null
    private val binding get() = _binding!!
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var images: List<Int>
    private var currentPage = 0
    val productList = ArrayList<Products>()
    private lateinit var adapterRetrofit: ProductGuestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeGuestBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(binding)
        fetchProducts(binding)
        with(binding){
            tvSeeAll.setOnClickListener {
                navigateToTab(0)
            }
            kategoriall.setOnClickListener{
                navigateToTab(0)
            }
            kategoriDekorasi.setOnClickListener{
                navigateToTab(1)
            }
            kategoriPeralatan.setOnClickListener{
                navigateToTab(2)
            }
            kategorilainnya.setOnClickListener{
                navigateToTab(3)
            }


        }
        // Initialize image data
        images = listOf(
            R.drawable.img1,
            R.drawable.img3,
            R.drawable.img4,
            R.drawable.img5,
            R.drawable.img6
        )

        // Set up ViewPager2 with adapter
        val adapter = CarouselAdapter(images)
        binding.viewPager.adapter = adapter

        // Attach TabLayout with ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()

        // Start Auto-Scroll
        startAutoScroll()
    }

    private fun navigateToTab(tabIndex: Int) {
        val fragment = ListProdukGuestFragment().apply {
            arguments = Bundle().apply {
                putInt("TAB_INDEX", tabIndex)
            }
        }
        Log.d("argumen" , "bodi : ${tabIndex}")
        parentFragmentManager.beginTransaction()
            .replace(R.id.mainGuest, fragment) // Sesuaikan dengan ID container Anda
            .addToBackStack(null)
            .commit()
    }

    private fun setupRecyclerView(binding: FragmentHomeGuestBinding) {
        adapterRetrofit = ProductGuestAdapter(productList, onBookProduk = { product ->
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
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainGuest, bookFragment)
                .addToBackStack(null) // Add to back stack so you can navigate back
                .commit()
            Log.d("FragmentTransaction", "Navigating to BookingFragment")
        },onClickProduk = { product ->
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
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainGuest, detaildataFragment)
                .addToBackStack(null) // Add to back stack so you can navigate back
                .commit()

        })
        binding.recyclerViewtopproduct.apply {
            adapter = adapterRetrofit

            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun fetchProducts(binding: FragmentHomeGuestBinding) {
        val fragmentWeakReference = WeakReference(this)

        val loadingFragment = LoadingFragment()

        // Safely check if the fragment is still attached before showing loading
        fragmentWeakReference.get()?.let { fragment ->
            if (fragment.isAdded && !fragment.isDetached) {
                loadingFragment.show(fragment.parentFragmentManager, "LoadingFragment")
            }
        }
//        binding.progressbar.visibility = View.VISIBLE
        val apiService = ApiClient.getInstance()

        apiService.getAllProducts().enqueue(object : Callback<List<Products>> {
            override fun onResponse(
                call: Call<List<Products>>,
                response: Response<List<Products>>
            ) {
                fragmentWeakReference.get()?.let { fragment ->
                    // Ensure the fragment is still in a valid state
                    if (fragment.isAdded && !fragment.isDetached) {
                        // Dismiss loading fragment
                        val loadingDialogFragment =
                            fragment.parentFragmentManager.findFragmentByTag("LoadingFragment")
                        if (loadingDialogFragment is DialogFragment) {
                            loadingDialogFragment.dismiss()
                        }
                        if (response.isSuccessful) {
                            val products = response.body()
                            Log.d("listproduk", "body : ${products}")
                            if (!products.isNullOrEmpty()) {
                                val reversedProducts = products.reversed()
                                val limitedProducts = reversedProducts.take(6)

                                Log.d("FetchProducts", "Reversed products: $reversedProducts")
                                Log.d("FetchProducts", "Limited products: $limitedProducts")

//                        productList.addAll(limitedProducts)
//                        Log.d("FetchProducts", "Product list after addAll: $productList")

                                adapterRetrofit.updateData(limitedProducts)
                                Log.d(
                                    "FetchProducts",
                                    "Product list after addAllsize1: ${productList.size}"
                                )

//                        binding.recyclerViewtopproduct.adapter = adapterRetrofit
                                Log.d(
                                    "FetchProducts",
                                    "Product list after addAllsize: ${productList.size}"
                                )
                            } else {
                                Log.e("FetchProducts", "Product list is empty")
                            }
                        } else {
                            Log.e("API Error", "Error response: ${response.errorBody()?.string()}")
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Products>>, t: Throwable) {
                fragmentWeakReference.get()?.let { fragment ->
                    // Ensure the fragment is still in a valid state
                    if (fragment.isAdded && !fragment.isDetached) {
                        // Dismiss loading fragment
                        val loadingDialogFragment =
                            fragment.parentFragmentManager.findFragmentByTag("LoadingFragment")
                        if (loadingDialogFragment is DialogFragment) {
                            loadingDialogFragment.dismiss()
                        }
                        Log.e("Network Error", "Error fetching products: ${t.message}")
                    }
                }
            }
        })
    }


    private fun startAutoScroll() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                currentPage = (currentPage + 1) % images.size
                binding.viewPager.currentItem = currentPage
                handler.postDelayed(this, 3000) // Delay 3 seconds
            }
        }, 3000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null) // Stop Auto-Scroll
        _binding = null
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeGuestFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                HomeGuestFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}