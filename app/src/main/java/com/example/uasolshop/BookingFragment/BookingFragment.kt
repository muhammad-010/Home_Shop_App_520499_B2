package com.example.uasolshop.BookingFragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.uasolshop.R
import com.example.uasolshop.auth.PrefManager
import com.example.uasolshop.database.History
import com.example.uasolshop.database.HistoryDao
import com.example.uasolshop.database.HistoryRoomDatabase
import com.example.uasolshop.databinding.FragmentBookingBinding
import com.example.uasolshop.dataclass.Products
import com.example.uasolshop.history.HistoryFragment
import com.example.uasolshop.network.ApiClient
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import java.util.Calendar
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BookingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookingFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentBookingBinding? = null
    private lateinit var executorService: ExecutorService
    private val binding get() = _binding!!
    private lateinit var historyDao: HistoryDao

    private var filePath:String = null.toString();
    private var historyId: Int = 0
    private var id: String? = null
    private var namaProduk: String? = null
    private var kategori: String? = null
    private var harga: Int? = null
    private var stok: Int? = null
    private var deskripsiBarang: String? = null
    private var fotoBarang: String? = null
    lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString("id")
            namaProduk = it.getString("namaProduk")
            kategori = it.getString("kategori")
            harga = it.getInt("harga")
            stok = it.getInt("stok")
            deskripsiBarang= it.getString("deskripsiBarang")
            fotoBarang = it.getString("fotoBarang")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using binding
        _binding = FragmentBookingBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefManager = PrefManager.getInstance(requireContext())
        executorService = Executors.newSingleThreadExecutor()

        // Aktifkan database
        val db = HistoryRoomDatabase.getDatabase(requireContext())

        historyDao = db!!.historiDao()!!
//        historyId = id
        // Hubungkan ke NoteDao untuk menggunakan method
        with(binding){
            back.setOnClickListener{
                Log.d("hihiihi", "aaaa")
                parentFragmentManager.popBackStack()
            }
            btnTglBooking.setOnClickListener {
                val datePicker = DatePicker(this@BookingFragment)
                datePicker.show(parentFragmentManager, "datePicker")
            }

            btnSimpanBook.setOnClickListener{
                BookProduk()
            }
        }

    }

//    private fun submitProduk() {
//        val booking = binding.btnTglBooking.text.toString()
//        if (validateInput(booking)) {
////            Log.d("img", "bodi1:  ${filePath}")
////            Log.d("img", "bodi2:  ${fotoBarang   }")
////            val imageToUpload = if (filePath == "null" || filePath == "default_path") fotoBarang else filePath
////            Log.d("img", "imageToUpload: $imageToUpload")
//
////            Log.d("img", "bodi:  ${imageToUpload }")
////            if (imageToUpload != null) {
//                BookProduk()
////            }
//        }else{
//            Toast.makeText(requireContext(), "Isi semua field terlebih dahulu", Toast.LENGTH_SHORT).show()
//        }
//    }

    private fun validateInput( tglBook: String, banyakBook: String): Boolean {
        return when {
            tglBook.isEmpty() -> {
                Toast.makeText(requireContext(), "Tanggal Booking harus diisi", Toast.LENGTH_SHORT)
                    .show()
                false
            }banyakBook.isEmpty() -> {
                Toast.makeText(requireContext(), "Tanggal Booking harus diisi", Toast.LENGTH_SHORT)
                    .show()
                false
            }
            else -> true
        }

    }

    private fun BookProduk(
//        namaProduk: String,
//        kategori: String,
//        harga: String,
//        stok: String,
//        deskripsiBrang: String,
//        filePath : String,
//        tglBook: String
    ) {
        // Log all input values before sending

        val booking = binding.btnTglBooking.text.toString()
        val banyakBook = binding.etBanyakBook.text.toString()
        Log.d("tanggal", "book : ${booking}")
        Log.d("ProductUpload", "Nama Produk: $namaProduk, Tipe Data: ${namaProduk.toString().javaClass.simpleName}")
        Log.d("ProductUpload", "Kategori: $kategori, Tipe Data: ${kategori.toString().javaClass.simpleName}")
        Log.d("ProductUpload", "Harga: $harga, Tipe Data: ${harga.toString().javaClass.simpleName}")
//        Log.d("ProductUpload", "Stok: $stok, Tipe Data: ${stok.toString().javaClass.simpleName}")
        Log.d("ProductUpload", "Keterangan: $deskripsiBarang, Tipe Data: ${deskripsiBarang.toString().javaClass.simpleName}")
        Log.d("ProductUpload", "Foto: $fotoBarang, Tipe Data: ${fotoBarang.toString().javaClass.simpleName}")
        if (validateInput(booking, banyakBook)) {
            // Ensure that stock and number to book are valid integers
            val currentStock = stok ?:0
            val bookAmount = banyakBook.toIntOrNull() ?: 0

            if (currentStock < bookAmount) {
                Toast.makeText(requireContext(), "Stok tidak cukup", Toast.LENGTH_SHORT).show()
                return
            }
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val stokAkhir = currentStock-bookAmount
                    val jsonData = Gson().toJsonTree(
                        mapOf(
                            "namaProduk" to namaProduk,
                            "kategori" to kategori,
                            "harga" to harga,
                            "stok" to stokAkhir,
                            "deskripsiBarang" to deskripsiBarang,
                            "fotoBarang" to fotoBarang
                        )
                    ).toString()
                    val requestBody = jsonData.toRequestBody("application/json".toMediaType())
                    Log.d("ProductUpload", "book: $id, Tipe Data: ${id.toString().javaClass.simpleName}")

                    decreaseProductStock(id.toString(), requestBody)
//                    historyDao.decreaseStok(historyId.toString())
                    Log.d("ProductUpload", "Stok decrease: $stokAkhir, Tipe Data: ${stok.toString().javaClass.simpleName}")
                    Log.d("ProductUpload", "current: $currentStock, Tipe Data: ${currentStock.toString().javaClass.simpleName}")
                    Log.d("ProductUpload", "book: $bookAmount, Tipe Data: ${bookAmount.toString().javaClass.simpleName}")

                    historyDao.insert(
                        History(
                            id = historyId,
                            namaProduk = namaProduk.toString(),
                            kategori = kategori.toString(),
                            harga = harga ?: 0,
                            banyak_book = bookAmount,
                            deskripsiBarang = deskripsiBarang.toString(),
                            fotoBarang = fotoBarang.toString(),
                            date = booking,
                            username = prefManager.getUsername()
                        )
                    )
                    withContext(Dispatchers.Main) {

                            val safeContext = context ?: return@withContext
                            Toast.makeText(
                                safeContext,
                                "Data booked successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.mainGuest, HistoryFragment())
                            .addToBackStack(null) // Menambahkan ke backstack
                            .commit()

                    }



                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.d("ProductUpload", "Error booking product: ${e.message}")
                        Toast.makeText(
                            requireContext(),
                            "Error booking product: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }else{
            Toast.makeText(requireContext(), "Isi semua field terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun decreaseProductStock(productId: String, requestBody: RequestBody) {
        val apiService = ApiClient.getInstance()

        // Panggil API untuk mengurangi stok
        apiService.updateProduk(productId, requestBody).enqueue(object : Callback<Products> {
            override fun onResponse(call: Call<Products>, response: retrofit2.Response<Products>) {
                Log.d("stokkk", response.body()?.stok.toString())
                if (response.isSuccessful) {
                    // Berhasil mengupdate stok
                    Toast.makeText(requireContext(), "Stok berhasil dikurangi!", Toast.LENGTH_SHORT).show()
                } else {
                    // Tampilkan pesan kesalahan
                    Toast.makeText(requireContext(), "Gagal mengurangi stok: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Products>, t: Throwable) {
                // Tampilkan pesan error jika koneksi gagal
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    class DatePicker(private val listener: DatePickerDialog.OnDateSetListener) : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val monthOfYear = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            return DatePickerDialog(
                requireActivity(),
                listener, // Use the passed listener
                year,
                monthOfYear,
                dayOfMonth
            )
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BookingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(id: String, namaProduk: String, kategori: String, harga: Int, stok: Int, deskripsiBarang: String, fotoBarang: String) =
            BookingFragment().apply {
                arguments = Bundle().apply {
                    putString("id", id)
                    putString("namaProduk", namaProduk)
                    putString("kategori", kategori)
                    putInt("harga", harga)
                    putInt("stok", stok)
                    putString("deskripsiBarang", deskripsiBarang)
                    putString("fotoBarang", fotoBarang)
                }
            }
        var tanggal = ""
    }

    override fun onDateSet(view: android.widget.DatePicker?, year: Int, month: Int, day: Int) {
        val selectedDate = "$day/${month + 1}/$year"
        Log.d("BookingFragment", "Date selected: $selectedDate")
        binding.btnTglBooking.text = selectedDate
        binding.btnTglBooking.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        tanggal = selectedDate
    }
}

