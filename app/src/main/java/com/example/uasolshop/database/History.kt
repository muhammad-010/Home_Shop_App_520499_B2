package com.example.uasolshop.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "historiprodukDB")
data class History(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,
    @ColumnInfo(name = "namaProduk")
    val namaProduk: String,
    @ColumnInfo(name = "kategori")
    val kategori: String,
    @ColumnInfo(name = "harga")
    val harga: Int,
    @ColumnInfo(name = "banyak_book")
    val banyak_book: Int,
    @ColumnInfo("stok")
    val stok: Int? = 0,
    @ColumnInfo("deskripsiBarang")
    val deskripsiBarang: String,
    @ColumnInfo("fotoBarang")
    val fotoBarang: String,
    @ColumnInfo(name = "tgl_book")
    val date: String,
    @ColumnInfo(name = "username")
    val username: String
)