package com.example.uasolshop.crud

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.uasolshop.R
import com.example.uasolshop.dataclass.Products
import com.example.uasolshop.databinding.FragmentTambahDataBinding
import com.example.uasolshop.network.ApiClient
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
//import retrofit2.Callback
import retrofit2.Response
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TambahDataFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TambahDataFragment : Fragment(R.layout.fragment_tambah_data) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentTambahDataBinding? = null
    private val binding get() = _binding!!
    private var selectedImageUri: Uri? = null
    private lateinit var imageFile: File
    private var uploadedImageUrl: String? = null
    private var filePath:String = null.toString();

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
        _binding = FragmentTambahDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        requestStoragePermission()
        with(binding) {
            buttonPickImage.setOnClickListener { pickImage() }
            btnTambah.setOnClickListener { submitProduk() }
            back.setOnClickListener{
                Log.d("hihiihi", "aaaa")
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun submitProduk() {
        val namaProduk = binding.etNamaProduk.text.toString()
        val harga = binding.etHarga.text.toString()
        val stok = binding.etStok.text.toString()
        val keterangan = binding.etKet.text.toString()

        // Ambil kategori dari radio button
        val kategori = when(binding.radioGroupkategori.checkedRadioButtonId) {
            R.id.radio_peralatan -> "Peralatan"
            R.id.radio_dekorasi -> "Dekorasi"
            R.id.radio_lainnya -> "Lainnya"
            else -> "Lainnya"
        }

        if (validateInput(namaProduk, harga, stok, kategori)) {
            uploadImageAndSubmitProduk(
                namaProduk,
                kategori,
                harga,
                stok,
                keterangan,
                filePath
            )
        }
    }

    private fun validateInput(
        namaProduk: String,
        harga: String,
        stok: String,
        kategori: String
    ): Boolean {
        return when {
            namaProduk.isEmpty() -> {
                Toast.makeText(requireContext(), "Nama produk harus diisi", Toast.LENGTH_SHORT).show()
                false
            }
            harga.isEmpty() -> {
                Toast.makeText(requireContext(), "Harga harus diisi", Toast.LENGTH_SHORT).show()
                false
            }
            stok.isEmpty() -> {
                Toast.makeText(requireContext(), "Stok harus diisi", Toast.LENGTH_SHORT).show()
                false
            }
            kategori.isEmpty() -> {
                Toast.makeText(requireContext(), "Kategori harus dipilih", Toast.LENGTH_SHORT).show()
                false
            }
            filePath == null -> {
                Toast.makeText(requireContext(), "Gambar harus dipilih", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun uploadImageAndSubmitProduk(
        namaProduk: String,
        kategori: String,
        harga: String,
        stok: String,
        deskripsiBrang: String,
        filePath : String
    ) {
        // Log all input values before sending
        Log.d("ProductUpload", "Nama Produk: $namaProduk, Tipe Data: ${namaProduk.javaClass.simpleName}")
        Log.d("ProductUpload", "Kategori: $kategori, Tipe Data: ${kategori.javaClass.simpleName}")
        Log.d("ProductUpload", "Harga: $harga, Tipe Data: ${harga.javaClass.simpleName}")
        Log.d("ProductUpload", "Stok: $stok, Tipe Data: ${stok.javaClass.simpleName}")
        Log.d("ProductUpload", "Keterangan: $deskripsiBrang, Tipe Data: ${deskripsiBrang.javaClass.simpleName}")
        Log.d("ProductUpload", "Foto: $filePath, Tipe Data: ${filePath.javaClass.simpleName}")

        val jsonData = Gson().toJsonTree(
            mapOf(
                "namaProduk" to namaProduk,
                "kategori" to kategori,
                "harga" to harga,
                "stok" to stok,
                "deskripsiBarang" to deskripsiBrang,
                "fotoBarang" to filePath
            )
        ).toString()
//
        val requestBody = jsonData.toRequestBody("application/json".toMediaType())
//
        ApiClient.getInstance().uploadProduk(requestBody).enqueue(object : Callback<Products> {
            override fun onResponse(call: Call<Products>, response: Response<Products>) {
                if (response.isSuccessful) {
                    // Tangani jika data berhasil dikirimkan
                    Toast.makeText(
                        requireContext(),
                        "Data added successfully",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    println("Data berhasil dikirim: ${response.body()}")
                    parentFragmentManager.popBackStack()
                } else {
                    // Tangani jika ada error
                    println("Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Products>, t: Throwable) {
                // Tangani jika terjadi kesalahan jaringan atau lainnya
                println("Koneksi gagal: ${t.message}")
            }
        })
    }

    private fun pickImage() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        galleryLauncher.launch(intent)
    }
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            result.data?.data?.let { uri ->
                binding.imageUploadImg.setImageURI(uri)
//                binding.defaultTextTxt.visibility = View.GONE
                binding.imageUploadImg.visibility = View.VISIBLE

                val fileName = getFileName(uri)
                binding.filenameTxt.visibility = View.VISIBLE
                binding.filenameTxt.text = fileName

                filePath = getRealPathFromURI(requireContext(), uri) ?: "default_path"
                // Gunakan filePath sesuai kebutuhan
            }
        }

    }
    private fun getFileName(uri: Uri): String {
        var name = ""
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (columnIndex != -1) {
                    name = it.getString(columnIndex)
                }
            }
        }
        return name
    }

    private fun getRealPathFromURI(context: Context, uri: Uri): String? {
        var path: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                path = it.getString(columnIndex)
            }
        }
        return path
    }


    //    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
//            data?.data?.let { uri ->
//                selectedImageUri = uri
//                val filePath = getRealPathFromURI(uri)
//                imageFile = File(filePath)
//
//                Glide.with(requireContext())
//                    .load(uri)
//                    .into(binding.imagePreview)
//            }
//        }
//    }
//    private fun getRealPathFromURI(uri: Uri): String {
//        // Check different methods to get actual file path
//        val projection = arrayOf(MediaStore.Images.Media.DATA)
//        requireContext().contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
//            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//            cursor.moveToFirst()
//            return cursor.getString(columnIndex)
//        }
//        return uri.path ?: ""
//    }
//override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//    super.onActivityResult(requestCode, resultCode, data)
//    if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
//        data?.data?.let { uri ->
//            try {
//                // Use ContentResolver to get a more reliable file path
//                val filePath = getFilePathFromContentUri(uri)
//
//                if (filePath.isNotEmpty()) {
//                    imageFile = File(filePath)
//
//                    // Validate file exists and is readable
//                    if (imageFile!!.exists() && imageFile!!.canRead()) {
//                        // Load image preview using Glide
//                        Glide.with(requireContext())
//                            .load(uri)
//                            .into(binding.imageUploadImg)
//
//                        Log.d("ImageSelection", "Selected image path: $filePath")
//                    } else {
//                        Toast.makeText(
//                            requireContext(),
//                            "Cannot read the selected image",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                } else {
//                    Toast.makeText(
//                        requireContext(),
//                        "Could not retrieve image path",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            } catch (e: Exception) {
//                Log.e("ImageSelection", "Error processing image", e)
//                Toast.makeText(
//                    requireContext(),
//                    "Error selecting image: ${e.localizedMessage}",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }
//}
//
//    private fun getFilePathFromContentUri(uri: Uri): String {
//        val projection = arrayOf(MediaStore.Images.Media.DATA)
//        val cursor = requireContext().contentResolver.query(
//            uri,
//            projection,
//            null,
//            null,
//            null
//        )
//
//        return cursor?.use {
//            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//            it.moveToFirst()
//            it.getString(columnIndex)
//        } ?: ""
//    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TambahDataFragment.
         */
        private const val REQUEST_IMAGE_PICK = 100
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TambahDataFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}