package com.example.ridequestcarrentalapp.ui.profile

import android.app.DatePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.ridequestcarrentalapp.R
import com.example.ridequestcarrentalapp.ui.theme.Helvetica
import com.example.ridequestcarrentalapp.ui.theme.Orange
import java.text.SimpleDateFormat
import java.util.*

// Edit Profile Data
data class EditableUserProfile(
    var profileImageUri: Uri? = null,
    var fullName: String = "",
    var email: String = "",
    var phone: String = "",
    var dateOfBirth: String = "",
    var gender: String = "",
    var streetAddress: String = "",
    var barangay: String = "",
    var city: String = "",
    var province: String = "",
    var postalCode: String = "",
    var emergencyContactName: String = "",
    var emergencyContactNumber: String = "",
    var licenseNumber: String = "",
    var licenseType: String = "Non-Professional",
    var licenseIssueDate: String = "",
    var licenseExpiryDate: String = "",
    var licenseFrontUri: Uri? = null,
    var licenseBackUri: Uri? = null
)

@Composable
fun EditProfileScreen(
    userProfile: UserProfile,
    onBackClick: () -> Unit,
    onSaveClick: (EditableUserProfile) -> Unit
) {
    var editableProfile by remember {
        mutableStateOf(
            EditableUserProfile(
                fullName = userProfile.name,
                email = userProfile.email,
                phone = userProfile.phone
            )
        )
    }
    var showSaveDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            EditProfileTopBar(
                onBackClick = onBackClick,
                onSaveClick = { showSaveDialog = true }
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
            // Profile Picture Section
            ProfilePictureSection(
                profileImageUri = editableProfile.profileImageUri,
                onImageSelected = { editableProfile = editableProfile.copy(profileImageUri = it) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Basic Information
            SectionTitle("Basic Information")
            Spacer(modifier = Modifier.height(12.dp))

            EditTextField(
                value = editableProfile.fullName,
                onValueChange = { editableProfile = editableProfile.copy(fullName = it) },
                label = "Full Name",
                icon = Icons.Default.Person
            )

            Spacer(modifier = Modifier.height(12.dp))

            EditTextField(
                value = editableProfile.email,
                onValueChange = { editableProfile = editableProfile.copy(email = it) },
                label = "Email Address",
                icon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(12.dp))

            EditTextField(
                value = editableProfile.phone,
                onValueChange = { editableProfile = editableProfile.copy(phone = it) },
                label = "Phone Number",
                icon = Icons.Default.Phone,
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Date of Birth
            DatePickerField(
                value = editableProfile.dateOfBirth,
                onValueChange = { editableProfile = editableProfile.copy(dateOfBirth = it) },
                label = "Date of Birth"
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Gender Selection
            Text(
                text = "Gender",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = Helvetica
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listOf("Male", "Female", "Other").forEach { gender ->
                    FilterChip(
                        selected = editableProfile.gender == gender,
                        onClick = { editableProfile = editableProfile.copy(gender = gender) },
                        label = { Text(gender) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Orange,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Address Information
            SectionTitle("Address Information")
            Spacer(modifier = Modifier.height(12.dp))

            EditTextField(
                value = editableProfile.streetAddress,
                onValueChange = { editableProfile = editableProfile.copy(streetAddress = it) },
                label = "Street Address",
                icon = Icons.Default.Home
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                EditTextField(
                    value = editableProfile.barangay,
                    onValueChange = { editableProfile = editableProfile.copy(barangay = it) },
                    label = "Barangay",
                    modifier = Modifier.weight(1f)
                )
                EditTextField(
                    value = editableProfile.city,
                    onValueChange = { editableProfile = editableProfile.copy(city = it) },
                    label = "City",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                EditTextField(
                    value = editableProfile.province,
                    onValueChange = { editableProfile = editableProfile.copy(province = it) },
                    label = "Province",
                    modifier = Modifier.weight(1f)
                )
                EditTextField(
                    value = editableProfile.postalCode,
                    onValueChange = { editableProfile = editableProfile.copy(postalCode = it) },
                    label = "Postal Code",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Emergency Contact
            SectionTitle("Emergency Contact")
            Spacer(modifier = Modifier.height(12.dp))

            EditTextField(
                value = editableProfile.emergencyContactName,
                onValueChange = { editableProfile = editableProfile.copy(emergencyContactName = it) },
                label = "Contact Name",
                icon = Icons.Default.ContactEmergency
            )

            Spacer(modifier = Modifier.height(12.dp))

            EditTextField(
                value = editableProfile.emergencyContactNumber,
                onValueChange = { editableProfile = editableProfile.copy(emergencyContactNumber = it) },
                label = "Contact Number",
                icon = Icons.Default.Phone,
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Driver's License
            SectionTitle("Driver's License")
            Spacer(modifier = Modifier.height(12.dp))

            EditTextField(
                value = editableProfile.licenseNumber,
                onValueChange = { editableProfile = editableProfile.copy(licenseNumber = it.uppercase()) },
                label = "License Number",
                icon = Icons.Default.CreditCard
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "License Type", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf("Non-Professional", "Professional").forEach { type ->
                    FilterChip(
                        selected = editableProfile.licenseType == type,
                        onClick = { editableProfile = editableProfile.copy(licenseType = type) },
                        label = { Text(type, fontSize = 13.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Orange,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DatePickerField(
                    value = editableProfile.licenseIssueDate,
                    onValueChange = { editableProfile = editableProfile.copy(licenseIssueDate = it) },
                    label = "Issue Date",
                    modifier = Modifier.weight(1f),
                    maxDate = System.currentTimeMillis()
                )

                DatePickerField(
                    value = editableProfile.licenseExpiryDate,
                    onValueChange = { editableProfile = editableProfile.copy(licenseExpiryDate = it) },
                    label = "Expiry Date",
                    modifier = Modifier.weight(1f),
                    minDate = System.currentTimeMillis()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "License Photos", fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = Helvetica)
            Spacer(modifier = Modifier.height(12.dp))

            LicensePhotoUpload(
                label = "Front Side",
                uri = editableProfile.licenseFrontUri,
                onUriSelected = { editableProfile = editableProfile.copy(licenseFrontUri = it) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            LicensePhotoUpload(
                label = "Back Side",
                uri = editableProfile.licenseBackUri,
                onUriSelected = { editableProfile = editableProfile.copy(licenseBackUri = it) }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Save Confirmation Dialog
    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Save Changes?", fontFamily = Helvetica) },
            text = { Text("Do you want to save your profile changes?", fontFamily = Helvetica) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onSaveClick(editableProfile)
                        showSaveDialog = false
                    }
                ) {
                    Text("Save", color = Orange, fontFamily = Helvetica)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("Cancel", fontFamily = Helvetica)
                }
            }
        )
    }
}

@Composable
private fun EditProfileTopBar(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
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
            text = "Edit Profile",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontFamily = Helvetica
        )

        TextButton(onClick = onSaveClick) {
            Text(
                text = "Save",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Orange,
                fontFamily = Helvetica
            )
        }
    }
}

@Composable
private fun ProfilePictureSection(
    profileImageUri: Uri?,
    onImageSelected: (Uri?) -> Unit
) {
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> onImageSelected(uri) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clickable { imagePicker.launch("image/*") }
        ) {
            if (profileImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(profileImageUri),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Orange, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color.White,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Orange, CircleShape)
                    .align(Alignment.BottomEnd),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Change Photo",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tap to change photo",
            fontSize = 12.sp,
            color = Color.Gray,
            fontFamily = Helvetica
        )
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        fontFamily = Helvetica
    )
}

@Composable
private fun EditTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = icon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = Orange
                )
            }
        },
        modifier = modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Orange,
            focusedLabelColor = Orange,
            focusedLeadingIconColor = Orange
        ),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}

@Composable
private fun DatePickerField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    maxDate: Long? = null,
    minDate: Long? = null
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            calendar.set(year, month, day)
            onValueChange(dateFormatter.format(calendar.time))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
        maxDate?.let { datePicker.maxDate = it }
        minDate?.let { datePicker.minDate = it }
    }

    OutlinedTextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        trailingIcon = {
            IconButton(onClick = { datePickerDialog.show() }) {
                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Orange)
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .clickable { datePickerDialog.show() },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Orange,
            focusedLabelColor = Orange
        ),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
private fun LicensePhotoUpload(
    label: String,
    uri: Uri?,
    onUriSelected: (Uri?) -> Unit
) {
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { selectedUri -> onUriSelected(selectedUri) }

    Column {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium, fontFamily = Helvetica)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .border(
                    2.dp,
                    if (uri != null) Orange else Color.Gray.copy(0.3f),
                    RoundedCornerShape(12.dp)
                )
                .clip(RoundedCornerShape(12.dp))
                .clickable { imagePicker.launch("image/*") }
                .background(Color.Gray.copy(0.05f)),
            contentAlignment = Alignment.Center
        ) {
            if (uri != null) {
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = label,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.AddAPhoto,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tap to upload",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = Helvetica
                    )
                }
            }
        }
    }
}