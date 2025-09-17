package com.example.ridequestcarrentalapp.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.style.TextAlign
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
        // Top Bar - RideQuest Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // RideQuest Logo and Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Orange, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = "RideQuest Logo",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "RideQuest",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Orange,
                    fontFamily = Helvetica
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Notification Icon with badge
                Box {
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
                    // Notification badge
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(Color.Red, CircleShape)
                            .align(Alignment.TopEnd),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "2",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
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
                    "Search your dream car",
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
                IconButton(onClick = { showBrandFilter = !showBrandFilter }) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Filter",
                        tint = Orange
                    )
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

        // Categories Filter as horizontal chips
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(categories) { category ->
                CategoryChipNew(
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

        Spacer(modifier = Modifier.height(20.dp))

        // Cars Grid - 2 columns like in the design
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredCars) { car ->
                CarCardGrid(
                    car = car,
                    onClick = { onCarClick(car) }
                )
            }
        }
    }
}

@Composable
fun CategoryChipNew(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val displayCategory = when(category) {
        "Van/MPV" -> "Van / MPV"
        "Electric/Hybrid" -> "EV"
        else -> category
    }

    Box(
        modifier = Modifier
            .background(
                if (isSelected) Orange else Color.Gray.copy(alpha = 0.1f),
                RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = displayCategory,
            color = if (isSelected) Color.White else Color.Gray,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
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
fun CarCardGrid(
    car: Car,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Car Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color.Gray.copy(alpha = 0.05f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = car.imageRes),
                    contentDescription = car.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Car Name and Brand
            Text(
                text = "${car.brand} ${car.name}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = Helvetica,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = car.category,
                fontSize = 12.sp,
                color = Color.Gray,
                fontFamily = Helvetica
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Car specs in compact form
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Seats",
                        modifier = Modifier.size(12.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = "${car.seats}",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        fontFamily = Helvetica
                    )
                    Text(
                        text = "seater",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        fontFamily = Helvetica
                    )

                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Transmission",
                        modifier = Modifier.size(12.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = if (car.transmission == "Automatic") "Automatic" else "Manual",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        fontFamily = Helvetica
                    )
                }

                // Premium badge (if luxury)
                if (car.category == "Luxury") {
                    Box(
                        modifier = Modifier
                            .background(Orange, RoundedCornerShape(8.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Premium",
                            fontSize = 8.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontFamily = Helvetica
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Price
            Text(
                text = "₱${car.pricePerDay.toInt()}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Orange,
                fontFamily = Helvetica
            )
            Text(
                text = "per day",
                fontSize = 10.sp,
                color = Color.Gray,
                fontFamily = Helvetica
            )

            Spacer(modifier = Modifier.height(12.dp))

            // View Details Button
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "View Details",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Helvetica
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainDashboardPreview() {
    MainDashboard()
}