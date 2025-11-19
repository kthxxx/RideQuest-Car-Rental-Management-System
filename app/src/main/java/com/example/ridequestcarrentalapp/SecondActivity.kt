// Updated SecondActivity.kt
package com.example.ridequestcarrentalapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ridequestcarrentalapp.ui.login.LoginScreen
import com.example.ridequestcarrentalapp.ui.login.CompleteSignUpScreen // CHANGED
import com.example.ridequestcarrentalapp.ui.login.CompleteRegistrationData // ADDED
import com.example.ridequestcarrentalapp.ui.feature.onbound.OnBoundScreen
import com.example.ridequestcarrentalapp.ui.feature.onbound.OnBoundScreen1
import com.example.ridequestcarrentalapp.ui.feature.onbound.OnBoundScreen2
import com.example.ridequestcarrentalapp.ui.feature.onbound.OnBoundScreen3
import com.example.ridequestcarrentalapp.ui.feature.onbound.OnBoundScreen4

class SecondActivity : ComponentActivity() {
    // Simple in-memory user storage with complete profile data
    private val registeredUsers = mutableMapOf<String, UserData>()

    // User data class to store complete information
    data class UserData(
        val email: String,
        val password: String,
        val fullName: String,
        val phone: String,
        val dateOfBirth: String,
        val gender: String,
        val address: String,
        val licenseNumber: String
    )

    // Default credentials for testing
    init {
        registeredUsers["admin@ridequest.com"] = UserData(
            email = "admin@ridequest.com",
            password = "admin123",
            fullName = "Admin User",
            phone = "+63 912 345 6789",
            dateOfBirth = "Jan 01, 1990",
            gender = "Male",
            address = "Cebu City, Cebu",
            licenseNumber = "A00-00-000000"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "onboard"
            ) {
                // Onboarding Screens
                composable("onboard") {
                    OnBoundScreen {
                        navController.navigate("onboard1")
                    }
                }

                composable("onboard1") {
                    OnBoundScreen1 {
                        navController.navigate("onboard2")
                    }
                }

                composable("onboard2") {
                    OnBoundScreen2 {
                        navController.navigate("onboard3")
                    }
                }

                composable("onboard3") {
                    OnBoundScreen3 {
                        navController.navigate("onboard4")
                    }
                }

                composable("onboard4") {
                    OnBoundScreen4 {
                        navController.navigate("login")
                    }
                }

                // Auth Screens
                composable("login") {
                    LoginScreen(
                        onLoginClick = { email, password ->
                            // Validate credentials
                            if (validateLogin(email, password)) {
                                Toast.makeText(
                                    this@SecondActivity,
                                    "Welcome back!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                // Navigate to MainActivity after successful login
                                startActivity(Intent(this@SecondActivity, MainActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(
                                    this@SecondActivity,
                                    "Invalid email or password",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        onSignUpClick = {
                            navController.navigate("signup")
                        },
                        onForgotPasswordClick = {
                            Toast.makeText(
                                this@SecondActivity,
                                "Forgot Password clicked",
                                Toast.LENGTH_SHORT
                            ).show()
                            // TODO: Implement forgot password functionality
                        },
                        onGoogleSignInClick = {
                            Toast.makeText(
                                this@SecondActivity,
                                "Google Sign In coming soon",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onFacebookSignInClick = {
                            Toast.makeText(
                                this@SecondActivity,
                                "Facebook Sign In coming soon",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }

                // UPDATED SIGNUP ROUTE
                composable("signup") {
                    CompleteSignUpScreen(
                        onSignUpComplete = { registrationData ->
                            // Store the complete user data
                            registeredUsers[registrationData.email] = UserData(
                                email = registrationData.email,
                                password = registrationData.password,
                                fullName = registrationData.fullName,
                                phone = registrationData.phone,
                                dateOfBirth = registrationData.dateOfBirth,
                                gender = registrationData.gender,
                                address = "${registrationData.streetAddress}, ${registrationData.city}",
                                licenseNumber = registrationData.licenseNumber
                            )

                            Toast.makeText(
                                this@SecondActivity,
                                "Account created successfully! Welcome ${registrationData.fullName}",
                                Toast.LENGTH_LONG
                            ).show()

                            // Navigate back to login after successful registration
                            navController.navigate("login") {
                                popUpTo("signup") { inclusive = true }
                            }
                        },
                        onLoginClick = {
                            navController.navigate("login") {
                                popUpTo("signup") { inclusive = true }
                            }
                        },
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }

    // Validate login credentials
    private fun validateLogin(email: String, password: String): Boolean {
        return registeredUsers[email]?.password == password
    }
}