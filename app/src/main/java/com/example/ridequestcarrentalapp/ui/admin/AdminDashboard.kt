package com.example.ridequestcarrentalapp.ui.admin

import android.annotation.SuppressLint
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ridequestcarrentalapp.ui.theme.Helvetica
import com.example.ridequestcarrentalapp.ui.theme.Orange

// Data classes for admin management
data class AdminStats(
    val totalUsers: Int,
    val totalBookings: Int,
    val totalSubscribers: Int,
    val totalQueries: Int,
    val totalVehicles: Int,
    val activeBookings: Int,
    val revenue: Double,
    val testimonials: Int
)

data class VehicleBrand(
    val id: String,
    val name: String,
    val logo: String,
    val vehicleCount: Int,
    val isActive: Boolean
)

data class Vehicle(
    val id: String,
    val brand: String,
    val model: String,
    val year: Int,
    val pricePerDay: Double,
    val seats: Int,
    val fuelType: String,
    val transmission: String,
    val isAvailable: Boolean,
    val category: String,
    val images: List<String> = emptyList()
)

data class Booking(
    val id: String,
    val userId: String,
    val userName: String,
    val vehicleId: String,
    val vehicleName: String,
    val startDate: String,
    val endDate: String,
    val totalAmount: Double,
    val status: BookingStatus,
    val bookingDate: String
)

enum class BookingStatus {
    PENDING, CONFIRMED, CANCELLED, COMPLETED
}

data class Testimonial(
    val id: String,
    val userId: String,
    val userName: String,
    val userImage: String,
    val rating: Float,
    val comment: String,
    val date: String,
    val isActive: Boolean
)

data class ContactQuery(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val subject: String,
    val message: String,
    val date: String,
    val isResolved: Boolean
)

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val registrationDate: String,
    val totalBookings: Int,
    val isActive: Boolean
)

data class Subscriber(
    val id: String,
    val email: String,
    val subscriptionDate: String,
    val isActive: Boolean
)

@SuppressLint("AutoboxingStateCreation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboard(
    onManageBrandsClick: () -> Unit = {},
    onPostVehicleClick: () -> Unit = {},
    onManageVehiclesClick: () -> Unit = {},
    onManageBookingsClick: () -> Unit = {},
    onManageTestimonialsClick: () -> Unit = {},
    onManageQueriesClick: () -> Unit = {},
    onManageUsersClick: () -> Unit = {},
    onManageContentClick: () -> Unit = {},
    onManageSubscribersClick: () -> Unit = {},
    onChangePasswordClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = listOf("Dashboard", "Vehicles", "Bookings", "Users", "Settings")

    // Sample admin stats
    val adminStats = AdminStats(
        totalUsers = 1247,
        totalBookings = 3892,
        totalSubscribers = 756,
        totalQueries = 89,
        totalVehicles = 43,
        activeBookings = 156,
        revenue = 2847500.0,
        testimonials = 234
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Admin Top Bar
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Orange)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Admin Dashboard",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = Helvetica
                    )
                    Text(
                        text = "RideQuest Car Rental Management",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        fontFamily = Helvetica
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { onChangePasswordClick() },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = { onLogoutClick() },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        // Tab Row
        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.White,
            contentColor = Orange
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontFamily = Helvetica,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        // Tab Content
        when (selectedTab) {
            0 -> DashboardOverview(adminStats)
            1 -> VehicleManagement(
                onManageBrandsClick = onManageBrandsClick,
                onPostVehicleClick = onPostVehicleClick,
                onManageVehiclesClick = onManageVehiclesClick
            )
            2 -> BookingManagement(onManageBookingsClick)
            3 -> UserManagement(
                onManageUsersClick = onManageUsersClick,
                onManageSubscribersClick = onManageSubscribersClick
            )
            4 -> SettingsManagement(
                onManageTestimonialsClick = onManageTestimonialsClick,
                onManageQueriesClick = onManageQueriesClick,
                onManageContentClick = onManageContentClick
            )
        }
    }
}

@Composable
fun DashboardOverview(stats: AdminStats) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Overview Statistics",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = Helvetica
            )
        }

        item {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.height(400.dp)
            ) {
                items(
                    listOf(
                        StatCard("Total Users", stats.totalUsers.toString(), Icons.Default.People, Color(0xFF4CAF50)),
                        StatCard("Total Bookings", stats.totalBookings.toString(), Icons.Default.BookOnline, Color(0xFF2196F3)),
                        StatCard("Active Bookings", stats.activeBookings.toString(), Icons.Default.EventAvailable, Orange),
                        StatCard("Total Vehicles", stats.totalVehicles.toString(), Icons.Default.DirectionsCar, Color(0xFF9C27B0)),
                        StatCard("Subscribers", stats.totalSubscribers.toString(), Icons.Default.Email, Color(0xFFFF9800)),
                        StatCard("Queries", stats.totalQueries.toString(), Icons.Default.ContactSupport, Color(0xFFF44336)),
                        StatCard("Revenue", "₱${String.format("%,.0f", stats.revenue)}", Icons.Default.AttachMoney, Color(0xFF009688)),
                        StatCard("Testimonials", stats.testimonials.toString(), Icons.Default.Star, Color(0xFFFFEB3B))
                    )
                ) { statCard ->
                    StatCardItem(statCard)
                }
            }
        }

        item {
            Text(
                text = "Recent Activity",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = Helvetica,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    RecentActivityItem("New booking from Juan Dela Cruz", "2 minutes ago", Icons.Default.BookOnline)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    RecentActivityItem("Vehicle Toyota Vios posted", "15 minutes ago", Icons.Default.DirectionsCar)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    RecentActivityItem("New user registration", "1 hour ago", Icons.Default.PersonAdd)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    RecentActivityItem("Contact query received", "3 hours ago", Icons.Default.ContactSupport)
                }
            }
        }
    }
}

@Composable
fun VehicleManagement(
    onManageBrandsClick: () -> Unit,
    onPostVehicleClick: () -> Unit,
    onManageVehiclesClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Vehicle Management",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = Helvetica
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ManagementCard(
                    title = "Manage Brands",
                    description = "Add, edit, or delete vehicle brands",
                    icon = Icons.Default.Business,
                    color = Color(0xFF2196F3),
                    onClick = onManageBrandsClick,
                    modifier = Modifier.weight(1f)
                )

                ManagementCard(
                    title = "Post Vehicle",
                    description = "Add new vehicles to the fleet",
                    icon = Icons.Default.Add,
                    color = Color(0xFF4CAF50),
                    onClick = onPostVehicleClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            ManagementCard(
                title = "Manage Vehicles",
                description = "Edit or remove existing vehicles",
                icon = Icons.Default.DirectionsCar,
                color = Orange,
                onClick = onManageVehiclesClick,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Text(
                text = "Vehicle Brands",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = Helvetica
            )
        }

        items(getSampleBrands()) { brand ->
            BrandItem(brand)
        }
    }
}

@Composable
fun BookingManagement(onManageBookingsClick: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Booking Management",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = Helvetica
            )
        }

        item {
            ManagementCard(
                title = "Manage Bookings",
                description = "Confirm, cancel, or view all bookings",
                icon = Icons.Default.BookOnline,
                color = Orange,
                onClick = onManageBookingsClick,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Text(
                text = "Recent Bookings",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = Helvetica
            )
        }

        items(getSampleBookings()) { booking ->
            BookingItem(booking)
        }
    }
}

@Composable
fun UserManagement(
    onManageUsersClick: () -> Unit,
    onManageSubscribersClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "User Management",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = Helvetica
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ManagementCard(
                    title = "Manage Users",
                    description = "View registered user details",
                    icon = Icons.Default.People,
                    color = Color(0xFF4CAF50),
                    onClick = onManageUsersClick,
                    modifier = Modifier.weight(1f)
                )

                ManagementCard(
                    title = "Subscribers",
                    description = "Manage newsletter subscribers",
                    icon = Icons.Default.Email,
                    color = Color(0xFFFF9800),
                    onClick = onManageSubscribersClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Text(
                text = "Recent Users",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = Helvetica
            )
        }

        items(getSampleUsers()) { user ->
            UserItem(user)
        }
    }
}

@Composable
fun SettingsManagement(
    onManageTestimonialsClick: () -> Unit,
    onManageQueriesClick: () -> Unit,
    onManageContentClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Settings & Content",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = Helvetica
            )
        }

        item {
            ManagementCard(
                title = "Manage Testimonials",
                description = "Activate or deactivate customer reviews",
                icon = Icons.Default.Star,
                color = Color(0xFFFFEB3B),
                onClick = onManageTestimonialsClick,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            ManagementCard(
                title = "Contact Queries",
                description = "View and respond to customer inquiries",
                icon = Icons.Default.ContactSupport,
                color = Color(0xFFF44336),
                onClick = onManageQueriesClick,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            ManagementCard(
                title = "Update Content",
                description = "Update website content and contact details",
                icon = Icons.Default.Edit,
                color = Color(0xFF9C27B0),
                onClick = onManageContentClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// Helper Composables
@Composable
fun StatCardItem(statCard: StatCard) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = statCard.icon,
                contentDescription = statCard.title,
                tint = statCard.color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = statCard.value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = Helvetica
            )
            Text(
                text = statCard.title,
                fontSize = 12.sp,
                color = Color.Gray,
                fontFamily = Helvetica
            )
        }
    }
}

@Composable
fun ManagementCard(
    title: String,
    description: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = Helvetica
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = Helvetica
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Go",
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun RecentActivityItem(text: String, time: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Orange,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = text,
                fontSize = 14.sp,
                color = Color.Black,
                fontFamily = Helvetica
            )
            Text(
                text = time,
                fontSize = 12.sp,
                color = Color.Gray,
                fontFamily = Helvetica
            )
        }
    }
}

@Composable
fun BrandItem(brand: VehicleBrand) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = brand.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                fontFamily = Helvetica,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${brand.vehicleCount} vehicles",
                fontSize = 12.sp,
                color = Color.Gray,
                fontFamily = Helvetica
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .background(
                        if (brand.isActive) Color.Green.copy(alpha = 0.1f) else Color.Red.copy(alpha = 0.1f),
                        RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (brand.isActive) "Active" else "Inactive",
                    fontSize = 10.sp,
                    color = if (brand.isActive) Color.Green else Color.Red,
                    fontFamily = Helvetica
                )
            }
        }
    }
}

@Composable
fun BookingItem(booking: Booking) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = booking.userName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    fontFamily = Helvetica
                )
                Box(
                    modifier = Modifier
                        .background(
                            when (booking.status) {
                                BookingStatus.PENDING -> Orange.copy(alpha = 0.1f)
                                BookingStatus.CONFIRMED -> Color.Green.copy(alpha = 0.1f)
                                BookingStatus.CANCELLED -> Color.Red.copy(alpha = 0.1f)
                                BookingStatus.COMPLETED -> Color.Blue.copy(alpha = 0.1f)
                            },
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = booking.status.name,
                        fontSize = 10.sp,
                        color = when (booking.status) {
                            BookingStatus.PENDING -> Orange
                            BookingStatus.CONFIRMED -> Color.Green
                            BookingStatus.CANCELLED -> Color.Red
                            BookingStatus.COMPLETED -> Color.Blue
                        },
                        fontFamily = Helvetica
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${booking.vehicleName} • ${booking.startDate} to ${booking.endDate}",
                fontSize = 12.sp,
                color = Color.Gray,
                fontFamily = Helvetica
            )
            Text(
                text = "₱${String.format("%,.0f", booking.totalAmount)}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Orange,
                fontFamily = Helvetica
            )
        }
    }
}

@Composable
fun UserItem(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    fontFamily = Helvetica
                )
                Text(
                    text = user.email,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = Helvetica
                )
                Text(
                    text = "${user.totalBookings} bookings • Joined ${user.registrationDate}",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    fontFamily = Helvetica
                )
            }
            Box(
                modifier = Modifier
                    .background(
                        if (user.isActive) Color.Green.copy(alpha = 0.1f) else Color.Red.copy(alpha = 0.1f),
                        RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (user.isActive) "Active" else "Inactive",
                    fontSize = 10.sp,
                    color = if (user.isActive) Color.Green else Color.Red,
                    fontFamily = Helvetica
                )
            }
        }
    }
}

// Sample Data Functions
fun getSampleBrands(): List<VehicleBrand> = listOf(
    VehicleBrand("1", "Toyota", "", 12, true),
    VehicleBrand("2", "Honda", "", 8, true),
    VehicleBrand("3", "Nissan", "", 6, true),
    VehicleBrand("4", "Mitsubishi", "", 5, true),
    VehicleBrand("5", "Ford", "", 4, true),
    VehicleBrand("6", "BMW", "", 3, false)
)

fun getSampleBookings(): List<Booking> = listOf(
    Booking("1", "u1", "Juan Dela Cruz", "v1", "Toyota Vios", "2024-03-15", "2024-03-18", 5400.0, BookingStatus.PENDING, "2024-03-10"),
    Booking("2", "u2", "Maria Santos", "v2", "Honda CR-V", "2024-03-20", "2024-03-25", 17500.0, BookingStatus.CONFIRMED, "2024-03-12"),
    Booking("3", "u3", "Jose Rizal", "v3", "BMW 3 Series", "2024-03-12", "2024-03-14", 13000.0, BookingStatus.COMPLETED, "2024-03-08")
)

fun getSampleUsers(): List<User> = listOf(
    User("1", "Juan Dela Cruz", "juan@email.com", "+63 912 345 6789", "2024-01-15", 5, true),
    User("2", "Maria Santos", "maria@email.com", "+63 998 765 4321", "2024-02-10", 3, true),
    User("3", "Jose Rizal", "jose@email.com", "+63 917 123 4567", "2024-03-01", 1, true)
)

data class StatCard(
    val title: String,
    val value: String,
    val icon: ImageVector,
    val color: Color
)

@Preview(showBackground = true)
@Composable
fun AdminDashboardPreview() {
    AdminDashboard()
}