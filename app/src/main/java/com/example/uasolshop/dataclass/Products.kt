package com.example.uasolshop.dataclass

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "produkDB")
data class Products(
    @SerializedName("_id")
    val id: String,
    @SerializedName("namaProduk")
    val namaProduk: String,
    @SerializedName("kategori")
    val kategori: String,
    @SerializedName("harga")
    val harga: Int,
    @SerializedName("stok")
    val stok: Int,
    @SerializedName("deskripsiBarang")
    val deskripsiBarang: String,
    @SerializedName("fotoBarang")
    val fotoBarang: String

)
