package com.example.ridequestcarrentalapp.ui.authentication

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class User(
    val id: String,
    val email: String,
    val name: String,
    val phone: String,
    val profileImage: String? = null
)

class AuthenticationManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        // Check if user is already logged in
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        val userId = prefs.getString("user_id", null)
        val email = prefs.getString("user_email", null)
        val name = prefs.getString("user_name", null)
        val phone = prefs.getString("user_phone", null)
        val profileImage = prefs.getString("user_profile_image", null)

        if (userId != null && email != null && name != null && phone != null) {
            _currentUser.value = User(userId, email, name, phone, profileImage)
            _isLoggedIn.value = true
        }
    }

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            // Simulate API call delay
            kotlinx.coroutines.delay(1000)

            // Simple validation for demo (replace with real API call)
            if (isValidCredentials(email, password)) {
                val user = User(
                    id = generateUserId(),
                    email = email,
                    name = getUserNameFromEmail(email),
                    phone = getPhoneFromEmail(email)
                )

                saveUserToPrefs(user)
                _currentUser.value = user
                _isLoggedIn.value = true

                Result.success(user)
            } else {
                Result.failure(Exception("Invalid credentials"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(name: String, email: String, phone: String, password: String): Result<User> {
        return try {
            // Simulate API call delay
            kotlinx.coroutines.delay(1000)

            // Simple validation
            if (isValidEmail(email) && password.length >= 6) {
                val user = User(
                    id = generateUserId(),
                    email = email,
                    name = name,
                    phone = phone
                )

                saveUserToPrefs(user)
                _currentUser.value = user
                _isLoggedIn.value = true

                Result.success(user)
            } else {
                Result.failure(Exception("Invalid registration data"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        prefs.edit().clear().apply()
        _currentUser.value = null
        _isLoggedIn.value = false
    }

    private fun saveUserToPrefs(user: User) {
        prefs.edit().apply {
            putString("user_id", user.id)
            putString("user_email", user.email)
            putString("user_name", user.name)
            putString("user_phone", user.phone)
            putString("user_profile_image", user.profileImage)
            apply()
        }
    }

    private fun isValidCredentials(email: String, password: String): Boolean {
        // Demo credentials - replace with real authentication
        return email == "user@example.com" && password == "password123" ||
                email == "admin@example.com" && password == "admin123" ||
                isValidEmail(email) && password.length >= 6
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun getUserNameFromEmail(email: String): String {
        return email.substringBefore("@").replaceFirstChar { it.uppercase() }
    }

    private fun getPhoneFromEmail(email: String): String {
        // Demo phone generation - replace with real data
        return "+1234567890"
    }

    private fun generateUserId(): String {
        return "user_${System.currentTimeMillis()}"
    }
}