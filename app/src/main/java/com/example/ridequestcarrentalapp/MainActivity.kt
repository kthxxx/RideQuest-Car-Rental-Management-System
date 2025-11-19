package com.example.ridequestcarrentalapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ridequestcarrentalapp.data.CarRepositoryFactory
import com.example.ridequestcarrentalapp.ui.dashboard.EnhancedMainDashboard
import com.example.ridequestcarrentalapp.ui.detail.CarDetailScreen
import com.example.ridequestcarrentalapp.ui.booking.BookingFlowScreen
import com.example.ridequestcarrentalapp.ui.profile.ProfileScreen
import com.example.ridequestcarrentalapp.ui.profile.UserProfile
import com.example.ridequestcarrentalapp.ui.profile.UserBooking
import com.example.ridequestcarrentalapp.ui.profile.VerificationStatus
import com.example.ridequestcarrentalapp.ui.profile.MembershipTier
import com.example.ridequestcarrentalapp.ui.admin.BookingStatus
import com.example.ridequestcarrentalapp.ui.theme.RideQuestCarRentalAppTheme
import com.example.ridequestcarrentalapp.ui.theme.Orange
import com.example.ridequestcarrentalapp.ui.theme.Helvetica
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import com.example.ridequestcarrentalapp.data.Car
import com.example.ridequestcarrentalapp.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RideQuestCarRentalAppTheme {
                RideQuestApp()
            }
        }
    }
}

@Composable
fun RideQuestApp() {
    val navController = rememberNavController()
    val repository = remember { CarRepositoryFactory.create() }
    val context = LocalContext.current

    // Sample user profile data
    val sampleUserProfile = remember {
        UserProfile(
            id = "user123",
            name = "Joshua Miguel Jamisola",
            email = "joshua@ridequest.com",
            phone = "+63 912 345 6789",
            memberSince = "January 2024",
            totalBookings = 12,
            favoriteLocations = listOf("Cebu City", "Mactan Airport", "IT Park"),
            verificationStatus = VerificationStatus.VERIFIED,
            loyaltyPoints = 1850,
            membershipTier = MembershipTier.SILVER
        )
    }

    // Sample bookings data
    val sampleBookings = remember {
        listOf(
            UserBooking(
                id = "booking1",
                carName = "Toyota Vios",
                carImage = R.drawable.white_back_logo,
                pickupDate = "Oct 10, 2024",
                returnDate = "Oct 13, 2024",
                location = "Cebu City",
                totalAmount = 5400.0,
                status = BookingStatus.CONFIRMED,
                bookingDate = "Oct 08, 2024"
            ),
            UserBooking(
                id = "booking2",
                carName = "Honda CR-V",
                carImage = R.drawable.white_back_logo,
                pickupDate = "Sep 25, 2024",
                returnDate = "Sep 28, 2024",
                location = "Mactan Airport",
                totalAmount = 8100.0,
                status = BookingStatus.COMPLETED,
                bookingDate = "Sep 20, 2024"
            ),
            UserBooking(
                id = "booking3",
                carName = "Mitsubishi Xpander",
                carImage = R.drawable.white_back_logo,
                pickupDate = "Sep 15, 2024",
                returnDate = "Sep 18, 2024",
                location = "IT Park",
                totalAmount = 7500.0,
                status = BookingStatus.COMPLETED,
                bookingDate = "Sep 10, 2024"
            )
        )
    }

    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)) {
                    EnhancedMainDashboard(
                        onCarClick = { car ->
                            navController.navigate("car_detail/${car.id}")
                        },
                        onProfileClick = {
                            navController.navigate("profile")
                        },
                        onNotificationClick = {
                            navController.navigate("notification")
                        },
                        onCategoryClick = { category ->
                            println("Category clicked: ${category.displayName}")
                        },
                        onBrandClick = { brand ->
                            println("Brand clicked: $brand")
                        },
                        onMapViewClick = {
                            Toast.makeText(context, "Map view coming soon!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }

        composable("profile") {
            ProfileScreen(
                userProfile = sampleUserProfile,
                recentBookings = sampleBookings,
                onBackClick = {
                    navController.popBackStack()
                },
                onEditProfileClick = {
                    Toast.makeText(context, "Edit profile feature coming soon!", Toast.LENGTH_SHORT).show()
                },
                onBookingHistoryClick = {
                    Toast.makeText(context, "Booking history feature coming soon!", Toast.LENGTH_SHORT).show()
                },
                onPaymentMethodsClick = {
                    Toast.makeText(context, "Payment methods feature coming soon!", Toast.LENGTH_SHORT).show()
                },
                onNotificationsClick = {
                    Toast.makeText(context, "Notifications settings coming soon!", Toast.LENGTH_SHORT).show()
                },
                onHelpSupportClick = {
                    Toast.makeText(context, "Help & Support feature coming soon!", Toast.LENGTH_SHORT).show()
                },
                onSettingsClick = {
                    Toast.makeText(context, "Settings feature coming soon!", Toast.LENGTH_SHORT).show()
                },
                onLogoutClick = {
                    Toast.makeText(context, "Logging out...", Toast.LENGTH_SHORT).show()
                    // Navigate back to SecondActivity (login screen)
                    val intent = Intent(context, SecondActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                    (context as? ComponentActivity)?.finish()
                },
                onBookingClick = { booking ->
                    Toast.makeText(context, "Booking details: ${booking.carName}", Toast.LENGTH_SHORT).show()
                }
            )
        }

        composable("car_detail/{carId}") { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId") ?: ""
            CarDetailWithLoading(
                carId = carId,
                repository = repository,
                navController = navController,
                context = context
            )
        }

        composable("booking_flow/{carId}") { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId") ?: ""
            BookingFlowWithLoading(
                carId = carId,
                repository = repository,
                navController = navController,
                context = context
            )
        }

        composable("quick_book") {
            QuickBookScreen(
                repository = repository,
                onBackClick = { navController.popBackStack() },
                onCarSelected = { car ->
                    navController.navigate("car_detail/${car.id}")
                }
            )
        }
    }
}

@Composable
private fun CarDetailWithLoading(
    carId: String,
    repository: com.example.ridequestcarrentalapp.data.CarRepository,
    navController: NavController,
    context: android.content.Context
) {
    var car by remember(carId) { mutableStateOf<Car?>(null) }
    var isLoading by remember(carId) { mutableStateOf(true) }
    var hasError by remember(carId) { mutableStateOf(false) }

    LaunchedEffect(carId) {
        try {
            isLoading = true
            hasError = false
            car = repository.getCarById(carId)
            if (car == null) {
                hasError = true
                Toast.makeText(context, "Car not found", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
        } catch (e: Exception) {
            hasError = true
            Toast.makeText(context, "Error loading car details", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        } finally {
            isLoading = false
        }
    }

    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Orange)
            }
        }
        hasError -> {
            // Error state is handled in LaunchedEffect
        }
        car != null -> {
            CarDetailScreen(
                car = car!!,
                onBackClick = { navController.popBackStack() },
                onBookNowClick = { selectedCar ->
                    navController.navigate("booking_flow/${selectedCar.id}")
                }
            )
        }
    }
}

@Composable
private fun BookingFlowWithLoading(
    carId: String,
    repository: com.example.ridequestcarrentalapp.data.CarRepository,
    navController: NavController,
    context: android.content.Context
) {
    var car by remember(carId) { mutableStateOf<Car?>(null) }
    var isLoading by remember(carId) { mutableStateOf(true) }
    var hasError by remember(carId) { mutableStateOf(false) }

    LaunchedEffect(carId) {
        try {
            isLoading = true
            hasError = false
            car = repository.getCarById(carId)
            if (car == null) {
                hasError = true
                Toast.makeText(context, "Car not found", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
        } catch (e: Exception) {
            hasError = true
            Toast.makeText(context, "Error loading car details", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        } finally {
            isLoading = false
        }
    }

    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Orange)
            }
        }
        hasError -> {
            // Error state is handled in LaunchedEffect
        }
        car != null -> {
            BookingFlowScreen(
                car = car!!,
                onBackClick = { navController.popBackStack() },
                onConfirmBookingClick = { bookingDetails ->
                    // TODO: Save booking to backend
                    Toast.makeText(context, "Booking confirmed for ${bookingDetails.car.name}!", Toast.LENGTH_LONG).show()
                    // Navigate back to dashboard
                    navController.navigate("dashboard") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                },
                onLocationClick = { location ->
                    // TODO: Open map view for location selection
                    Toast.makeText(context, "Location: $location", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuickBookScreen(
    repository: com.example.ridequestcarrentalapp.data.CarRepository,
    onBackClick: () -> Unit,
    onCarSelected: (Car) -> Unit
) {
    var availableCars by remember { mutableStateOf<List<Car>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            isLoading = true
            availableCars = repository.getAllCars().filter { it.isAvailable }
        } catch (e: Exception) {
            // Handle error
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quick Book", fontFamily = Helvetica) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(color = Orange)
                }
                availableCars.isEmpty() -> {
                    EmptyQuickBookState(onBrowseCars = onBackClick)
                }
                else -> {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = Orange,
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Quick Book",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontFamily = Helvetica
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${availableCars.size} cars available for instant booking",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            fontFamily = Helvetica
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = onBackClick,
                            colors = ButtonDefaults.buttonColors(containerColor = Orange)
                        ) {
                            Text("Browse Available Cars", fontFamily = Helvetica)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyQuickBookState(onBrowseCars: () -> Unit) {
    Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.DirectionsCar,
            contentDescription = null,
            tint = Color.Gray.copy(alpha = 0.3f),
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "No Cars Available",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontFamily = Helvetica
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "All cars are current@ly booked.\nCheck back later or browse our full fleet.",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            fontFamily = Helvetica
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onBrowseCars,
            colors = ButtonDefaults.buttonColors(containerColor = Orange)
        ) {
            Text("Browse All Cars", fontFamily = Helvetica)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppNavigationPreview() {
    RideQuestCarRentalAppTheme {
        RideQuestApp()
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyQuickBookPreview() {
    RideQuestCarRentalAppTheme {
        EmptyQuickBookState(onBrowseCars = {})
    }
}