package com.example.uasolshop.productAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uasolshop.databinding.ItemProductsGuestBinding
import com.example.uasolshop.dataclass.Products

class ProductGuestAdapter(val listproduk : ArrayList<Products>,private val onBookProduk: (Products) -> Unit , private val onClickProduk: (Products) -> Unit):RecyclerView.Adapter<ProductGuestAdapter.ItemProductGuestViewHolder>() {
    inner class ItemProductGuestViewHolder(private val binding: ItemProductsGuestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Products) {
            with(binding) {
                namaproduct1.text = data.namaProduk
                hargaproduct1.text = data.harga.toString()
                kategoriproduct1.text = data.kategori
                stokproduct1.text = data.stok.toString()
                Log.d("home", data.fotoBarang.toString())
                Glide.with(itemView.context)
                    .load(data.fotoBarang) // pastikan `data.imageUrl` adalah URL gambar yang valid
                    .into(fotoProduk)

                btnBook.setOnClickListener {
                    Log.d("id",data.id)
                    // Trigger the lambda to handle navigation
                    onBookProduk(data)
                }

                itemView.setOnClickListener{
                    onClickProduk(data)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemProductGuestViewHolder {
        val binding =
            ItemProductsGuestBinding.inflate(
                LayoutInflater.from(parent.context), parent,
                false)
        return ItemProductGuestViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listproduk.size
    }

    override fun onBindViewHolder(holder: ItemProductGuestViewHolder, position: Int) {
        holder.bind(listproduk[position])
    }
    fun removeItem(position: Int) {
        listproduk.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position,listproduk.size)
    }
    fun updateData(newProducts: List<Products>) {
        Log.d("AdapterUpdate", "Incoming data size: ${newProducts.size}")
        Log.d("AdapterUpdate", "Incoming data: $newProducts")

        listproduk.clear()
        listproduk.addAll(newProducts)
        Log.d("AdapterUpdate", "Data size after update: ${listproduk.size}")
        notifyDataSetChanged()
    }
}