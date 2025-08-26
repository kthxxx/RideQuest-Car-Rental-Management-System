package com.example.ridequestcarrentalapp.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ridequestcarrentalapp.R
import com.example.ridequestcarrentalapp.data.CarRepository
import com.example.ridequestcarrentalapp.data.Car
import com.example.ridequestcarrentalapp.ui.theme.Helvetica
import com.example.ridequestcarrentalapp.ui.theme.Orange


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDashboard(
    onCarClick: (Car) -> Unit = {},
    onProfileClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    onBrandClick: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedBrand by remember { mutableStateOf("All Brands") }
    var showBrandFilter by remember { mutableStateOf(false) }

    // Car categories
    val categories = listOf("All", "Economy", "SUV", "Van/MPV", "Luxury", "Electric/Hybrid")

    // Car brands
    val brands = listOf("All Brands", "Toyota", "Honda", "Mitsubishi", "Nissan", "Ford", "Hyundai", "Kia", "Suzuki", "BMW", "Mercedes", "Audi", "Lexus", "Tesla", "BYD", "MG")

    // Philippine car rental data with realistic pricing in PHP
    val philippineCars = CarRepository.getAllCars()

    // Advanced filtering logic
    val filteredCars = philippineCars.filter { car ->
        val matchesSearch = if (searchQuery.isBlank()) {
            true
        } else {
            car.name.contains(searchQuery, ignoreCase = true) ||
                    car.brand.contains(searchQuery, ignoreCase = true) ||
                    car.model.contains(searchQuery, ignoreCase = true) ||
                    car.fuelType.contains(searchQuery, ignoreCase = true) ||
                    car.transmission.contains(searchQuery, ignoreCase = true)
        }

        val matchesCategory = selectedCategory == "All" || car.category == selectedCategory
        val matchesBrand = selectedBrand == "All Brands" || car.brand == selectedBrand

        matchesSearch && matchesCategory && matchesBrand
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Good Morning",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = Helvetica
                )
                Text(
                    text = "Juan Dela Cruz",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = Helvetica
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Notification Icon
                IconButton(
                    onClick = { onNotificationClick() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Gray.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.Gray
                    )
                }

                // Profile Image
                IconButton(
                    onClick = { onProfileClick() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Orange, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color.White
                    )
                }
            }
        }

        // Enhanced Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = {
                Text(
                    "Search by car, brand, fuel type...",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
            },
            trailingIcon = {
                Row {
                    // Clear search button
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    IconButton(onClick = { showBrandFilter = !showBrandFilter }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Brand Filter",
                            tint = Orange
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Orange,
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                unfocusedContainerColor = Color.Gray.copy(alpha = 0.05f),
                focusedContainerColor = Color.Gray.copy(alpha = 0.05f)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Categories Filter
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(categories) { category ->
                CategoryChip(
                    category = category,
                    isSelected = category == selectedCategory,
                    onClick = {
                        selectedCategory = category
                        onCategoryClick(category)
                    }
                )
            }
        }

        // Brand Filter (conditionally shown)
        if (showBrandFilter) {
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(brands) { brand ->
                    BrandChip(
                        brand = brand,
                        isSelected = brand == selectedBrand,
                        onClick = {
                            selectedBrand = brand
                            onBrandClick(brand)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Section Header with Results Count
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Available Cars",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = Helvetica
                )
                Text(
                    text = "${filteredCars.size} cars found",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = Helvetica
                )
            }

            Text(
                text = "See All",
                fontSize = 14.sp,
                color = Orange,
                fontFamily = Helvetica,
                modifier = Modifier.clickable { /* See all action */ }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Cars List
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(filteredCars) { car ->
                PhilippineCarCard(
                    car = car,
                    onClick = { onCarClick(car) }
                )
            }
        }
    }
}

@Composable
fun CategoryChip(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                if (isSelected) Orange else Color.Gray.copy(alpha = 0.1f),
                RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = category,
            color = if (isSelected) Color.White else Color.Gray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Helvetica
        )
    }
}

@Composable
fun BrandChip(
    brand: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                if (isSelected) Orange.copy(alpha = 0.2f) else Color.Gray.copy(alpha = 0.05f),
                RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = brand,
            color = if (isSelected) Orange else Color.Gray,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            fontFamily = Helvetica
        )
    }
}

@Composable
fun PhilippineCarCard(
    car: Car,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Car Image
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = car.imageRes),
                    contentDescription = car.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Car Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Brand and Car Name
                Text(
                    text = "${car.brand} ${car.name}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = Helvetica,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${car.year} Model",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = Helvetica
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Car Specs Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DetailChip(
                        icon = Icons.Default.Person,
                        text = "${car.seats} seats"
                    )
                    DetailChip(
                        icon = Icons.Default.LocalGasStation,
                        text = car.fuelType
                    )
                    DetailChip(
                        icon = Icons.Default.Settings,
                        text = car.transmission
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Rating and Availability
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = car.rating.toString(),
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontFamily = Helvetica
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                if (car.isAvailable) Color.Green.copy(alpha = 0.1f) else Color.Red.copy(alpha = 0.1f),
                                RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (car.isAvailable) "Available" else "Booked",
                            fontSize = 10.sp,
                            color = if (car.isAvailable) Color.Green else Color.Red,
                            fontWeight = FontWeight.Medium,
                            fontFamily = Helvetica
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Price in PHP
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "₱${car.pricePerDay.toInt()}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Orange,
                    fontFamily = Helvetica
                )
                Text(
                    text = "per day",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = Helvetica
                )
            }
        }
    }
}

@Composable
fun DetailChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(12.dp),
            tint = Color.Gray
        )
        Text(
            text = text,
            fontSize = 10.sp,
            color = Color.Gray,
            fontFamily = Helvetica
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainDashboardPreview() {
    MainDashboard()
}