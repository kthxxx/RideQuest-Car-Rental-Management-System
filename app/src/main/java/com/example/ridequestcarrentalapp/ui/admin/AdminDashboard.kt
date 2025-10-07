package com.example.ridequestcarrentalapp.ui.admin

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ridequestcarrentalapp.ui.theme.Helvetica
import com.example.ridequestcarrentalapp.ui.theme.Orange
import java.text.SimpleDateFormat
import java.util.*

// Data Models
data class StatCard(
    val title: String,
    val value: String,
    val icon: ImageVector,
    val color: Color
)

data class BookingItem(
    val id: Long,
    val userName: String,
    val carName: String,
    val pickupDate: String,
    val returnDate: String,
    val totalAmount: Double,
    val status: BookingStatus,
    val paymentStatus: PaymentStatus
)

enum class BookingStatus(val displayName: String, val color: Color) {
    PENDING("Pending", Color(0xFFFF9800)),
    CONFIRMED("Confirmed", Color(0xFF4CAF50)),
    ONGOING("Ongoing", Color(0xFF2196F3)),
    COMPLETED("Completed", Color(0xFF009688)),
    CANCELLED("Cancelled", Color(0xFFF44336))
}

enum class PaymentStatus(val displayName: String, val color: Color) {
    PENDING("Pending", Color(0xFFFF9800)),
    PAID("Paid", Color(0xFF4CAF50)),
    REFUNDED("Refunded", Color(0xFF9C27B0)),
    FAILED("Failed", Color(0xFFF44336))
}

@SuppressLint("AutoboxingStateCreation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboard(
    bookings: List<BookingItem> = emptyList(),
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
    onLogoutClick: () -> Unit = {},
    onUpdateBookingStatus: (Long, BookingStatus) -> Unit = { _, _ -> }
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = listOf("Dashboard", "Vehicles", "Bookings", "Users", "Settings")

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
            0 -> DashboardOverview(bookings = bookings)
            1 -> VehicleManagement(
                onManageBrandsClick = onManageBrandsClick,
                onPostVehicleClick = onPostVehicleClick,
                onManageVehiclesClick = onManageVehiclesClick
            )
            2 -> BookingManagement(
                bookings = bookings,
                onManageBookingsClick = onManageBookingsClick,
                onUpdateBookingStatus = onUpdateBookingStatus
            )
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
fun DashboardOverview(bookings: List<BookingItem> = emptyList()) {
    val totalBookings = bookings.size
    val activeBookings = bookings.count { it.status == BookingStatus.CONFIRMED || it.status == BookingStatus.ONGOING }
    val pendingBookings = bookings.count { it.status == BookingStatus.PENDING }
    val completedBookings = bookings.count { it.status == BookingStatus.COMPLETED }
    val totalRevenue = bookings.filter { it.paymentStatus == PaymentStatus.PAID }.sumOf { it.totalAmount }

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
                        StatCard("Total Bookings", "$totalBookings", Icons.Default.BookOnline, Color(0xFF2196F3)),
                        StatCard("Active Bookings", "$activeBookings", Icons.Default.EventAvailable, Orange),
                        StatCard("Pending", "$pendingBookings", Icons.Default.HourglassEmpty, Color(0xFFFF9800)),
                        StatCard("Completed", "$completedBookings", Icons.Default.CheckCircle, Color(0xFF4CAF50)),
                        StatCard("Revenue", "₱${String.format("%,.2f", totalRevenue)}", Icons.Default.AttachMoney, Color(0xFF009688)),
                        StatCard("Avg. Booking", if (totalBookings > 0) "₱${String.format("%,.0f", totalRevenue / totalBookings)}" else "₱0", Icons.Default.TrendingUp, Color(0xFF9C27B0)),
                        StatCard("Total Vehicles", "0", Icons.Default.DirectionsCar, Color(0xFF673AB7)),
                        StatCard("Users", "0", Icons.Default.People, Color(0xFF4CAF50))
                    )
                ) { statCard ->
                    StatCardItem(statCard)
                }
            }
        }

        item {
            Text(
                text = "Recent Bookings",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = Helvetica,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        if (bookings.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.BookOnline,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = Color.Gray.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No bookings yet",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                fontFamily = Helvetica,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        } else {
            items(bookings.take(5)) { booking ->
                BookingCard(booking = booking, onClick = {})
            }
        }
    }
}

@Composable
fun BookingManagement(
    bookings: List<BookingItem> = emptyList(),
    onManageBookingsClick: () -> Unit,
    onUpdateBookingStatus: (Long, BookingStatus) -> Unit
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Pending", "Confirmed", "Ongoing", "Completed", "Cancelled")

    val filteredBookings = if (selectedFilter == "All") {
        bookings
    } else {
        bookings.filter { it.status.displayName == selectedFilter }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Booking Management",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = Helvetica
                )

                Text(
                    text = "${filteredBookings.size} bookings",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = Helvetica
                )
            }
        }

        // Filter Chips
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = {
                            Text(
                                text = filter,
                                fontSize = 12.sp,
                                fontFamily = Helvetica
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Orange,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        if (filteredBookings.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.BookOnline,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = Color.Gray.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No $selectedFilter bookings",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                fontFamily = Helvetica,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        } else {
            items(filteredBookings) { booking ->
                BookingCardWithActions(
                    booking = booking,
                    onUpdateStatus = { newStatus ->
                        onUpdateBookingStatus(booking.id, newStatus)
                    }
                )
            }
        }
    }
}

@Composable
fun BookingCard(
    booking: BookingItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = booking.carName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = Helvetica
                    )
                    Text(
                        text = booking.userName,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontFamily = Helvetica
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Surface(
                        color = booking.status.color.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = booking.status.displayName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = booking.status.color,
                            fontFamily = Helvetica,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Pickup",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = Helvetica
                    )
                    Text(
                        text = booking.pickupDate,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        fontFamily = Helvetica
                    )
                }

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Orange,
                    modifier = Modifier.size(20.dp)
                )

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Return",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = Helvetica
                    )
                    Text(
                        text = booking.returnDate,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        fontFamily = Helvetica
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Divider(color = Color.Gray.copy(alpha = 0.2f))

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Payment,
                        contentDescription = null,
                        tint = booking.paymentStatus.color,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = booking.paymentStatus.displayName,
                        fontSize = 12.sp,
                        color = booking.paymentStatus.color,
                        fontFamily = Helvetica
                    )
                }

                Text(
                    text = "₱${String.format("%,.2f", booking.totalAmount)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Orange,
                    fontFamily = Helvetica
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingCardWithActions(
    booking: BookingItem,
    onUpdateStatus: (BookingStatus) -> Unit
) {
    var showStatusMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Booking #${booking.id}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = Helvetica
                    )
                    Text(
                        text = booking.carName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = Helvetica
                    )
                    Text(
                        text = booking.userName,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontFamily = Helvetica
                    )
                }

                Box {
                    Surface(
                        color = booking.status.color.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.clickable { showStatusMenu = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = booking.status.displayName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = booking.status.color,
                                fontFamily = Helvetica
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Change Status",
                                tint = booking.status.color,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showStatusMenu,
                        onDismissRequest = { showStatusMenu = false }
                    ) {
                        BookingStatus.values().forEach { status ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .background(status.color, CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = status.displayName,
                                            fontFamily = Helvetica
                                        )
                                    }
                                },
                                onClick = {
                                    onUpdateStatus(status)
                                    showStatusMenu = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Pickup",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = Helvetica
                    )
                    Text(
                        text = booking.pickupDate,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        fontFamily = Helvetica
                    )
                }

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Orange,
                    modifier = Modifier.size(20.dp)
                )

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Return",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = Helvetica
                    )
                    Text(
                        text = booking.returnDate,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        fontFamily = Helvetica
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Divider(color = Color.Gray.copy(alpha = 0.2f))

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Payment,
                        contentDescription = null,
                        tint = booking.paymentStatus.color,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = booking.paymentStatus.displayName,
                        fontSize = 12.sp,
                        color = booking.paymentStatus.color,
                        fontFamily = Helvetica
                    )
                }

                Text(
                    text = "₱${String.format("%,.2f", booking.totalAmount)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Orange,
                    fontFamily = Helvetica
                )
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
                fontFamily = Helvetica,
                textAlign = TextAlign.Center
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

@Preview(showBackground = true)
@Composable
fun AdminDashboardPreview() {
    val sampleBookings = listOf(
        BookingItem(
            id = 1,
            userName = "Juan Dela Cruz",
            carName = "Toyota Fortuner",
            pickupDate = "Dec 15, 2024",
            returnDate = "Dec 20, 2024",
            totalAmount = 22500.0,
            status = BookingStatus.CONFIRMED,
            paymentStatus = PaymentStatus.PAID
        ),
        BookingItem(
            id = 2,
            userName = "Maria Santos",
            carName = "Honda CR-V",
            pickupDate = "Dec 18, 2024",
            returnDate = "Dec 22, 2024",
            totalAmount = 14000.0,
            status = BookingStatus.PENDING,
            paymentStatus = PaymentStatus.PENDING
        ),
        BookingItem(
            id = 3,
            userName = "Pedro Reyes",
            carName = "Toyota Vios",
            pickupDate = "Dec 10, 2024",
            returnDate = "Dec 14, 2024",
            totalAmount = 7200.0,
            status = BookingStatus.COMPLETED,
            paymentStatus = PaymentStatus.PAID
        )
    )

    AdminDashboard(bookings = sampleBookings)
}