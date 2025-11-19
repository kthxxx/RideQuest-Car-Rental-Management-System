package com.example.ridequestcarrentalapp.ui.login

import android.app.DatePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.ridequestcarrentalapp.R
import com.example.ridequestcarrentalapp.ui.theme.Helvetica
import com.example.ridequestcarrentalapp.ui.theme.Orange
import java.text.SimpleDateFormat
import java.util.*

// Complete Registration Data
data class CompleteRegistrationData(
    var email: String = "",
    var phone: String = "",
    var password: String = "",
    var fullName: String = "",
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
fun CompleteSignUpScreen(
    onSignUpComplete: (CompleteRegistrationData) -> Unit,
    onLoginClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var currentStep by remember { mutableIntStateOf(1) }

    // Step 1 states
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var agreeToTerms by remember { mutableStateOf(false) }

    // Step 2 states
    var fullName by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var streetAddress by remember { mutableStateOf("") }
    var barangay by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var province by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var emergencyContactName by remember { mutableStateOf("") }
    var emergencyContactNumber by remember { mutableStateOf("") }

    // Step 3 states
    var licenseNumber by remember { mutableStateOf("") }
    var licenseType by remember { mutableStateOf("Non-Professional") }
    var licenseIssueDate by remember { mutableStateOf("") }
    var licenseExpiryDate by remember { mutableStateOf("") }
    var licenseFrontUri by remember { mutableStateOf<Uri?>(null) }
    var licenseBackUri by remember { mutableStateOf<Uri?>(null) }

    val isStepValid = when (currentStep) {
        1 -> email.isNotEmpty() && email.contains("@") && phone.isNotEmpty() &&
                password.length >= 6 && password == confirmPassword && agreeToTerms
        2 -> fullName.isNotEmpty() && dateOfBirth.isNotEmpty() && gender.isNotEmpty() &&
                streetAddress.isNotEmpty() && barangay.isNotEmpty() && city.isNotEmpty() &&
                province.isNotEmpty() && postalCode.isNotEmpty() && emergencyContactName.isNotEmpty() &&
                emergencyContactNumber.isNotEmpty()
        3 -> licenseNumber.isNotEmpty() && licenseIssueDate.isNotEmpty() &&
                licenseExpiryDate.isNotEmpty() && licenseFrontUri != null && licenseBackUri != null
        4 -> true
        else -> false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        SignUpHeader(
            currentStep = currentStep,
            totalSteps = 4,
            onBackClick = {
                if (currentStep > 1) currentStep-- else onBackClick()
            }
        )

        // Content
        Column(
            modifier = Modifier.weight(1f)
        ) {
            when (currentStep) {
                1 -> Step1Content(
                    email, { email = it },
                    phone, { phone = it },
                    password, { password = it },
                    confirmPassword, { confirmPassword = it },
                    passwordVisible, { passwordVisible = it },
                    confirmPasswordVisible, { confirmPasswordVisible = it },
                    agreeToTerms, { agreeToTerms = it }
                )
                2 -> Step2Content(
                    fullName, { fullName = it },
                    dateOfBirth, { dateOfBirth = it },
                    gender, { gender = it },
                    streetAddress, { streetAddress = it },
                    barangay, { barangay = it },
                    city, { city = it },
                    province, { province = it },
                    postalCode, { postalCode = it },
                    emergencyContactName, { emergencyContactName = it },
                    emergencyContactNumber, { emergencyContactNumber = it }
                )
                3 -> Step3Content(
                    licenseNumber, { licenseNumber = it },
                    licenseType, { licenseType = it },
                    licenseIssueDate, { licenseIssueDate = it },
                    licenseExpiryDate, { licenseExpiryDate = it },
                    licenseFrontUri, { licenseFrontUri = it },
                    licenseBackUri, { licenseBackUri = it }
                )
                4 -> Step4Review(
                    email, phone, fullName, dateOfBirth, gender,
                    streetAddress, barangay, city, province, postalCode,
                    emergencyContactName, emergencyContactNumber,
                    licenseNumber, licenseType, licenseIssueDate, licenseExpiryDate,
                    licenseFrontUri, licenseBackUri
                )
            }
        }

        // Bottom Navigation
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (currentStep > 1) {
                    OutlinedButton(
                        onClick = { currentStep-- },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(2.dp, Orange)
                    ) {
                        Text("Previous", color = Orange, fontFamily = Helvetica, fontWeight = FontWeight.Bold)
                    }
                }

                Button(
                    onClick = {
                        if (currentStep < 4) {
                            currentStep++
                        } else {
                            val data = CompleteRegistrationData(
                                email = email,
                                phone = phone,
                                password = password,
                                fullName = fullName,
                                dateOfBirth = dateOfBirth,
                                gender = gender,
                                streetAddress = streetAddress,
                                barangay = barangay,
                                city = city,
                                province = province,
                                postalCode = postalCode,
                                emergencyContactName = emergencyContactName,
                                emergencyContactNumber = emergencyContactNumber,
                                licenseNumber = licenseNumber,
                                licenseType = licenseType,
                                licenseIssueDate = licenseIssueDate,
                                licenseExpiryDate = licenseExpiryDate,
                                licenseFrontUri = licenseFrontUri,
                                licenseBackUri = licenseBackUri
                            )
                            onSignUpComplete(data)
                        }
                    },
                    enabled = isStepValid,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Orange)
                ) {
                    Text(
                        if (currentStep < 4) "Next" else "Submit",
                        fontFamily = Helvetica,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun SignUpHeader(currentStep: Int, totalSteps: Int, onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Orange.copy(alpha = 0.05f))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, CircleShape)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = when (currentStep) {
                        1 -> "Create Account"
                        2 -> "Personal Information"
                        3 -> "Driver's License"
                        4 -> "Review & Submit"
                        else -> "Sign Up"
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Helvetica
                )
                Text(
                    text = "Step $currentStep of $totalSteps",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = Helvetica
                )
            }

            Box(modifier = Modifier.size(40.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(totalSteps) { step ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .background(
                            if (step < currentStep) Orange else Color.Gray.copy(alpha = 0.3f),
                            RoundedCornerShape(2.dp)
                        )
                )
            }
        }

        if (currentStep == 1) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Orange)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.white_back_logo),
                    contentDescription = "Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    }
}

@Composable
private fun Step1Content(
    email: String, onEmailChange: (String) -> Unit,
    phone: String, onPhoneChange: (String) -> Unit,
    password: String, onPasswordChange: (String) -> Unit,
    confirmPassword: String, onConfirmPasswordChange: (String) -> Unit,
    passwordVisible: Boolean, onPasswordVisibleChange: (Boolean) -> Unit,
    confirmPasswordVisible: Boolean, onConfirmPasswordVisibleChange: (Boolean) -> Unit,
    agreeToTerms: Boolean, onAgreeToTermsChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Let's create your account", fontSize = 16.sp, fontWeight = FontWeight.Medium, fontFamily = Helvetica)
        Text("Enter your email and create a password", fontSize = 14.sp, color = Color.Gray, fontFamily = Helvetica)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email Address *") },
            placeholder = { Text("example@gmail.com") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Orange, focusedLabelColor = Orange),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = onPhoneChange,
            label = { Text("Phone Number *") },
            placeholder = { Text("+63 912 345 6789") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Orange, focusedLabelColor = Orange),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password *") },
            placeholder = { Text("Minimum 6 characters") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { onPasswordVisibleChange(!passwordVisible) }) {
                    Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Orange, focusedLabelColor = Orange),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = { Text("Confirm Password *") },
            placeholder = { Text("Re-enter password") },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { onConfirmPasswordVisibleChange(!confirmPasswordVisible) }) {
                    Icon(if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            isError = confirmPassword.isNotEmpty() && confirmPassword != password,
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Orange, focusedLabelColor = Orange),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        if (confirmPassword.isNotEmpty() && confirmPassword != password) {
            Text("Passwords do not match", color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(verticalAlignment = Alignment.Top) {
            Checkbox(
                checked = agreeToTerms,
                onCheckedChange = onAgreeToTermsChange,
                colors = CheckboxDefaults.colors(checkedColor = Orange)
            )
            Text(
                text = buildAnnotatedString {
                    append("I agree to the ")
                    withStyle(SpanStyle(color = Orange, fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline)) {
                        append("Terms & Conditions")
                    }
                    append(" and ")
                    withStyle(SpanStyle(color = Orange, fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline)) {
                        append("Privacy Policy")
                    }
                },
                fontSize = 13.sp,
                fontFamily = Helvetica,
                modifier = Modifier.padding(start = 8.dp, top = 12.dp)
            )
        }
    }
}

@Composable
private fun Step2Content(
    fullName: String, onFullNameChange: (String) -> Unit,
    dateOfBirth: String, onDateOfBirthChange: (String) -> Unit,
    gender: String, onGenderChange: (String) -> Unit,
    streetAddress: String, onStreetAddressChange: (String) -> Unit,
    barangay: String, onBarangayChange: (String) -> Unit,
    city: String, onCityChange: (String) -> Unit,
    province: String, onProvinceChange: (String) -> Unit,
    postalCode: String, onPostalCodeChange: (String) -> Unit,
    emergencyContactName: String, onEmergencyContactNameChange: (String) -> Unit,
    emergencyContactNumber: String, onEmergencyContactNumberChange: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            calendar.set(year, month, day)
            onDateOfBirthChange(dateFormatter.format(calendar.time))
        },
        calendar.get(Calendar.YEAR) - 25,
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
        val maxCalendar = Calendar.getInstance()
        maxCalendar.add(Calendar.YEAR, -18)
        datePicker.maxDate = maxCalendar.timeInMillis
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Tell us about yourself", fontSize = 16.sp, fontWeight = FontWeight.Medium, fontFamily = Helvetica)
        Text("This information helps us verify your identity", fontSize = 14.sp, color = Color.Gray, fontFamily = Helvetica)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = onFullNameChange,
            label = { Text("Full Name *") },
            placeholder = { Text("Juan Dela Cruz") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Orange, focusedLabelColor = Orange),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = dateOfBirth,
            onValueChange = {},
            readOnly = true,
            label = { Text("Date of Birth *") },
            placeholder = { Text("Select your birth date") },
            trailingIcon = {
                IconButton(onClick = { datePickerDialog.show() }) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Orange)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { datePickerDialog.show() },
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Orange, focusedLabelColor = Orange),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Gender *", fontSize = 14.sp, fontWeight = FontWeight.Medium, fontFamily = Helvetica)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            listOf("Male", "Female", "Other").forEach { g ->
                FilterChip(
                    selected = gender == g,
                    onClick = { onGenderChange(g) },
                    label = { Text(g) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Orange,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Complete Address", fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = Helvetica)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = streetAddress,
            onValueChange = onStreetAddressChange,
            label = { Text("Street Address *") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Orange, focusedLabelColor = Orange),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = barangay,
                onValueChange = onBarangayChange,
                label = { Text("Barangay *") },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Orange, focusedLabelColor = Orange),
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = city,
                onValueChange = onCityChange,
                label = { Text("City *") },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Orange, focusedLabelColor = Orange),
                shape = RoundedCornerShape(12.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = province,
                onValueChange = onProvinceChange,
                label = { Text("Province *") },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Orange, focusedLabelColor = Orange),
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = postalCode,
                onValueChange = onPostalCodeChange,
                label = { Text("Postal Code *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Orange, focusedLabelColor = Orange),
                shape = RoundedCornerShape(12.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Emergency Contact", fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = Helvetica)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = emergencyContactName,
            onValueChange = onEmergencyContactNameChange,
            label = { Text("Contact Name *") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Orange, focusedLabelColor = Orange),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = emergencyContactNumber,
            onValueChange = onEmergencyContactNumberChange,
            label = { Text("Contact Number *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Orange, focusedLabelColor = Orange),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
private fun Step3Content(
    licenseNumber: String, onLicenseNumberChange: (String) -> Unit,
    licenseType: String, onLicenseTypeChange: (String) -> Unit,
    licenseIssueDate: String, onLicenseIssueDateChange: (String) -> Unit,
    licenseExpiryDate: String, onLicenseExpiryDateChange: (String) -> Unit,
    licenseFrontUri: Uri?, onLicenseFrontUriChange: (Uri?) -> Unit,
    licenseBackUri: Uri?, onLicenseBackUriChange: (Uri?) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    val issueDatePicker = DatePickerDialog(
        context,
        { _, year, month, day ->
            calendar.set(year, month, day)
            onLicenseIssueDateChange(dateFormatter.format(calendar.time))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).apply { datePicker.maxDate = System.currentTimeMillis() }

    val expiryDatePicker = DatePickerDialog(
        context,
        { _, year, month, day ->
            calendar.set(year, month, day)
            onLicenseExpiryDateChange(dateFormatter.format(calendar.time))
        },
        calendar.get(Calendar.YEAR) + 5,
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).apply { datePicker.minDate = System.currentTimeMillis() }

    val frontPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { onLicenseFrontUriChange(it) }
    }

    val backPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { onLicenseBackUriChange(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Driver's License", fontSize = 16.sp, fontWeight = FontWeight.Medium, fontFamily = Helvetica)
        Text("Required for vehicle rental verification", fontSize = 14.sp, color = Color.Gray, fontFamily = Helvetica)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = licenseNumber,
            onValueChange = { onLicenseNumberChange(it.uppercase()) },
            label = { Text("License Number *") },
            placeholder = { Text("A00-00-000000") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Orange, focusedLabelColor = Orange),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("License Type *", fontSize = 14.sp, fontWeight = FontWeight.Medium, fontFamily = Helvetica)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf("Non-Professional", "Professional").forEach { type ->
                FilterChip(
                    selected = licenseType == type,
                    onClick = { onLicenseTypeChange(type) },
                    label = { Text(type, fontSize = 13.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Orange,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = licenseIssueDate,
                onValueChange = {},
                readOnly = true,
                label = { Text("Issue Date *") },
                trailingIcon = {
                    IconButton(onClick = { issueDatePicker.show() }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Orange)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .clickable { issueDatePicker.show() },
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Orange, focusedLabelColor = Orange),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = licenseExpiryDate,
                onValueChange = {},
                readOnly = true,
                label = { Text("Expiry Date *") },
                trailingIcon = {
                    IconButton(onClick = { expiryDatePicker.show() }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Orange)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .clickable { expiryDatePicker.show() },
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Orange, focusedLabelColor = Orange),
                shape = RoundedCornerShape(12.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Upload License Photos", fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = Helvetica)
        Spacer(modifier = Modifier.height(12.dp))

        LicenseUpload("Front Side", licenseFrontUri) { frontPicker.launch("image/*") }
        Spacer(modifier = Modifier.height(12.dp))
        LicenseUpload("Back Side", licenseBackUri) { backPicker.launch("image/*") }
    }
}

@Composable
private fun LicenseUpload(label: String, uri: Uri?, onClick: () -> Unit) {
    Column {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium, fontFamily = Helvetica)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .border(2.dp, if (uri != null) Orange else Color.Gray.copy(0.3f), RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onClick)
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
                    Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tap to upload", fontSize = 12.sp, color = Color.Gray, fontFamily = Helvetica)
                }
            }
        }
    }
}

@Composable
private fun Step4Review(
    email: String, phone: String, fullName: String, dateOfBirth: String, gender: String,
    streetAddress: String, barangay: String, city: String, province: String, postalCode: String,
    emergencyContactName: String, emergencyContactNumber: String,
    licenseNumber: String, licenseType: String, licenseIssueDate: String, licenseExpiryDate: String,
    licenseFrontUri: Uri?, licenseBackUri: Uri?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Review Your Information", fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = Helvetica)
        Text("Please verify all details are correct", fontSize = 14.sp, color = Color.Gray, fontFamily = Helvetica)
        Spacer(modifier = Modifier.height(20.dp))

        ReviewSection("Account", Icons.Default.Person) {
            ReviewRow("Email", email)
            ReviewRow("Phone", phone)
        }

        Spacer(modifier = Modifier.height(12.dp))

        ReviewSection("Personal Information", Icons.Default.Badge) {
            ReviewRow("Name", fullName)
            ReviewRow("Date of Birth", dateOfBirth)
            ReviewRow("Gender", gender)
            ReviewRow("Address", "$streetAddress, $barangay")
            ReviewRow("City/Province", "$city, $province")
            ReviewRow("Postal Code", postalCode)
            ReviewRow("Emergency Contact", emergencyContactName)
            ReviewRow("Emergency Number", emergencyContactNumber)
        }

        Spacer(modifier = Modifier.height(12.dp))

        ReviewSection("Driver's License", Icons.Default.CreditCard) {
            ReviewRow("License Number", licenseNumber)
            ReviewRow("License Type", licenseType)
            ReviewRow("Issue Date", licenseIssueDate)
            ReviewRow("Expiry Date", licenseExpiryDate)

            Spacer(modifier = Modifier.height(12.dp))
            Text("License Photos", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Gray, fontFamily = Helvetica)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (licenseFrontUri != null) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Front", fontSize = 12.sp, color = Color.Gray, fontFamily = Helvetica)
                        Spacer(modifier = Modifier.height(4.dp))
                        Image(
                            painter = rememberAsyncImagePainter(licenseFrontUri),
                            contentDescription = "License Front",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                if (licenseBackUri != null) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Back", fontSize = 12.sp, color = Color.Gray, fontFamily = Helvetica)
                        Spacer(modifier = Modifier.height(4.dp))
                        Image(
                            painter = rememberAsyncImagePainter(licenseBackUri),
                            contentDescription = "License Back",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Orange.copy(alpha = 0.1f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Info, contentDescription = null, tint = Orange, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "By submitting, you agree that all information provided is accurate and complete.",
                    fontSize = 13.sp,
                    color = Color.Black.copy(alpha = 0.7f),
                    fontFamily = Helvetica
                )
            }
        }
    }
}

@Composable
private fun ReviewSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = Orange, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Helvetica
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color.Gray.copy(0.2f))
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun ReviewRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray,
            fontFamily = Helvetica
        )
        Text(
            text = value.ifEmpty { "Not provided" },
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Helvetica,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}