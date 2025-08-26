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
                        onLoginClick = { email, password ->
                            // Handle login logic here
                            Toast.makeText(this@SecondActivity, "Login: $email", Toast.LENGTH_SHORT).show()

                            // Navigate to MainActivity after successful login
                            startActivity(Intent(this@SecondActivity, MainActivity::class.java))
                            finish()
                        },
                        onSignUpClick = {
                            navController.navigate("signup")
                        },
                        onForgotPasswordClick = {
                            Toast.makeText(this@SecondActivity, "Forgot Password clicked", Toast.LENGTH_SHORT).show()
                        },
                        onGoogleSignInClick = {
                            Toast.makeText(this@SecondActivity, "Google Sign In clicked", Toast.LENGTH_SHORT).show()
                            // TODO: Implement Google Sign In
                        },
                        onFacebookSignInClick = {
                            Toast.makeText(this@SecondActivity, "Facebook Sign In clicked", Toast.LENGTH_SHORT).show()
                            // TODO: Implement Facebook Sign In
                        }
                    )
                }

                composable("signup") {
                    SignUpScreen(
                        onSignUpClick = { name, email, password ->
                            // Handle sign up logic here
                            Toast.makeText(this@SecondActivity, "Account created for $name", Toast.LENGTH_SHORT).show()

                            // Navigate back to login or directly to MainActivity
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
                        },
                        onGoogleSignUpClick = {
                            Toast.makeText(this@SecondActivity, "Google Sign Up clicked", Toast.LENGTH_SHORT).show()
                            // TODO: Implement Google Sign Up
                        },
                        onFacebookSignUpClick = {
                            Toast.makeText(this@SecondActivity, "Facebook Sign Up clicked", Toast.LENGTH_SHORT).show()
                            // TODO: Implement Facebook Sign Up
                        }
                    )
                }
            }
        }
    }
}