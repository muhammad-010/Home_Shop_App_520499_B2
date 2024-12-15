package com.example.uasolshop.dataclass

import com.google.gson.annotations.SerializedName

data class Users(
    @SerializedName("_id")
    val id: String? = null,
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("role")
    val role: String
)
