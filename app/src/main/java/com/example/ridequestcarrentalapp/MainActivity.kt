package com.example.ridequestcarrentalapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ridequestcarrentalapp.ui.dashboard.MainDashboard
import com.example.ridequestcarrentalapp.ui.detail.CarDetailScreen
import com.example.ridequestcarrentalapp.ui.theme.RideQuestCarRentalAppTheme
import com.example.ridequestcarrentalapp.ui.profile.ProfileScreen
import androidx.compose.runtime.LaunchedEffect
import com.example.ridequestcarrentalapp.data.CarRepository
import com.example.ridequestcarrentalapp.data.Car
import com.example.ridequestcarrentalapp.ui.booking.BookingDetails
import com.example.ridequestcarrentalapp.ui.booking.BookingFlowScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RideQuestCarRentalAppTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "MainDashboard"
    ) {
        // Dashboard Screen
        composable("MainDashboard") {
            MainDashboard(
                onCarClick = { car ->
                    navController.navigate("car_detail/${car.id}")
                },
                onProfileClick = {
                    navController.navigate("ProfileScreen")
                },
                onNotificationClick = {
                    Toast.makeText(navController.context, "Notifications clicked", Toast.LENGTH_SHORT).show()
                    // navController.navigate("NotificationScreen")
                },
                onCategoryClick = { category ->
                    Toast.makeText(navController.context, "Category: $category", Toast.LENGTH_SHORT).show()
                },
                onBrandClick = { brand ->
                    Toast.makeText(navController.context, "Brand: $brand", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Profile Screen
        composable("ProfileScreen") {
            ProfileScreen(
                onBackClick = { navController.popBackStack() },
                onEditProfileClick = {
                    Toast.makeText(navController.context, "Edit Profile", Toast.LENGTH_SHORT).show()
                },
                onBookingHistoryClick = {
                    Toast.makeText(navController.context, "Booking History", Toast.LENGTH_SHORT).show()
                },
                onPaymentMethodsClick = {
                    Toast.makeText(navController.context, "Payment Methods", Toast.LENGTH_SHORT).show()
                },
                onNotificationsClick = {
                    Toast.makeText(navController.context, "Notification Settings", Toast.LENGTH_SHORT).show()
                },
                onHelpSupportClick = {
                    Toast.makeText(navController.context, "Help & Support", Toast.LENGTH_SHORT).show()
                },
                onSettingsClick = {
                    Toast.makeText(navController.context, "Settings", Toast.LENGTH_SHORT).show()
                },
                onLogoutClick = {
                    Toast.makeText(navController.context, "Logout", Toast.LENGTH_SHORT).show()
                    // Handle logout logic here
                }
            )
        }

        // Car Detail Screen
        composable("car_detail/{carId}") { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId") ?: ""
            val car = CarRepository.getCarById(carId)

            if (car != null) {
                CarDetailScreen(
                    car = car,
                    onBackClick = { navController.popBackStack() },
                    onBookNowClick = { selectedCar ->
                        navController.navigate("booking_flow/${selectedCar.id}")
                    }
                )
            } else {
                // Handle case where car is not found
                LaunchedEffect(Unit) {
                    Toast.makeText(navController.context, "Car not found", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            }
        }


        composable("booking_flow/{carId}") { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId") ?: ""
            val car = CarRepository.getCarById(carId)

            if (car != null) {
                BookingFlowScreen(
                    car = car,
                    onBackClick = { navController.popBackStack() },
                    onConfirmBookingClick = { bookingDetails ->
                        // Proceed with bookingDetails (e.g. navigate to confirmation)
                        navController.navigate("booking_confirmation")
                    }
                )
            } else {
                LaunchedEffect(Unit) {
                    Toast.makeText(navController.context, "Car not found", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            }
        }


    }
}

