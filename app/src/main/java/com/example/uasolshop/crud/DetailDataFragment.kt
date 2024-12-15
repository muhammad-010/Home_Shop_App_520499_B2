package com.example.uasolshop.crud

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.uasolshop.R
import com.example.uasolshop.databinding.FragmentDetailDataBinding
import com.example.uasolshop.databinding.FragmentEditDataBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailDataFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailDataFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentDetailDataBinding? =null
    private val binding get() = _binding!!
    private var id: String? = null
    private var namaProduk: String? = null
    private var kategori: String? = null
    private var harga: Int? = null
    private var stok: Int? = null
    private var deskripsiBarang: String? = null
    private var fotoBarang: String? = null

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
        _binding = FragmentDetailDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            btnBack.setOnClickListener{
                Log.d("hihiihi", "aaaa")
                parentFragmentManager.popBackStack()
            }
            tvNamaproduk.setText(namaProduk)
            tvHarga.setText(harga.toString())
            tvStok.setText(stok.toString())
            tvKet.setText(deskripsiBarang)
            // Query image in MediaStore
            Glide.with(requireActivity())
                .load(fotoBarang)
                .into(imageUploadImg)
            imageUploadImg.visibility = View.VISIBLE


            when (kategori) {
                "Dekorasi" -> tvKategori.text = "Dekorasi"
                "Peralatan" -> tvKategori.text = "Peralatan"
                "Lainnya" -> tvKategori.text = "Lainnya"
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
         * @return A new instance of fragment DetailDataFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(id: String, namaProduk: String, kategori: String, harga: Int, stok: Int, deskripsiBarang: String, fotoBarang: String) =
            DetailDataFragment().apply {
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
    }
}