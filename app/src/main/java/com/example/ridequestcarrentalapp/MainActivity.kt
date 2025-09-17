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
import com.example.ridequestcarrentalapp.ui.booking.BookingFlowScreen
import com.example.ridequestcarrentalapp.ui.notifications.NotificationsScreen

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
                    navController.navigate("NotificationsScreen")
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
                    navController.navigate("NotificationsScreen") // Navigates to notifications screen
                },
                onHelpSupportClick = {
                    Toast.makeText(navController.context, "Help & Support", Toast.LENGTH_SHORT).show()
                },
                onSettingsClick = {
                    Toast.makeText(navController.context, "Settings", Toast.LENGTH_SHORT).show()
                },
                onLogoutClick = {
                    Toast.makeText(navController.context, "Logout", Toast.LENGTH_SHORT).show()
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
                LaunchedEffect(Unit) {
                    Toast.makeText(navController.context, "Car not found", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            }
        }

        // Booking Flow Screen
        composable("booking_flow/{carId}") { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId") ?: ""
            val car = CarRepository.getCarById(carId)

            if (car != null) {
                BookingFlowScreen(
                    car = car,
                    onBackClick = { navController.popBackStack() },
                    onConfirmBookingClick = {
                        // Handle confirmation logic
                    }
                )
            } else {
                LaunchedEffect(Unit) {
                    Toast.makeText(navController.context, "Car not found", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            }
        }

        // Notifications Screen
        composable("NotificationsScreen") {
            NotificationsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNotificationClick = { notification ->
                    // Optional: Handle clicks on individual notifications, e.g., navigate to a specific booking.
                    // Example: navController.navigate("booking_details/${notification.relatedBookingId}")
                    Toast.makeText(navController.context, "Notification clicked: ${notification.title}", Toast.LENGTH_SHORT).show()
                },
                onMarkAllReadClick = {
                    Toast.makeText(navController.context, "All notifications marked as read", Toast.LENGTH_SHORT).show()
                },
                onNotificationSettingsClick = {
                    // Optional: Navigate to a dedicated settings screen for notifications.
                    Toast.makeText(navController.context, "Notification Settings clicked", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}