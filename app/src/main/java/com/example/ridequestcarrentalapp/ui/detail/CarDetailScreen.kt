package com.example.ridequestcarrentalapp.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ridequestcarrentalapp.R
import com.example.ridequestcarrentalapp.ui.theme.Helvetica
import com.example.ridequestcarrentalapp.ui.theme.Orange
import com.example.ridequestcarrentalapp.data.Car
import com.example.ridequestcarrentalapp.data.CarCategory
import com.example.ridequestcarrentalapp.data.FuelType
import com.example.ridequestcarrentalapp.data.Transmission

@Composable
fun CarDetailScreen(
    car: Car,
    onBackClick: () -> Unit = {},
    onBookNowClick: (Car) -> Unit = {},
    onFavoriteClick: (Car) -> Unit = {},
    onShareClick: (Car) -> Unit = {},
    onCallClick: () -> Unit = {}
) {
    var isFavorite by remember { mutableStateOf(false) }

    // Enhanced car features matching the design
    val carFeatures = listOf(
        CarFeature("Capacity", "${car.seats} Seats", Icons.Default.Person),
        CarFeature("Engine Out", "670 HP", Icons.Default.Speed),
        CarFeature("Max Speed", "250km/h", Icons.Default.Timeline),
        CarFeature("Advance", "Autopilot", Icons.Default.SmartToy),
        CarFeature("Single Charge", "405 Miles", Icons.Default.ElectricCar),
        CarFeature("Advance", "Auto Parking", Icons.Default.LocalParking)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp)
        ) {
            // Header with back button and menu
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onBackClick() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Gray.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }

                Text(
                    text = "Car Details",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = Helvetica
                )

                IconButton(
                    onClick = { /* Menu action */ },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Gray.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options",
                        tint = Color.Black
                    )
                }
            }

            // Car Image Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(200.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.05f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = car.imageRes),
                        contentDescription = car.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .fillMaxHeight(0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Car Title and Price
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${car.brand} ${car.name}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontFamily = Helvetica
                        )
                        Text(
                            text = "Premium Car",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontFamily = Helvetica
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "₱${car.pricePerDay.toInt()}",
                            fontSize = 24.sp,
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

            Spacer(modifier = Modifier.height(24.dp))

            // Car Features Section
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Car features",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = Helvetica
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Features Grid - 3 columns, 2 rows
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.height(200.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(carFeatures) { feature ->
                        FeatureCard(feature = feature)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Reviews Section
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Review (125)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = Helvetica
                    )
                    Text(
                        text = "See All",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontFamily = Helvetica,
                        modifier = Modifier.clickable { /* Navigate to all reviews */ }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // First Review
                    ReviewCard(
                        name = "Mr. Jack",
                        rating = 5.0f,
                        comment = "The rental car was clean, reliable, and the service was quick and efficient.",
                        modifier = Modifier.weight(1f)
                    )

                    // Second Review
                    ReviewCard(
                        name = "Robert",
                        rating = 5.0f,
                        comment = "The rental car was clean, reliable and the service was quick.",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        // Bottom Book Now Button
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Orange),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onBookNowClick(car) }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Book Now",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = Helvetica
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Book Now",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ReviewCard(
    name: String,
    rating: Float,
    comment: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.05f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Profile and Rating Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Profile Avatar
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Orange.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = name.first().toString(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Orange,
                            fontFamily = Helvetica
                        )
                    }

                    Text(
                        text = name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        fontFamily = Helvetica
                    )
                }

                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = rating.toString(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = Helvetica
                    )
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Review Comment
            Text(
                text = comment,
                fontSize = 12.sp,
                color = Color.Gray,
                fontFamily = Helvetica,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun FeatureCard(feature: CarFeature) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.05f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Orange.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = feature.icon,
                    contentDescription = feature.title,
                    tint = Orange,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = feature.title,
                fontSize = 10.sp,
                color = Color.Gray,
                fontFamily = Helvetica,
                textAlign = TextAlign.Center,
                maxLines = 1
            )

            // Value
            Text(
                text = feature.value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = Helvetica,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

data class CarFeature(
    val title: String,
    val value: String,
    val icon: ImageVector
)

data class Review(val userName: String, val comment: String, val rating: Float, val date: String)

private val sampleCar = Car(
    id = "1",
    name = "Model S",
    brand = "Tesla",
    model = "Model S",
    year = 2023,
    pricePerDay = 18000.0,
    rating = 4.8f,
    imageRes = R.drawable.white_back_logo,
    fuelType = FuelType.GASOLINE,
    transmission = Transmission.AUTOMATIC,
    seats = 5,
    isAvailable = true,
    category = CarCategory.LUXURY
)

@Preview(showBackground = true)
@Composable
fun CarDetailScreenPreview() {
    CarDetailScreen(car = sampleCar)
}