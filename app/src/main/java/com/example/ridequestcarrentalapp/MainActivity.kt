package com.example.ridequestcarrentalapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextAlign
import com.example.ridequestcarrentalapp.data.CarRepositoryFactory
import com.example.ridequestcarrentalapp.ui.dashboard.EnhancedMainDashboard
import com.example.ridequestcarrentalapp.ui.detail.CarDetailScreen
import com.example.ridequestcarrentalapp.ui.booking.BookingFlowScreen
import com.example.ridequestcarrentalapp.ui.theme.RideQuestCarRentalAppTheme
import com.example.ridequestcarrentalapp.ui.theme.Orange
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import com.example.ridequestcarrentalapp.data.Car

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
                            // Navigate to profile screen or show profile dialog
                            Toast.makeText(context, "Profile feature coming soon!", Toast.LENGTH_SHORT).show()
                        },
                        onNotificationClick = {
                            // Navigate to notifications screen
                            Toast.makeText(context, "Notifications feature coming soon!", Toast.LENGTH_SHORT).show()
                        },
                        onCategoryClick = { category ->
                            // Handle category filtering - this is already handled in the dashboard
                            println("Category clicked: ${category.displayName}")
                        },
                        onBrandClick = { brand ->
                            // Handle brand filtering - this is already handled in the dashboard
                            println("Brand clicked: $brand")
                        },
                        onQuickBookClick = {
                            // Navigate to quick booking or show quick booking dialog
                            navController.navigate("quick_book")
                        },
                        onMapViewClick = {
                            // Navigate to map view
                            Toast.makeText(context, "Map view feature coming soon!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
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

        // Quick booking route - can be implemented later
        composable("quick_book") {
            QuickBookScreen(
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
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuickBookScreen(
    onBackClick: () -> Unit,
    onCarSelected: (Car) -> Unit
) {
    val repository = remember { CarRepositoryFactory.create() }
    var availableCars by remember { mutableStateOf<List<Car>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            isLoading = true
            // Get available cars for quick booking
            availableCars = repository.getAllCars().filter { it.isAvailable }
        } catch (e: Exception) {
            // Handle error
        } finally {
            isLoading = false
        }
    }

    // For now, just show a placeholder
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quick Book") },
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
            if (isLoading) {
                CircularProgressIndicator(color = Orange)
            } else {
                Text(
                    text = "Quick Book feature coming soon!\n${availableCars.size} cars available",
                    textAlign = TextAlign.Center
                )
            }
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