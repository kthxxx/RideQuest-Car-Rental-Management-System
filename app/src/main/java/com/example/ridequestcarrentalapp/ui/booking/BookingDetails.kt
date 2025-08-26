package com.example.ridequestcarrentalapp.ui.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ridequestcarrentalapp.ui.theme.Helvetica
import com.example.ridequestcarrentalapp.ui.theme.Orange
import com.example.ridequestcarrentalapp.data.Car
import com.example.ridequestcarrentalapp.data.CarRepository
import com.example.ridequestcarrentalapp.R
import java.text.SimpleDateFormat
import java.util.*
import android.app.DatePickerDialog
import android.widget.DatePicker

data class BookingDetails(
    val car: Car,
    val pickupDate: String,
    val returnDate: String,
    val pickupTime: String,
    val returnTime: String,
    val pickupLocation: String,
    val returnLocation: String,
    val totalDays: Int,
    val subtotal: Double,
    val insurance: Double,
    val tax: Double,
    val total: Double
)

@Composable
fun BookingDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray,
            fontFamily = Helvetica,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = Color.Black,
            fontFamily = Helvetica,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun PriceRow(label: String, amount: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray,
            fontFamily = Helvetica
        )
        Text(
            text = "₱${String.format("%,.0f", amount)}",
            fontSize = 14.sp,
            color = Color.Black,
            fontFamily = Helvetica
        )
    }
}

// Helper functions
fun calculateDaysBetween(startDate: String, endDate: String): Int {
    return try {
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val start = formatter.parse(startDate)
        val end = formatter.parse(endDate)

        if (start != null && end != null) {
            val diffInMillis = end.time - start.time
            val diffInDays = (diffInMillis / (1000 * 60 * 60 * 24)).toInt()
            diffInDays.coerceAtLeast(1) // Minimum 1 day
        } else {
            1 // Default to 1 day if parsing fails
        }
    } catch (e: Exception) {
        1 // Default to 1 day if any error occurs
    }
}

fun getCurrentDate(): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return formatter.format(Date())
}

fun getTomorrowDate(): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, 1)
    return formatter.format(calendar.time)
}

fun getWeekendDate(): Pair<String, String> {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val calendar = Calendar.getInstance()

    // Find next Saturday
    while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }
    val saturday = formatter.format(calendar.time)

    // Sunday
    calendar.add(Calendar.DAY_OF_YEAR, 1)
    val sunday = formatter.format(calendar.time)

    return Pair(saturday, sunday)
}

// Sample car for preview
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
fun BookingFlowScreenPreview() {
    BookingFlowScreen(car = sampleCar)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingFlowScreen(
    car: Car,
    onBackClick: () -> Unit = {},
    onConfirmBookingClick: (BookingDetails) -> Unit = {},
    onLocationClick: (String) -> Unit = {}
) {
    var currentStep by remember { mutableStateOf(1) }
    var pickupDate by remember { mutableStateOf("") }
    var returnDate by remember { mutableStateOf("") }
    var pickupTime by remember { mutableStateOf("10:00 AM") }
    var returnTime by remember { mutableStateOf("10:00 AM") }
    var pickupLocation by remember { mutableStateOf("Cebu City Center") }
    var returnLocation by remember { mutableStateOf("Cebu City Center") }
    var addInsurance by remember { mutableStateOf(false) }

    val totalDays = if (pickupDate.isNotEmpty() && returnDate.isNotEmpty()) {
        calculateDaysBetween(pickupDate, returnDate).coerceAtLeast(1)
    } else 1

    val subtotal = car.pricePerDay * totalDays
    val insurance = if (addInsurance) 500.0 * totalDays else 0.0
    val tax = (subtotal + insurance) * 0.12
    val total = subtotal + insurance + tax

    val timeSlots = listOf("08:00 AM", "09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
        "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM")

    val locations = listOf("Cebu City Center", "Mactan Airport", "SM City Cebu", "Ayala Center",
        "Capitol Site", "Colon Street", "IT Park", "Lahug Area")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 120.dp)
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
                    onClick = {
                        if (currentStep > 1) currentStep-- else onBackClick()
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Gray.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
                }

                Text(
                    text = when (currentStep) {
                        1 -> "Select Dates"
                        2 -> "Pickup & Return"
                        3 -> "Review Booking"
                        else -> "Book Car"
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = Helvetica
                )

                Text(
                    text = "$currentStep/3",
                    fontSize = 14.sp,
                    color = Orange,
                    fontFamily = Helvetica
                )
            }

            // Progress indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(3) { step ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .background(
                                if (step < currentStep) Orange else Color.Gray.copy(alpha = 0.3f),
                                RoundedCornerShape(2.dp)
                            )
                    )
                    if (step < 2) Spacer(modifier = Modifier.width(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Car Summary Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Orange.copy(alpha = 0.05f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = "Car",
                        tint = Orange,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "${car.brand} ${car.name}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontFamily = Helvetica
                        )
                        Text(
                            text = "₱${car.pricePerDay.toInt()} per day • ${car.seats} seats",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontFamily = Helvetica
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Step Content
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                when (currentStep) {
                    1 -> DateSelectionStep(
                        pickupDate = pickupDate,
                        returnDate = returnDate,
                        onPickupDateChange = { pickupDate = it },
                        onReturnDateChange = { returnDate = it }
                    )
                    2 -> LocationTimeStep(
                        pickupTime = pickupTime,
                        returnTime = returnTime,
                        pickupLocation = pickupLocation,
                        returnLocation = returnLocation,
                        timeSlots = timeSlots,
                        locations = locations,
                        onPickupTimeChange = { pickupTime = it },
                        onReturnTimeChange = { returnTime = it },
                        onPickupLocationChange = { pickupLocation = it },
                        onReturnLocationChange = { returnLocation = it },
                        onLocationClick = onLocationClick
                    )
                    3 -> BookingSummaryStep(
                        bookingDetails = BookingDetails(
                            car, pickupDate, returnDate,
                            pickupTime, returnTime,
                            pickupLocation, returnLocation,
                            totalDays, subtotal, insurance, tax, total
                        ),
                        addInsurance = addInsurance,
                        onInsuranceChange = { addInsurance = it }
                    )
                }
            }
        }

        // Bottom Button
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (currentStep == 3) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total Amount", fontSize = 12.sp, color = Color.Gray, fontFamily = Helvetica)
                            Text(
                                text = "₱${String.format("%,.0f", total)}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Orange,
                                fontFamily = Helvetica
                            )
                        }
                        Button(
                            onClick = {
                                val bookingDetails = BookingDetails(
                                    car, pickupDate, returnDate,
                                    pickupTime, returnTime,
                                    pickupLocation, returnLocation,
                                    totalDays, subtotal, insurance, tax, total
                                )
                                onConfirmBookingClick(bookingDetails)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Orange),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Icon(Icons.Default.Payment, contentDescription = "Confirm", modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Confirm Booking", fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = Helvetica)
                        }
                    }
                } else {
                    Button(
                        onClick = { currentStep++ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Orange),
                        enabled = when (currentStep) {
                            1 -> pickupDate.isNotEmpty() && returnDate.isNotEmpty()
                            2 -> pickupTime.isNotEmpty() && returnTime.isNotEmpty() &&
                                    pickupLocation.isNotEmpty() && returnLocation.isNotEmpty()
                            else -> true
                        }
                    ) {
                        Text(
                            text = when (currentStep) {
                                1 -> "Continue to Location & Time"
                                2 -> "Review Booking"
                                else -> "Next"
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Helvetica
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DateSelectionStep(
    pickupDate: String,
    returnDate: String,
    onPickupDateChange: (String) -> Unit,
    onReturnDateChange: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    // Date picker for pickup date
    val pickupDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            calendar.set(year, month, dayOfMonth)
            onPickupDateChange(dateFormatter.format(calendar.time))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
        datePicker.minDate = System.currentTimeMillis() // Prevent selecting past dates
    }

    // Date picker for return date
    val returnDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            calendar.set(year, month, dayOfMonth)
            onReturnDateChange(dateFormatter.format(calendar.time))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
        datePicker.minDate = System.currentTimeMillis() // Prevent selecting past dates
    }

    Column {
        Text(
            text = "Select Your Rental Dates",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontFamily = Helvetica
        )

        Text(
            text = "Choose when you want to pick up and return the car",
            fontSize = 14.sp,
            color = Color.Gray,
            fontFamily = Helvetica
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Pickup Date
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { pickupDatePickerDialog.show() },
            colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.05f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Pickup Date",
                    tint = Orange,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Pickup Date",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = Helvetica
                    )
                    Text(
                        text = if (pickupDate.isEmpty()) "Select pickup date" else pickupDate,
                        fontSize = 16.sp,
                        color = if (pickupDate.isEmpty()) Color.Gray else Color.Black,
                        fontFamily = Helvetica
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Select",
                    tint = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Return Date
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    // Set minimum date for return date picker based on pickup date
                    if (pickupDate.isNotEmpty()) {
                        try {
                            val pickupCalendar = Calendar.getInstance()
                            pickupCalendar.time = dateFormatter.parse(pickupDate) ?: Date()
                            returnDatePickerDialog.datePicker.minDate = pickupCalendar.timeInMillis
                        } catch (e: Exception) {
                            // If parsing fails, just use current date
                            returnDatePickerDialog.datePicker.minDate = System.currentTimeMillis()
                        }
                    }
                    returnDatePickerDialog.show()
                },
            colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.05f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Event,
                    contentDescription = "Return Date",
                    tint = Orange,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Return Date",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = Helvetica
                    )
                    Text(
                        text = if (returnDate.isEmpty()) "Select return date" else returnDate,
                        fontSize = 16.sp,
                        color = if (returnDate.isEmpty()) Color.Gray else Color.Black,
                        fontFamily = Helvetica
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Select",
                    tint = Color.Gray
                )
            }
        }

        // Quick Date Options
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Quick Options",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            fontFamily = Helvetica
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuickDateButton(
                text = "Today",
                onClick = {
                    val today = getCurrentDate()
                    onPickupDateChange(today)
                },
                modifier = Modifier.weight(1f)
            )
            QuickDateButton(
                text = "Tomorrow",
                onClick = {
                    val tomorrow = getTomorrowDate()
                    onPickupDateChange(tomorrow)
                },
                modifier = Modifier.weight(1f)
            )
            QuickDateButton(
                text = "Weekend",
                onClick = {
                    val weekend = getWeekendDate()
                    onPickupDateChange(weekend.first)
                    onReturnDateChange(weekend.second)
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun LocationTimeStep(
    pickupTime: String,
    returnTime: String,
    pickupLocation: String,
    returnLocation: String,
    timeSlots: List<String>,
    locations: List<String>,
    onPickupTimeChange: (String) -> Unit,
    onReturnTimeChange: (String) -> Unit,
    onPickupLocationChange: (String) -> Unit,
    onReturnLocationChange: (String) -> Unit,
    onLocationClick: (String) -> Unit
) {
    var showPickupLocationDropdown by remember { mutableStateOf(false) }
    var showReturnLocationDropdown by remember { mutableStateOf(false) }
    var showPickupTimeDropdown by remember { mutableStateOf(false) }
    var showReturnTimeDropdown by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "Pickup & Return Details",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontFamily = Helvetica
        )

        Text(
            text = "Set your preferred pickup and return locations and times",
            fontSize = 14.sp,
            color = Color.Gray,
            fontFamily = Helvetica
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Pickup Location
        Text(
            text = "Pickup Location",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            fontFamily = Helvetica
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showPickupLocationDropdown = true },
                colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.05f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = Orange,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = pickupLocation,
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontFamily = Helvetica,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select",
                        tint = Color.Gray
                    )
                }
            }

            DropdownMenu(
                expanded = showPickupLocationDropdown,
                onDismissRequest = { showPickupLocationDropdown = false }
            ) {
                locations.forEach { location ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = location,
                                fontFamily = Helvetica
                            )
                        },
                        onClick = {
                            onPickupLocationChange(location)
                            showPickupLocationDropdown = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pickup Time
        Text(
            text = "Pickup Time",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            fontFamily = Helvetica
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showPickupTimeDropdown = true },
                colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.05f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Time",
                        tint = Orange,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = pickupTime,
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontFamily = Helvetica,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select",
                        tint = Color.Gray
                    )
                }
            }

            DropdownMenu(
                expanded = showPickupTimeDropdown,
                onDismissRequest = { showPickupTimeDropdown = false }
            ) {
                timeSlots.forEach { time ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = time,
                                fontFamily = Helvetica
                            )
                        },
                        onClick = {
                            onPickupTimeChange(time)
                            showPickupTimeDropdown = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Return Location
        Text(
            text = "Return Location",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            fontFamily = Helvetica
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showReturnLocationDropdown = true },
                colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.05f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = Orange,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = returnLocation,
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontFamily = Helvetica,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select",
                        tint = Color.Gray
                    )
                }
            }

            DropdownMenu(
                expanded = showReturnLocationDropdown,
                onDismissRequest = { showReturnLocationDropdown = false }
            ) {
                locations.forEach { location ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = location,
                                fontFamily = Helvetica
                            )
                        },
                        onClick = {
                            onReturnLocationChange(location)
                            showReturnLocationDropdown = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Return Time
        Text(
            text = "Return Time",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            fontFamily = Helvetica
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showReturnTimeDropdown = true },
                colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.05f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Time",
                        tint = Orange,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = returnTime,
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontFamily = Helvetica,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select",
                        tint = Color.Gray
                    )
                }
            }

            DropdownMenu(
                expanded = showReturnTimeDropdown,
                onDismissRequest = { showReturnTimeDropdown = false }
            ) {
                timeSlots.forEach { time ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = time,
                                fontFamily = Helvetica
                            )
                        },
                        onClick = {
                            onReturnTimeChange(time)
                            showReturnTimeDropdown = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BookingSummaryStep(
    bookingDetails: BookingDetails,
    addInsurance: Boolean,
    onInsuranceChange: (Boolean) -> Unit
) {
    Column {
        Text(
            text = "Booking Summary",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontFamily = Helvetica
        )

        Text(
            text = "Review your booking details before confirming",
            fontSize = 14.sp,
            color = Color.Gray,
            fontFamily = Helvetica
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Rental Details Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.05f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Rental Details",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = Helvetica
                )

                Spacer(modifier = Modifier.height(12.dp))

                BookingDetailRow("Car", "${bookingDetails.car.brand} ${bookingDetails.car.name}")
                BookingDetailRow("Pickup", "${bookingDetails.pickupDate} at ${bookingDetails.pickupTime}")
                BookingDetailRow("Return", "${bookingDetails.returnDate} at ${bookingDetails.returnTime}")
                BookingDetailRow("Pickup Location", bookingDetails.pickupLocation)
                BookingDetailRow("Return Location", bookingDetails.returnLocation)
                BookingDetailRow("Duration", "${bookingDetails.totalDays} day${if (bookingDetails.totalDays > 1) "s" else ""}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Insurance Option
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.05f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onInsuranceChange(!addInsurance) }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = addInsurance,
                    onCheckedChange = onInsuranceChange,
                    colors = CheckboxDefaults.colors(checkedColor = Orange)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Add Insurance Coverage",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        fontFamily = Helvetica
                    )
                    Text(
                        text = "₱500/day - Comprehensive coverage for peace of mind",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = Helvetica
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Price Breakdown Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Orange.copy(alpha = 0.05f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Price Breakdown",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = Helvetica
                )

                Spacer(modifier = Modifier.height(12.dp))

                PriceRow(
                    label = "Car Rental (${bookingDetails.totalDays} day${if (bookingDetails.totalDays > 1) "s" else ""})",
                    amount = bookingDetails.subtotal
                )

                if (addInsurance) {
                    PriceRow(
                        label = "Insurance Coverage",
                        amount = bookingDetails.insurance
                    )
                }

                PriceRow(
                    label = "Tax & Fees",
                    amount = bookingDetails.tax
                )

                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Color.Gray.copy(alpha = 0.3f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Amount",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = Helvetica
                    )
                    Text(
                        text = "₱${String.format("%,.0f", bookingDetails.total)}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Orange,
                        fontFamily = Helvetica
                    )
                }
            }
        }
    }
}

@Composable
fun QuickDateButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Orange),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = androidx.compose.ui.graphics.SolidColor(Orange)
        )
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontFamily = Helvetica
        )
    }
}

@Composable
fun LocationDropdown(
    locations: List<String>,
    selected: String?,
    onLocationSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selected ?: "",
        onValueChange = {},
        label = { Text("Choose Location") },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
    )
}

@Composable
fun LazyRowTimeSelector(
    selectedTime: String,
    timeSlots: List<String>,
    onTimeSelected: (String) -> Unit
) {
    androidx.compose.foundation.lazy.LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(timeSlots) { time ->
            FilterChip(
                selected = selectedTime == time,
                onClick = { onTimeSelected(time) },
                label = {
                    Text(
                        text = time,
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
}

