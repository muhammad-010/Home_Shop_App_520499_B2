package com.example.uasolshop.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uasolshop.dataclass.Users
import com.example.uasolshop.mainactivity.MainActivityAdmin
import com.example.uasolshop.mainactivity.MainActivityGuest
import com.example.uasolshop.databinding.ActivityLoginBinding
import com.example.uasolshop.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefManager: PrefManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager.getInstance(this)
        with(binding){
            btnLogin.setOnClickListener {
                val username = etUserLogin.text.toString()
                val password = etPassLogin.text.toString()
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Mohon isi semua data",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Login(username,password)
                }
            }
            txtRegister.setOnClickListener {
                startActivity(
                    Intent(this@LoginActivity,
                    RegisterActivity::class.java)
                )
            }
        }
    }

    private fun Login(inputUsername: String, inputPassword: String) {
        val apiService = ApiClient.getInstance()  // Initialize your API service.

        apiService.getAllUsers().enqueue(object : Callback<List<Users>> {
            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                if (response.isSuccessful) {

                    val users = response.body()
                    if (users != null) {

                        // Check if the username&pass exists in the list
                        val usernameExists = users.any { it.username == inputUsername }
                        val passExists = users.any { it.password == inputPassword}
                        val roleGuest = users.any { it.role == "guest"}

                        if (usernameExists && passExists && roleGuest) {
                            prefManager.setLoggedIn(true)
                            checkLoginStatus(inputUsername,inputPassword, inputRole = "guest")
                        }
                        else if(inputUsername == "admin" && inputPassword=="admin123"){
                            prefManager.setLoggedIn(true)
                            checkLoginStatus(inputUsername,inputPassword, inputRole = "admin")
                        }
                        else {
                            Toast.makeText(this@LoginActivity, "Username or Password is Invalid", Toast.LENGTH_SHORT).show()

                        }
                    }
                } else {
                    Log.e("CheckUsername", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                Log.e("CheckUsername", "Network failure: ${t.message}")
            }
        })
    }


    private fun checkLoginStatus(inputUsername: String, inputPassword: String, inputRole: String) {
        val isLoggedIn = prefManager.isLoggedIn()
        if (isLoggedIn) {
            Toast.makeText(this@LoginActivity, "Login berhasil",
                Toast.LENGTH_SHORT).show()
            prefManager.saveUsername(inputUsername)
            prefManager.savePassword(inputPassword)
            prefManager.saveRole(inputRole)
            if (inputUsername == "admin" && inputPassword == "admin123" && inputRole=="admin") {
                navigateToAdminDashboard()
            }
            else{
                navigateToGuestDashboard()
            }

        } else {
            Toast.makeText(this@LoginActivity, "Login gagal",
                Toast.LENGTH_SHORT).show()
        }
    }
    private fun navigateToAdminDashboard() {
        startActivity(Intent(this@LoginActivity, MainActivityAdmin::class.java))
        finish()
    }
    private fun navigateToGuestDashboard() {
        Log.d("guest","guest")
        startActivity(Intent(this@LoginActivity, MainActivityGuest::class.java))
        finish()
    }
}
