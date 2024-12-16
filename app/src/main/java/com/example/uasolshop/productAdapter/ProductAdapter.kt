package com.example.uasolshop.productAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uasolshop.dataclass.Products
import com.example.uasolshop.databinding.ItemProductsBinding
import com.example.uasolshop.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
//private val client: ApiService
//private val onDeleteClick: (Int, Int) -> Unit
//typealias OnClickProduct = (Products) -> Unit
class ProductAdapter(val listproduk : ArrayList<Products>, private val onEditProduct: (Products) -> Unit, private val onClickProduk: (Products) -> Unit):RecyclerView.Adapter<ProductAdapter.ItemProductViewHolder>() {
    inner class ItemProductViewHolder(val binding: ItemProductsBinding) :
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

                edit.setOnClickListener {
                    Log.d("id",data.id)
                    // Trigger the lambda to handle navigation
                    onEditProduct(data)
                }

                itemView.setOnClickListener{
                    onClickProduk(data)
                }
//                edit.setOnClickListener{
//                    val intentToEdit = Intent(itemView.context, EditDataFragment::class.java)
//                    intentToEdit.putExtra("id", data.id)
//                    intentToEdit.putExtra("namaProduk", data.namaProduk)
//                    intentToEdit.putExtra("kategori", data.kategori)
//                    intentToEdit.putExtra("harga", data.harga)
//                    intentToEdit.putExtra("stok", data.stok)
//                    intentToEdit.putExtra("deskripsiBarang", data.deskripsiBarang)
//                    intentToEdit.putExtra("fotoBarang", data.fotoBarang)
//                    onEditProduct(intentToEdit)
//                }
                // Tombol hapus
//                delete.setOnClickListener {
//                    onDeleteClick(data.idProduk, position)
//                }
                delete.setOnClickListener{
                    println("ii")
                    Log.d("eror ini 4", "body : ")
                    deleteItem(data= data,itemView,adapterPosition)
                }

            }

        }
    }

    private fun deleteItem(data: Products, itemView: View, adapterPosition: Int) {
        // Additional null and empty checks
        if (data.id.toString().isNullOrBlank() || data.id == "0") {
            Log.e("DELETE ERROR", "Invalid product ID: ${data.namaProduk}")
            Toast.makeText(itemView.context, "Cannot delete product: Invalid ID", Toast.LENGTH_SHORT).show()
            return
        }

        val client = ApiClient.getInstance()
        val response = client.deleteProduct(data.id)

        response.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("DELETE SUCCESS", "Product deleted: ${data.namaProduk}")
                    Toast.makeText(itemView.context, "Produk telah dihapus", Toast.LENGTH_SHORT).show()
                    removeItem(adapterPosition)
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API ERROR", "Error deleting product: $errorMessage")
                    Toast.makeText(itemView.context, "Gagal menghapus produk", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("NETWORK ERROR", "Connection error", t)
                Toast.makeText(itemView.context, "Koneksi error", Toast.LENGTH_LONG).show()
            }
        })
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemProductViewHolder {
        val binding =
            ItemProductsBinding.inflate(
                LayoutInflater.from(parent.context), parent,
                false)
        return ItemProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listproduk.size
    }

    override fun onBindViewHolder(holder: ItemProductViewHolder, position: Int) {
        holder.bind(listproduk[position])
    }
    // Helper method to remove an item from the list
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