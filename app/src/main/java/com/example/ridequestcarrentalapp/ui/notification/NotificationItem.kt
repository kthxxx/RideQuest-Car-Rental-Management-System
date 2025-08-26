package com.example.ridequestcarrentalapp.ui.notifications

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ridequestcarrentalapp.ui.theme.Helvetica
import com.example.ridequestcarrentalapp.ui.theme.Orange

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val timestamp: String,
    val isRead: Boolean = false,
    val actionRequired: Boolean = false,
    val relatedBookingId: String? = null
)

enum class NotificationType {
    BOOKING_CONFIRMED,
    BOOKING_REMINDER,
    BOOKING_CANCELLED,
    PAYMENT_SUCCESS,
    PAYMENT_FAILED,
    PROMOTION,
    SYSTEM_UPDATE,
    SUPPORT_RESPONSE
}

@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit = {},
    onNotificationClick: (NotificationItem) -> Unit = {},
    onMarkAllReadClick: () -> Unit = {},
    onNotificationSettingsClick: () -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf("All") }
    var notifications by remember { mutableStateOf(getSampleNotifications()) }

    val filters = listOf("All", "Bookings", "Payments", "Promotions", "System")

    // Filter notifications based on selected filter
    val filteredNotifications = when (selectedFilter) {
        "All" -> notifications
        "Bookings" -> notifications.filter {
            it.type in listOf(NotificationType.BOOKING_CONFIRMED, NotificationType.BOOKING_REMINDER, NotificationType.BOOKING_CANCELLED)
        }
        "Payments" -> notifications.filter {
            it.type in listOf(NotificationType.PAYMENT_SUCCESS, NotificationType.PAYMENT_FAILED)
        }
        "Promotions" -> notifications.filter { it.type == NotificationType.PROMOTION }
        "System" -> notifications.filter {
            it.type in listOf(NotificationType.SYSTEM_UPDATE, NotificationType.SUPPORT_RESPONSE)
        }
        else -> notifications
    }

    val unreadCount = notifications.count { !it.isRead }

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

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Notifications",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = Helvetica
                )
                if (unreadCount > 0) {
                    Text(
                        text = "$unreadCount unread",
                        fontSize = 12.sp,
                        color = Orange,
                        fontFamily = Helvetica
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (unreadCount > 0) {
                    IconButton(
                        onClick = {
                            notifications = notifications.map { it.copy(isRead = true) }
                            onMarkAllReadClick()
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Orange.copy(alpha = 0.1f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = "Mark all read",
                            tint = Orange,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                IconButton(
                    onClick = { onNotificationSettingsClick() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Gray.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Filter Chips
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filters) { filter ->
                val isSelected = filter == selectedFilter
                val filterCount = when (filter) {
                    "All" -> notifications.size
                    "Bookings" -> notifications.count { it.type in listOf(NotificationType.BOOKING_CONFIRMED, NotificationType.BOOKING_REMINDER, NotificationType.BOOKING_CANCELLED) }
                    "Payments" -> notifications.count { it.type in listOf(NotificationType.PAYMENT_SUCCESS, NotificationType.PAYMENT_FAILED) }
                    "Promotions" -> notifications.count { it.type == NotificationType.PROMOTION }
                    "System" -> notifications.count { it.type in listOf(NotificationType.SYSTEM_UPDATE, NotificationType.SUPPORT_RESPONSE) }
                    else -> 0
                }

                FilterChip(
                    selected = isSelected,
                    onClick = { selectedFilter = filter },
                    label = {
                        Text(
                            text = "$filter ($filterCount)",
                            fontSize = 12.sp,
                            fontFamily = Helvetica
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Orange,
                        selectedLabelColor = Color.White,
                        containerColor = Color.Gray.copy(alpha = 0.1f),
                        labelColor = Color.Gray
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Notifications List
        if (filteredNotifications.isEmpty()) {
            EmptyNotificationsState(selectedFilter)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(filteredNotifications) { notification ->
                    NotificationCard(
                        notification = notification,
                        onClick = {
                            // Mark as read when clicked
                            notifications = notifications.map {
                                if (it.id == notification.id) it.copy(isRead = true) else it
                            }
                            onNotificationClick(notification)
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun NotificationCard(
    notification: NotificationItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (!notification.isRead) Orange.copy(alpha = 0.05f) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Notification Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        getNotificationColor(notification.type).copy(alpha = 0.1f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getNotificationIcon(notification.type),
                    contentDescription = notification.type.name,
                    tint = getNotificationColor(notification.type),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = notification.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        fontFamily = Helvetica,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (!notification.isRead) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Orange, CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = notification.message,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = Helvetica,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.timestamp,
                        fontSize = 12.sp,
                        color = Color.Gray.copy(alpha = 0.7f),
                        fontFamily = Helvetica
                    )

                    if (notification.actionRequired) {
                        Text(
                            text = "Action Required",
                            fontSize = 10.sp,
                            color = Color.Red,
                            fontWeight = FontWeight.Medium,
                            fontFamily = Helvetica,
                            modifier = Modifier
                                .background(
                                    Color.Red.copy(alpha = 0.1f),
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyNotificationsState(filter: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "No notifications",
            tint = Color.Gray.copy(alpha = 0.5f),
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (filter == "All") "No Notifications" else "No $filter Notifications",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            fontFamily = Helvetica
        )

        Text(
            text = "You're all caught up! New notifications will appear here.",
            fontSize = 14.sp,
            color = Color.Gray.copy(alpha = 0.7f),
            fontFamily = Helvetica,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = { /* Refresh notifications */ },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Orange),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = androidx.compose.ui.graphics.SolidColor(Orange)
            )
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh",
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Refresh",
                fontWeight = FontWeight.Medium,
                fontFamily = Helvetica
            )
        }
    }
}

fun getNotificationIcon(type: NotificationType): ImageVector {
    return when (type) {
        NotificationType.BOOKING_CONFIRMED -> Icons.Default.CheckCircle
        NotificationType.BOOKING_REMINDER -> Icons.Default.EventAvailable
        NotificationType.BOOKING_CANCELLED -> Icons.Default.Cancel
        NotificationType.PAYMENT_SUCCESS -> Icons.Default.Payment
        NotificationType.PAYMENT_FAILED -> Icons.Default.Error
        NotificationType.PROMOTION -> Icons.Default.LocalOffer
        NotificationType.SYSTEM_UPDATE -> Icons.Default.SystemUpdate
        NotificationType.SUPPORT_RESPONSE -> Icons.Default.Support
    }
}

fun getNotificationColor(type: NotificationType): Color {
    return when (type) {
        NotificationType.BOOKING_CONFIRMED -> Color.Green
        NotificationType.BOOKING_REMINDER -> Color(0xFF2196F3)
        NotificationType.BOOKING_CANCELLED -> Color.Red
        NotificationType.PAYMENT_SUCCESS -> Color.Green
        NotificationType.PAYMENT_FAILED -> Color.Red
        NotificationType.PROMOTION -> Color(0xFFFF9800)
        NotificationType.SYSTEM_UPDATE -> Color(0xFF9C27B0)
        NotificationType.SUPPORT_RESPONSE -> Color(0xFF607D8B)
    }
}

fun getSampleNotifications(): List<NotificationItem> {
    return listOf(
        NotificationItem(
            id = "1",
            title = "Booking Confirmed!",
            message = "Your Toyota Vios booking for Mar 15-18 has been confirmed. Pickup location: Cebu City Center.",
            type = NotificationType.BOOKING_CONFIRMED,
            timestamp = "2 hours ago",
            isRead = false,
            relatedBookingId = "booking123"
        ),
        NotificationItem(
            id = "2",
            title = "Payment Successful",
            message = "Your payment of ₱5,400 has been processed successfully for booking #BK123.",
            type = NotificationType.PAYMENT_SUCCESS,
            timestamp = "3 hours ago",
            isRead = false
        ),
        NotificationItem(
            id = "3",
            title = "Pickup Reminder",
            message = "Don't forget! Your car pickup is tomorrow at 10:00 AM. Please bring a valid driver's license.",
            type = NotificationType.BOOKING_REMINDER,
            timestamp = "5 hours ago",
            isRead = true,
            actionRequired = true,
            relatedBookingId = "booking124"
        ),
        NotificationItem(
            id = "4",
            title = "Special Weekend Offer!",
            message = "Get 20% off on all SUVs this weekend. Book now and save on your next adventure!",
            type = NotificationType.PROMOTION,
            timestamp = "1 day ago",
            isRead = true
        ),
        NotificationItem(
            id = "5",
            title = "Booking Cancelled",
            message = "Your Honda CR-V booking for Mar 25-27 has been cancelled as requested. Full refund processed.",
            type = NotificationType.BOOKING_CANCELLED,
            timestamp = "2 days ago",
            isRead = true,
            relatedBookingId = "booking125"
        ),
        NotificationItem(
            id = "6",
            title = "App Update Available",
            message = "Version 1.1.0 is now available with new features and improvements. Update now for the best experience.",
            type = NotificationType.SYSTEM_UPDATE,
            timestamp = "3 days ago",
            isRead = true
        ),
        NotificationItem(
            id = "7",
            title = "Support Response",
            message = "We've responded to your inquiry about insurance coverage. Check your messages for details.",
            type = NotificationType.SUPPORT_RESPONSE,
            timestamp = "4 days ago",
            isRead = true,
            actionRequired = true
        ),
        NotificationItem(
            id = "8",
            title = "Payment Failed",
            message = "Your payment for booking #BK126 could not be processed. Please update your payment method.",
            type = NotificationType.PAYMENT_FAILED,
            timestamp = "5 days ago",
            isRead = true,
            actionRequired = true,
            relatedBookingId = "booking126"
        ),
        NotificationItem(
            id = "9",
            title = "Early Bird Special!",
            message = "Book 7 days in advance and get 15% off on all economy cars. Limited time offer!",
            type = NotificationType.PROMOTION,
            timestamp = "1 week ago",
            isRead = true
        ),
        NotificationItem(
            id = "10",
            title = "Booking Reminder",
            message = "Your car return is due today at 10:00 AM. Please ensure the fuel tank is full.",
            type = NotificationType.BOOKING_REMINDER,
            timestamp = "1 week ago",
            isRead = true,
            relatedBookingId = "booking127"
        )
    )
}

@Preview(showBackground = true)
@Composable
fun NotificationsScreenPreview() {
    NotificationsScreen()
}