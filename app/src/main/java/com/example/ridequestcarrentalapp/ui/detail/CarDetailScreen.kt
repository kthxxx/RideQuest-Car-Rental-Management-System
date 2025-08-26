package com.example.ridequestcarrentalapp.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
    var selectedImageIndex by remember { mutableStateOf(0) }

    val carImages = listOf(
        R.drawable.white_back_logo,
        R.drawable.white_back_logo,
        R.drawable.white_back_logo,
        R.drawable.white_back_logo
    )

    val carFeatures = listOf(
        "Air Conditioning", "GPS Navigation", "Bluetooth", "USB Port",
        "Power Steering", "Central Locking", "Electric Windows", "ABS"
    )

    val reviews = listOf(
        Review("Juan Santos", "Excellent car! Very clean and comfortable.", 5.0f, "2 days ago"),
        Review("Maria Cruz", "Great experience. Highly recommended!", 4.5f, "1 week ago"),
        Review("Jose Dela Cruz", "Good value for money. Will rent again.", 4.0f, "2 weeks ago")
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

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = {
                            isFavorite = !isFavorite
                            onFavoriteClick(car)
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.Gray.copy(alpha = 0.1f), CircleShape)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else Color.Gray
                        )
                    }

                    IconButton(
                        onClick = { onShareClick(car) },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.Gray.copy(alpha = 0.1f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.Gray
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(250.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = carImages[selectedImageIndex]),
                        contentDescription = car.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        carImages.forEachIndexed { index, _ ->
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        if (index == selectedImageIndex) Orange else Color.White.copy(alpha = 0.5f),
                                        CircleShape
                                    )
                                    .clickable { selectedImageIndex = index }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${car.brand} ${car.name}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontFamily = Helvetica
                        )
                        Text(
                            text = "${car.year} Model • ${car.category}",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontFamily = Helvetica
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "₱${car.pricePerDay.toInt()}",
                            fontSize = 28.sp,
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

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
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
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "${car.rating} (${reviews.size} reviews)",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontFamily = Helvetica
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(
                                if (car.isAvailable) Color.Green.copy(alpha = 0.1f) else Color.Red.copy(alpha = 0.1f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (car.isAvailable) "Available Now" else "Currently Booked",
                            fontSize = 12.sp,
                            color = if (car.isAvailable) Color.Green else Color.Red,
                            fontWeight = FontWeight.Medium,
                            fontFamily = Helvetica
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.05f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Specifications",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = Helvetica
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SpecificationItem(
                            icon = Icons.Default.Person,
                            title = "Seats",
                            value = "${car.seats} People",
                            modifier = Modifier.weight(1f)
                        )
                        SpecificationItem(
                            icon = Icons.Default.LocalGasStation,
                            title = "Fuel Type",
                            value = car.fuelType,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SpecificationItem(
                            icon = Icons.Default.Settings,
                            title = "Transmission",
                            value = car.transmission,
                            modifier = Modifier.weight(1f)
                        )
                        SpecificationItem(
                            icon = Icons.Default.DirectionsCar,
                            title = "Category",
                            value = car.category,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Features & Amenities",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = Helvetica
                )
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(carFeatures) { feature ->
                        FeatureChip(feature)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Customer Reviews",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = Helvetica
                    )
                    Text(
                        text = "View All",
                        fontSize = 14.sp,
                        color = Orange,
                        fontFamily = Helvetica,
                        modifier = Modifier.clickable { }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                reviews.forEach { review ->
                    ReviewItem(review)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        Card(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { onCallClick() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Orange),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Orange)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Call",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Call", fontWeight = FontWeight.Medium, fontFamily = Helvetica)
                }
                Button(
                    onClick = { onBookNowClick(car) },
                    modifier = Modifier.weight(2f),
                    colors = ButtonDefaults.buttonColors(containerColor = Orange),
                    enabled = car.isAvailable
                ) {
                    Icon(
                        imageVector = Icons.Default.EventAvailable,
                        contentDescription = "Book",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (car.isAvailable) "Book Now" else "Not Available",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Helvetica
                    )
                }
            }
        }
    }
}

@Composable
fun SpecificationItem(icon: ImageVector, title: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = title, tint = Orange, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(title, fontSize = 12.sp, color = Color.Gray, fontFamily = Helvetica)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Black, fontFamily = Helvetica, textAlign = TextAlign.Center)
    }
}

@Composable
fun FeatureChip(feature: String) {
    Box(
        modifier = Modifier
            .background(Orange.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(feature, fontSize = 12.sp, color = Orange, fontWeight = FontWeight.Medium, fontFamily = Helvetica)
    }
}

@Composable
fun ReviewItem(review: Review) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.05f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(review.userName, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Black, fontFamily = Helvetica)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFD700), modifier = Modifier.size(16.dp))
                    Text(review.rating.toString(), fontSize = 12.sp, color = Color.Gray, fontFamily = Helvetica)
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(review.comment, fontSize = 13.sp, color = Color.Gray, fontFamily = Helvetica)
            Spacer(modifier = Modifier.height(4.dp))
            Text(review.date, fontSize = 11.sp, color = Color.Gray.copy(alpha = 0.7f), fontFamily = Helvetica)
        }
    }
}

data class Review(val userName: String, val comment: String, val rating: Float, val date: String)

private val sampleCar = Car(
    id = "1",
    name = "Vios",
    brand = "Toyota",
    model = "Vios",
    year = 2023,
    pricePerDay = 1800.0,
    rating = 4.5f,
    imageRes = R.drawable.white_back_logo,
    fuelType = "Gasoline",
    transmission = "Automatic",
    seats = 5,
    isAvailable = true,
    category = "Economy"
)

@Preview(showBackground = true)
@Composable
fun CarDetailScreenPreview() {
    CarDetailScreen(car = sampleCar)
}
