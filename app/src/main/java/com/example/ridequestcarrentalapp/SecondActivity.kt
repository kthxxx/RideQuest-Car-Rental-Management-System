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
import com.example.ridequestcarrentalapp.ui.login.SignUpScreen
import com.example.ridequestcarrentalapp.ui.feature.onbound.OnBoundScreen
import com.example.ridequestcarrentalapp.ui.feature.onbound.OnBoundScreen1
import com.example.ridequestcarrentalapp.ui.feature.onbound.OnBoundScreen2
import com.example.ridequestcarrentalapp.ui.feature.onbound.OnBoundScreen3
import com.example.ridequestcarrentalapp.ui.feature.onbound.OnBoundScreen4

class SecondActivity : ComponentActivity() {
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
                        onLoginClick = {
                            // Handle successful login
                            Toast.makeText(this@SecondActivity, "Welcome back!", Toast.LENGTH_SHORT).show()

                            // Navigate to MainActivity after successful login
                            startActivity(Intent(this@SecondActivity, MainActivity::class.java))
                            finish()
                        } as (String, String) -> Unit,
                        onSignUpClick = {
                            navController.navigate("signup")
                        },
                        onForgotPasswordClick = {
                            Toast.makeText(this@SecondActivity, "Forgot Password clicked", Toast.LENGTH_SHORT).show()
                            // TODO: Implement forgot password functionality
                        }
                    )
                }

                composable("signup") {
                    SignUpScreen(
                        onSignUpClick = {
                            // Handle successful sign up
                            Toast.makeText(this@SecondActivity, "Account created successfully!", Toast.LENGTH_SHORT).show()

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
}