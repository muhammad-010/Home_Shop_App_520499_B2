package com.example.uasolshop.HistoryAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uasolshop.database.History
import com.example.uasolshop.databinding.ItemHistoryBinding

class HistoryAdapter(val listbookhistory : List<History>):RecyclerView.Adapter<HistoryAdapter.ItemBookViewHolder>() {
    inner class ItemBookViewHolder
(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: History) {
            with(binding) {
                tanggalBook.text = data.date
                namaproduct1.text = data.namaProduk
                hargaproduct1.text = data.harga.toString()
                kategoriproduct1.text = data.kategori
                Log.d("home", data.banyak_book.toString())
                banyakbook2.text = data.banyak_book.toString()

                Glide.with(itemView.context)
                    .load(data.fotoBarang) // pastikan `data.imageUrl` adalah URL gambar yang valid
                    .into(fotoProduk)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemBookViewHolder {
        val binding =
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context), parent,
                false)
        return ItemBookViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listbookhistory.size
    }

    override fun onBindViewHolder(holder: ItemBookViewHolder, position: Int) {
        holder.bind(listbookhistory[position])
    }
//    fun removeItem(position: Int) {
//        listbookhistory.removeAt(position)
//        notifyItemRemoved(position)
//        notifyItemRangeChanged(position,listbookhistory.size)
//    }
//    fun updateData(newProducts: List<History>) {
//        Log.d("AdapterUpdate", "Incoming data size: ${newProducts.size}")
//        Log.d("AdapterUpdate", "Incoming data: $newProducts")
//        listbookhistory.clear()
//        listbookhistory.addAll(newProducts)
//        Log.d("AdapterUpdate", "Data size after update: ${listbookhistory.size}")
//        notifyDataSetChanged()
//    }
}