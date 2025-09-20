package com.example.ridequestcarrentalapp.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ridequestcarrentalapp.R
import com.example.ridequestcarrentalapp.ui.admin.BookingStatus
import com.example.ridequestcarrentalapp.ui.theme.Helvetica
import com.example.ridequestcarrentalapp.ui.theme.Orange

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val profileImage: String = "",
    val memberSince: String,
    val totalBookings: Int,
    val favoriteLocations: List<String>,
    val verificationStatus: VerificationStatus,
    val loyaltyPoints: Int,
    val membershipTier: MembershipTier
)

enum class VerificationStatus {
    VERIFIED, PENDING, UNVERIFIED
}

enum class MembershipTier {
    BRONZE, SILVER, GOLD, PLATINUM
}

data class UserBooking(
    val id: String,
    val carName: String,
    val carImage: Int,
    val pickupDate: String,
    val returnDate: String,
    val location: String,
    val totalAmount: Double,
    val status: BookingStatus,
    val bookingDate: String
)

data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val subtitle: String? = null,
    val hasNotification: Boolean = false,
    val onClick: () -> Unit = {}
)

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {},
    onEditProfileClick: () -> Unit = {},
    onBookingHistoryClick: () -> Unit = {},
    onPaymentMethodsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onHelpSupportClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    // Sample user data
    val userProfile = UserProfile(
        id = "user123",
        name = "Joshua Miguel Jamisola",
        email = "juan.delacruz@email.com",
        phone = "+63 912 345 6789",
        memberSince = "January 2023",
        totalBookings = 15,
        favoriteLocations = listOf("Cebu City", "Mactan Airport", "IT Park"),
        verificationStatus = VerificationStatus.VERIFIED,
        loyaltyPoints = 2450,
        membershipTier = MembershipTier.GOLD
    )

    val recentBookings = listOf(
        UserBooking("1", "Toyota Vios", R.drawable.white_back_logo, "Mar 15, 2024", "Mar 18, 2024", "Cebu City", 5400.0, BookingStatus.COMPLETED, "Mar 10, 2024"),
        UserBooking("2", "Honda CR-V", R.drawable.white_back_logo, "Mar 25, 2024", "Mar 27, 2024", "Mactan Airport", 7000.0, BookingStatus.COMPLETED, "Mar 20, 2024"),
        UserBooking("3", "Mitsubishi Xpander", R.drawable.white_back_logo, "Apr 05, 2024", "Apr 08, 2024", "IT Park", 7500.0, BookingStatus.CONFIRMED, "Apr 01, 2024")
    )

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
                text = "Profile",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = Helvetica
            )

            IconButton(
                onClick = { onEditProfileClick() },
                modifier = Modifier
                    .size(40.dp)
                    .background(Orange.copy(alpha = 0.1f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Orange
                )
            }
        }

        // Tab Row
        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.White,
            contentColor = Orange
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        text = "Profile",
                        fontFamily = Helvetica,
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        text = "Bookings",
                        fontFamily = Helvetica,
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
        }

        // Tab Content
        when (selectedTab) {
            0 -> ProfileTab(
                userProfile = userProfile,
                onEditProfileClick = onEditProfileClick,
                onPaymentMethodsClick = onPaymentMethodsClick,
                onNotificationsClick = onNotificationsClick,
                onHelpSupportClick = onHelpSupportClick,
                onSettingsClick = onSettingsClick,
                onLogoutClick = onLogoutClick
            )
            1 -> BookingsTab(
                bookings = recentBookings,
                onBookingClick = { booking ->
                    // Handle booking click
                },
                onBookingHistoryClick = onBookingHistoryClick
            )
        }
    }
}

@Composable
fun ProfileTab(
    userProfile: UserProfile,
    onEditProfileClick: () -> Unit,
    onPaymentMethodsClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onHelpSupportClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // User Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Orange.copy(alpha = 0.05f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Image
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(Orange, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (userProfile.profileImage.isEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = Color.White,
                                modifier = Modifier.size(40.dp)
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.white_back_logo),
                                contentDescription = "Profile",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        }

                        // Verification Badge
                        if (userProfile.verificationStatus == VerificationStatus.VERIFIED) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(Color.Green, CircleShape)
                                    .align(Alignment.BottomEnd),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Verified",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = userProfile.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = Helvetica
                    )

                    Text(
                        text = userProfile.email,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontFamily = Helvetica
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Membership Tier Badge
                    Box(
                        modifier = Modifier
                            .background(
                                when (userProfile.membershipTier) {
                                    MembershipTier.BRONZE -> Color(0xFFCD7F32)
                                    MembershipTier.SILVER -> Color(0xFFC0C0C0)
                                    MembershipTier.GOLD -> Color(0xFFFFD700)
                                    MembershipTier.PLATINUM -> Color(0xFFE5E4E2)
                                }.copy(alpha = 0.2f),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${userProfile.membershipTier} Member",
                            fontSize = 12.sp,
                            color = when (userProfile.membershipTier) {
                                MembershipTier.BRONZE -> Color(0xFFCD7F32)
                                MembershipTier.SILVER -> Color(0xFF666666)
                                MembershipTier.GOLD -> Color(0xFFB8860B)
                                MembershipTier.PLATINUM -> Color(0xFF666666)
                            },
                            fontWeight = FontWeight.Medium,
                            fontFamily = Helvetica
                        )
                    }
                }
            }
        }

        item {
            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Total Trips",
                    value = userProfile.totalBookings.toString(),
                    icon = Icons.Default.DirectionsCar,
                    color = Orange,
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    title = "Loyalty Points",
                    value = "${userProfile.loyaltyPoints}",
                    icon = Icons.Default.Star,
                    color = Color(0xFFFFD700),
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    title = "Member Since",
                    value = userProfile.memberSince.split(" ")[0],
                    icon = Icons.Default.CalendarToday,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            // Account Section
            Text(
                text = "Account",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = Helvetica
            )
        }

        items(
            listOf(
                MenuItem("Personal Information", Icons.Default.Person, "Update your profile details") { onEditProfileClick() },
                MenuItem("Payment Methods", Icons.Default.CreditCard, "Manage cards and payment options") { onPaymentMethodsClick() },
                MenuItem("Notification Settings", Icons.Default.Notifications, "Customize your alerts", hasNotification = true) { onNotificationsClick() }
            )
        ) { menuItem ->
            MenuItemCard(menuItem)
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            // Support Section
            Text(
                text = "Support",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = Helvetica
            )
        }

        items(
            listOf(
                MenuItem("Help & Support", Icons.Default.Help, "Get assistance and FAQs") { onHelpSupportClick() },
                MenuItem("Settings", Icons.Default.Settings, "App preferences and privacy") { onSettingsClick() },
                MenuItem("Rate the App", Icons.Default.Star, "Share your feedback"),
                MenuItem("Share with Friends", Icons.Default.Share, "Invite friends to RideQuest")
            )
        ) { menuItem ->
            MenuItemCard(menuItem)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            // Logout Button
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLogoutClick() },
                colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.05f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        tint = Color.Red,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Logout",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Red,
                        fontFamily = Helvetica
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            // App Version
            Text(
                text = "RideQuest v1.0.0",
                fontSize = 12.sp,
                color = Color.Gray,
                fontFamily = Helvetica,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun BookingsTab(
    bookings: List<UserBooking>,
    onBookingClick: (UserBooking) -> Unit,
    onBookingHistoryClick: () -> Unit
) {
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
                    text = "Recent Bookings",
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
                    modifier = Modifier.clickable { onBookingHistoryClick() }
                )
            }
        }

        if (bookings.isEmpty()) {
            item {
                EmptyBookingsState()
            }
        } else {
            items(bookings) { booking ->
                BookingCard(
                    booking = booking,
                    onClick = { onBookingClick(booking) }
                )
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = Helvetica
            )
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.Gray,
                fontFamily = Helvetica,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MenuItemCard(menuItem: MenuItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { menuItem.onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = menuItem.icon,
                contentDescription = menuItem.title,
                tint = Orange,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = menuItem.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        fontFamily = Helvetica
                    )

                    if (menuItem.hasNotification) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color.Red, CircleShape)
                        )
                    }
                }

                menuItem.subtitle?.let { subtitle ->
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = Helvetica
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Go",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun BookingCard(
    booking: UserBooking,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Car Image
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = booking.carImage),
                    contentDescription = booking.carName,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = booking.carName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = Helvetica
                )

                Text(
                    text = "${booking.pickupDate} - ${booking.returnDate}",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = Helvetica
                )

                Text(
                    text = booking.location,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = Helvetica
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₱${String.format("%,.0f", booking.totalAmount)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Orange,
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
                                    //else -> {}  // This returns Unit, not Color
                                },
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = booking.status.name.lowercase().replaceFirstChar { it.uppercase() },
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
            }
        }
    }
}

@Composable
fun EmptyBookingsState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.EventNote,
            contentDescription = "No bookings",
            tint = Color.Gray.copy(alpha = 0.5f),
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No Bookings Yet",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            fontFamily = Helvetica
        )

        Text(
            text = "Your rental history will appear here",
            fontSize = 14.sp,
            color = Color.Gray.copy(alpha = 0.7f),
            fontFamily = Helvetica,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* Navigate to car listings */ },
            colors = ButtonDefaults.buttonColors(containerColor = Orange)
        ) {
            Text(
                text = "Browse Cars",
                fontWeight = FontWeight.Medium,
                fontFamily = Helvetica
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}