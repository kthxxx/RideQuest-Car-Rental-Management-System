package com.example.ridequestcarrentalapp.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ridequestcarrentalapp.R
import com.example.ridequestcarrentalapp.ui.theme.Helvetica
import com.example.ridequestcarrentalapp.ui.theme.Orange

@Composable
fun PersonalInformationScreen(
    userProfile: CompleteUserProfile,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
) {
    val completionPercentage = userProfile.getProfileCompletionPercentage()

    Scaffold(
        topBar = {
            PersonalInfoTopBar(
                onBackClick = onBackClick,
                onEditClick = onEditClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Profile Completion Card
            ProfileCompletionCard(completionPercentage)

            Spacer(modifier = Modifier.height(20.dp))

            // Basic Information Section
            InfoSection(
                title = "Basic Information",
                icon = Icons.Default.Person
            ) {
                InfoRow("Full Name", userProfile.name, Icons.Default.Person)
                InfoRow("Email", userProfile.email, Icons.Default.Email)
                InfoRow("Phone Number", userProfile.phone, Icons.Default.Phone)
                InfoRow("Date of Birth", userProfile.dateOfBirth.ifEmpty { "Not provided" }, Icons.Default.Cake)
                InfoRow("Gender", userProfile.gender.ifEmpty { "Not provided" }, Icons.Default.Wc)
                if (userProfile.age > 0) {
                    InfoRow("Age", "${userProfile.age} years old", Icons.Default.CalendarToday)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Address Information Section
            InfoSection(
                title = "Address Information",
                icon = Icons.Default.Home
            ) {
                if (userProfile.streetAddress.isNotEmpty()) {
                    InfoRow("Street Address", userProfile.streetAddress, Icons.Default.LocationOn)
                }
                if (userProfile.barangay.isNotEmpty()) {
                    InfoRow("Barangay", userProfile.barangay, Icons.Default.Place)
                }
                if (userProfile.city.isNotEmpty()) {
                    InfoRow("City", userProfile.city, Icons.Default.LocationCity)
                }
                if (userProfile.province.isNotEmpty()) {
                    InfoRow("Province", userProfile.province, Icons.Default.Map)
                }
                if (userProfile.postalCode.isNotEmpty()) {
                    InfoRow("Postal Code", userProfile.postalCode, Icons.Default.Markunread)
                }

                if (userProfile.getFullAddress().isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Orange.copy(alpha = 0.1f))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Orange,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = userProfile.getFullAddress(),
                                fontSize = 13.sp,
                                color = Color.Black,
                                fontFamily = Helvetica
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Emergency Contact Section
            InfoSection(
                title = "Emergency Contact",
                icon = Icons.Default.ContactEmergency
            ) {
                if (userProfile.emergencyContactName.isNotEmpty()) {
                    InfoRow("Contact Name", userProfile.emergencyContactName, Icons.Default.Person)
                }
                if (userProfile.emergencyContactNumber.isNotEmpty()) {
                    InfoRow("Contact Number", userProfile.emergencyContactNumber, Icons.Default.Phone)
                }
                if (userProfile.emergencyContactRelationship.isNotEmpty()) {
                    InfoRow("Relationship", userProfile.emergencyContactRelationship, Icons.Default.FamilyRestroom)
                }

                if (userProfile.emergencyContactName.isEmpty() && userProfile.emergencyContactNumber.isEmpty()) {
                    EmptyStateMessage("No emergency contact added yet")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Driver's License Section
            InfoSection(
                title = "Driver's License",
                icon = Icons.Default.CreditCard
            ) {
                if (userProfile.licenseNumber.isNotEmpty()) {
                    InfoRow("License Number", userProfile.licenseNumber, Icons.Default.Badge)
                    InfoRow("License Type", userProfile.licenseType, Icons.Default.Category)
                    InfoRow("Issue Date", userProfile.licenseIssueDate, Icons.Default.CalendarToday)
                    InfoRow("Expiry Date", userProfile.licenseExpiryDate, Icons.Default.Event)

                    Spacer(modifier = Modifier.height(12.dp))

                    // License Status Badge
                    LicenseStatusBadge(userProfile.licenseStatus)

                    // License Photos
                    if (userProfile.licenseFrontPhoto.isNotEmpty() || userProfile.licenseBackPhoto.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "License Photos",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = Helvetica
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (userProfile.licenseFrontPhoto.isNotEmpty()) {
                                LicensePhotoCard("Front", Modifier.weight(1f))
                            }
                            if (userProfile.licenseBackPhoto.isNotEmpty()) {
                                LicensePhotoCard("Back", Modifier.weight(1f))
                            }
                        }
                    }
                } else {
                    EmptyStateMessage("No driver's license added yet")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Account Information Section
            InfoSection(
                title = "Account Information",
                icon = Icons.Default.AccountCircle
            ) {
                InfoRow("Member Since", userProfile.memberSince, Icons.Default.CalendarToday)
                InfoRow("Membership Tier", userProfile.membershipTier.name, Icons.Default.Star)
                InfoRow("Verification Status", userProfile.verificationStatus.name,
                    if (userProfile.verificationStatus == VerificationStatus.VERIFIED) Icons.Default.CheckCircle else Icons.Default.PendingActions
                )
                InfoRow("Total Bookings", userProfile.totalBookings.toString(), Icons.Default.DirectionsCar)
                InfoRow("Loyalty Points", userProfile.loyaltyPoints.toString(), Icons.Default.Loyalty)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Edit Profile Button
            Button(
                onClick = onEditClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Orange),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Edit Personal Information",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Helvetica
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PersonalInfoTopBar(
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(40.dp)
                .background(Color.Gray.copy(alpha = 0.1f), CircleShape)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
        }

        Text(
            text = "Personal Information",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontFamily = Helvetica
        )

        IconButton(
            onClick = onEditClick,
            modifier = Modifier
                .size(40.dp)
                .background(Orange.copy(alpha = 0.1f), CircleShape)
        ) {
            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Orange)
        }
    }
}

@Composable
private fun ProfileCompletionCard(percentage: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (percentage == 100) Color.Green.copy(alpha = 0.1f) else Orange.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Profile Completion",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Helvetica
                )
                Text(
                    text = "$percentage%",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (percentage == 100) Color.Green else Orange,
                    fontFamily = Helvetica
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { percentage / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = if (percentage == 100) Color.Green else Orange,
                trackColor = Color.Gray.copy(alpha = 0.2f)
            )

            if (percentage < 100) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Complete your profile to access all features",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = Helvetica
                )
            }
        }
    }
}

@Composable
private fun InfoSection(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Orange,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = Helvetica
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(12.dp))

            content()
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Gray,
                fontFamily = Helvetica
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                fontFamily = Helvetica
            )
        }
    }
}

@Composable
private fun LicenseStatusBadge(status: LicenseStatus) {
    val (bgColor, textColor, text, icon) = when (status) {
        LicenseStatus.VERIFIED -> Quad(
            Color.Green.copy(alpha = 0.1f),
            Color.Green,
            "Verified",
            Icons.Default.CheckCircle
        )
        LicenseStatus.PENDING -> Quad(
            Orange.copy(alpha = 0.1f),
            Orange,
            "Pending Verification",
            Icons.Default.PendingActions
        )
        LicenseStatus.EXPIRED -> Quad(
            Color.Red.copy(alpha = 0.1f),
            Color.Red,
            "Expired",
            Icons.Default.ErrorOutline
        )
        LicenseStatus.NOT_SUBMITTED -> Quad(
            Color.Gray.copy(alpha = 0.1f),
            Color.Gray,
            "Not Submitted",
            Icons.Default.Info
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = textColor,
                fontFamily = Helvetica
            )
        }
    }
}

@Composable
private fun LicensePhotoCard(label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = label,
                    tint = Color.Gray,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                fontFamily = Helvetica
            )
        }
    }
}

@Composable
private fun EmptyStateMessage(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = message,
            fontSize = 14.sp,
            color = Color.Gray,
            fontFamily = Helvetica
        )
    }
}

// Helper data class for quad values
private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)