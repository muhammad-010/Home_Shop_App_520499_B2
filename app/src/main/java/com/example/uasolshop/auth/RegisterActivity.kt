package com.example.uasolshop.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uasolshop.mainactivity.MainActivityGuest
//import com.example.uasolshop.database.ApiResponse
import com.example.uasolshop.dataclass.Users
import com.example.uasolshop.databinding.ActivityRegisterBinding
import com.example.uasolshop.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var prefManager: PrefManager
    private var counter: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager.getInstance(this)
        with(binding) {
            btnDftr.setOnClickListener {
                val username = etUserDftr.text.toString()
                val password = etPassDftr.text.toString()
                val confirmPassword = etCpassDftr.text.toString()
                if (username.isEmpty() || password.isEmpty() ||
                    confirmPassword.isEmpty()
                ) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Mohon isi semua data",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (password != confirmPassword) {
                    Toast.makeText(
                        this@RegisterActivity, "Password tidak sama",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    val generatedId = generateId()
                    val role = "guest"
                    val newUser = Users( username = username, password = password, role = role)
                    checkIfUsernameExists(newUser)
                }
            }
            txtLogin.setOnClickListener {
                startActivity(
                    Intent(
                        this@RegisterActivity,
                        LoginActivity::class.java
                    )
                )
            }
        }
    }


    private fun checkIfUsernameExists(user: Users) {
        val apiService = ApiClient.getInstance()  // Initialize your API service.

        apiService.getAllUsers().enqueue(object : Callback<List<Users>> {
            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                if (response.isSuccessful) {

                    val users = response.body()
                    if (users != null) {

                        // Check if the username exists in the list
                        val usernameExists = users.any { it.username == user.username }

                        if (usernameExists) {
//                            Log.d("CheckUsername", "Username already exists: $inputUsername")
                            // Show error message to the user
                            Toast.makeText(this@RegisterActivity, "Username already exists", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d("CheckUsername", "Username is available: $user")
                            // Proceed with signup
                            createUser(user)
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


    private fun createUser(user: Users) {
        val apiService = ApiClient.getInstance()

        apiService.createUser(user).enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    Log.d("SignUp1", "API Response: $apiResponse")

                    if (apiResponse != null) {
                        // Show success message
                        Log.d("SignUp1", "Sending user data: $user")
                        Log.d("SignUp1", "Request URL: ${call.request().url}")
                        prefManager.saveRole(user.role)
                        prefManager.savePassword(user.password)
                        prefManager.saveUsername(user.username)
                        prefManager.setLoggedIn(true)
                        checkLoginStatus()
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registration successful",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    val errorMessage = try {
                        response.errorBody()?.string() ?: "Unknown error occurred"
                    } catch (e: Exception) {
                        "Error: ${e.message}"
                    }

                    Log.e("SignUp1", "Error Response: $errorMessage")
                    Toast.makeText(
                        this@RegisterActivity,
                        errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                Log.e("SignUp1", "Network failure: ${t.message}")
                Toast.makeText(
                    this@RegisterActivity,
                    "Network error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun generateId(): Int {
        val multipleOf = 5
        counter++
        Log.d("id", "body : ${counter}")
        Log.d("id", "body : ${multipleOf}")
        Log.d("id", "body : ${(multipleOf/ counter ) * multipleOf}")
        // Make sure the ID is a multiple of the specified number
        return (multipleOf/ counter ) * multipleOf
    }

    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if (isLoggedIn) {
            Toast.makeText(
                this@RegisterActivity, "Registrasi berhasil",
                Toast.LENGTH_SHORT
            ).show()
            Log.d("haiiii", "hajuga")

            val intent = Intent(this@RegisterActivity, MainActivityGuest::class.java)
            startActivity(intent)
             finish()
        } else {
            Toast.makeText(
                this@RegisterActivity, "Registrasi gagal",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}