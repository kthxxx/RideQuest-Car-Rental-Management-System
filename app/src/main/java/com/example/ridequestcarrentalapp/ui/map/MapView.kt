package com.example.ridequestcarrentalapp.ui.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import com.example.ridequestcarrentalapp.R
import com.example.ridequestcarrentalapp.data.*
import com.example.ridequestcarrentalapp.ui.theme.Helvetica
import com.example.ridequestcarrentalapp.ui.theme.Orange
import kotlinx.coroutines.launch
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

// Data class for car locations
data class CarLocation(
    val car: Car,
    val latitude: Double,
    val longitude: Double,
    val address: String
)

// ViewModel for map state management
class MapViewModel(
    private val repository: CarRepository = CarRepositoryFactory.create()
) : androidx.lifecycle.ViewModel() {
    private val _uiState = mutableStateOf(MapUiState())
    val uiState: State<MapUiState> = _uiState

    private val _carLocations = mutableStateOf<List<CarLocation>>(emptyList())
    val carLocations: State<List<CarLocation>> = _carLocations

    private val _filteredLocations = mutableStateOf<List<CarLocation>>(emptyList())
    val filteredLocations: State<List<CarLocation>> = _filteredLocations

    init {
        loadCarLocations()
    }

    private fun loadCarLocations() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val cars = repository.getAllCars()
                val locations = cars.map { car ->
                    // Generate mock locations around Cebu City
                    val baseLatitude = 10.3157
                    val baseLongitude = 123.8854
                    val latOffset = (Math.random() - 0.5) * 0.1
                    val lngOffset = (Math.random() - 0.5) * 0.1

                    CarLocation(
                        car = car,
                        latitude = baseLatitude + latOffset,
                        longitude = baseLongitude + lngOffset,
                        address = generateMockAddress()
                    )
                }
                _carLocations.value = locations
                applyFilters()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load car locations: ${e.message}"
                )
            }
        }
    }

    private fun generateMockAddress(): String {
        val streets = listOf(
            "IT Park", "Lahug", "Capitol Site", "Fuente Circle",
            "Ayala Center", "SM City Cebu", "Colon Street", "Carbon Market",
            "JY Square Mall", "Banilad Town Center"
        )
        return "${streets.random()}, Cebu City"
    }

    fun updateSearchCriteria(criteria: CarSearchCriteria) {
        _uiState.value = _uiState.value.copy(searchCriteria = criteria)
        applyFilters()
    }

    private fun applyFilters() {
        val criteria = _uiState.value.searchCriteria
        var filtered = _carLocations.value

        // Apply category filter
        criteria.category?.let { category ->
            filtered = filtered.filter { it.car.category == category }
        }

        // Apply price range filter
        filtered = filtered.filter { location ->
            location.car.pricePerDay >= criteria.minPrice &&
                    location.car.pricePerDay <= criteria.maxPrice
        }

        // Apply availability filter
        if (criteria.availableOnly) {
            filtered = filtered.filter { it.car.isAvailable }
        }

        // Apply search query
        if (criteria.query.isNotEmpty()) {
            filtered = filtered.filter { location ->
                location.car.name.contains(criteria.query, ignoreCase = true) ||
                        location.car.brand.contains(criteria.query, ignoreCase = true) ||
                        location.address.contains(criteria.query, ignoreCase = true)
            }
        }

        _filteredLocations.value = filtered
        _uiState.value = _uiState.value.copy(isLoading = false)
    }

    fun selectLocation(location: CarLocation) {
        _uiState.value = _uiState.value.copy(selectedLocation = location)
    }

    fun clearSelection() {
        _uiState.value = _uiState.value.copy(selectedLocation = null)
    }

    fun toggleMapType() {
        val currentType = _uiState.value.mapType
        val newType = if (currentType == MapType.NORMAL) MapType.SATELLITE else MapType.NORMAL
        _uiState.value = _uiState.value.copy(mapType = newType)
    }

    fun toggleShowAvailableOnly() {
        val criteria = _uiState.value.searchCriteria
        updateSearchCriteria(criteria.copy(availableOnly = !criteria.availableOnly))
    }
}

data class MapUiState(
    val searchCriteria: CarSearchCriteria = CarSearchCriteria(),
    val selectedLocation: CarLocation? = null,
    val mapType: MapType = MapType.NORMAL,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showFilters: Boolean = false
)

enum class MapType {
    NORMAL, SATELLITE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapViewScreen(
    onBackClick: () -> Unit = {},
    onCarClick: (Car) -> Unit = {},
    viewModel: MapViewModel = viewModel()
) {
    val uiState by viewModel.uiState
    val carLocations by viewModel.carLocations
    val filteredLocations by viewModel.filteredLocations
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Car Locations",
                    fontFamily = Helvetica,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Orange
                    )
                }
            },
            actions = {
                IconButton(onClick = { viewModel.toggleMapType() }) {
                    Icon(
                        imageVector = if (uiState.mapType == MapType.NORMAL)
                            Icons.Default.Satellite else Icons.Default.Map,
                        contentDescription = "Toggle Map Type",
                        tint = Orange
                    )
                }
                IconButton(onClick = { viewModel.toggleShowAvailableOnly() }) {
                    Icon(
                        imageVector = if (uiState.searchCriteria.availableOnly)
                            Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle Available Only",
                        tint = if (uiState.searchCriteria.availableOnly) Orange else Color.Gray
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.Black
            )
        )

        // Search Bar
        SearchBarSection(
            searchCriteria = uiState.searchCriteria,
            onSearchChange = { query ->
                viewModel.updateSearchCriteria(
                    uiState.searchCriteria.copy(query = query)
                )
            }
        )

        // Error message
        uiState.errorMessage?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f))
            ) {
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.padding(12.dp),
                    fontSize = 14.sp
                )
            }
        }

        // Map Container (Placeholder since Google Maps requires API setup)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(
                    Color.Gray.copy(alpha = 0.1f),
                    RoundedCornerShape(16.dp)
                )
        ) {
            // Mock Map View
            MockMapView(
                locations = filteredLocations,
                selectedLocation = uiState.selectedLocation,
                onLocationClick = { location ->
                    viewModel.selectLocation(location)
                },
                mapType = uiState.mapType
            )

            // Loading overlay
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(alpha = 0.8f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Orange)
                }
            }

            // Map controls
            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FloatingActionButton(
                    onClick = { /* Zoom in */ },
                    modifier = Modifier.size(40.dp),
                    containerColor = Color.White,
                    contentColor = Orange
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Zoom In",
                        modifier = Modifier.size(20.dp)
                    )
                }
                FloatingActionButton(
                    onClick = { /* Zoom out */ },
                    modifier = Modifier.size(40.dp),
                    containerColor = Color.White,
                    contentColor = Orange
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Zoom Out",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Current location button
            FloatingActionButton(
                onClick = { /* Center on user location */ },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .size(48.dp),
                containerColor = Orange,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "My Location"
                )
            }

            // Results counter
            Card(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Orange),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "${filteredLocations.size} cars nearby",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Bottom car list
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(filteredLocations.take(10)) { location ->
                MapCarCard(
                    carLocation = location,
                    isSelected = uiState.selectedLocation?.car?.id == location.car.id,
                    onClick = {
                        viewModel.selectLocation(location)
                    },
                    onBookClick = {
                        onCarClick(location.car)
                    }
                )
            }
        }
    }

    // Car detail dialog
    uiState.selectedLocation?.let { location ->
        CarLocationDialog(
            carLocation = location,
            onDismiss = { viewModel.clearSelection() },
            onBookNow = { onCarClick(location.car) }
        )
    }
}

@Composable
private fun SearchBarSection(
    searchCriteria: CarSearchCriteria,
    onSearchChange: (String) -> Unit
) {
    OutlinedTextField(
        value = searchCriteria.query,
        onValueChange = onSearchChange,
        placeholder = {
            Text(
                "Search by car, brand, or location...",
                color = Color.Gray,
                fontSize = 14.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Gray
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Orange,
            unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
            unfocusedContainerColor = Color.Gray.copy(alpha = 0.05f),
            focusedContainerColor = Color.Gray.copy(alpha = 0.05f)
        ),
        singleLine = true
    )
}

@Composable
private fun MockMapView(
    locations: List<CarLocation>,
    selectedLocation: CarLocation?,
    onLocationClick: (CarLocation) -> Unit,
    mapType: MapType
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (mapType == MapType.SATELLITE)
                    Color(0xFF2D5016) else Color(0xFFF5F5DC),
                RoundedCornerShape(16.dp)
            )
    ) {
        // Mock map background
        Text(
            text = if (mapType == MapType.SATELLITE) "🛰️ Satellite View" else "🗺️ Map View",
            modifier = Modifier.align(Alignment.Center),
            color = if (mapType == MapType.SATELLITE) Color.White else Color.Gray,
            fontSize = 16.sp
        )

        // Mock location markers
        locations.forEachIndexed { index, location ->
            val offsetX = (50 + (index * 30) % 200).dp
            val offsetY = (80 + (index * 25) % 150).dp

            Box(
                modifier = Modifier
                    .absoluteOffset(x = offsetX, y = offsetY)
                    .size(if (selectedLocation?.car?.id == location.car.id) 40.dp else 30.dp)
                    .background(
                        if (location.car.isAvailable) Orange else Color.Gray,
                        CircleShape
                    )
                    .clickable { onLocationClick(location) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = "Car location",
                    tint = Color.White,
                    modifier = Modifier.size(
                        if (selectedLocation?.car?.id == location.car.id) 24.dp else 18.dp
                    )
                )
            }
        }
    }
}

@Composable
private fun MapCarCard(
    carLocation: CarLocation,
    isSelected: Boolean,
    onClick: () -> Unit,
    onBookClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Orange.copy(alpha = 0.1f) else Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 6.dp else 3.dp
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, Orange) else null
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Car image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(
                        Color.Gray.copy(alpha = 0.1f),
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = carLocation.car.imageRes),
                    contentDescription = carLocation.car.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Car details
            Text(
                text = "${carLocation.car.brand} ${carLocation.car.name}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = carLocation.address,
                fontSize = 10.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "₱${carLocation.car.pricePerDay.toInt()}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Orange
                )

                if (carLocation.car.isAvailable) {
                    Button(
                        onClick = onBookClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Orange,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .height(24.dp)
                            .width(60.dp),
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        Text(
                            text = "Book",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    Text(
                        text = "N/A",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun CarLocationDialog(
    carLocation: CarLocation,
    onDismiss: () -> Unit,
    onBookNow: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${carLocation.car.brand} ${carLocation.car.name}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.Gray
                        )
                    }
                }

                // Car image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(
                            Color.Gray.copy(alpha = 0.1f),
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = carLocation.car.imageRes),
                        contentDescription = carLocation.car.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(100.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Details
                DetailRow(label = "Location", value = carLocation.address)
                DetailRow(label = "Category", value = carLocation.car.category.displayName)
                DetailRow(label = "Price", value = "₱${carLocation.car.pricePerDay.toInt()}/day")
                DetailRow(label = "Seats", value = "${carLocation.car.seats} passengers")
                DetailRow(
                    label = "Status",
                    value = if (carLocation.car.isAvailable) "Available" else "Not Available",
                    valueColor = if (carLocation.car.isAvailable) Color(0xFF4CAF50) else Color.Red
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Orange
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Orange)
                    ) {
                        Text("Close")
                    }

                    Button(
                        onClick = onBookNow,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Orange,
                            contentColor = Color.White
                        ),
                        enabled = carLocation.car.isAvailable
                    ) {
                        Text("Book Now")
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    valueColor: Color = Color.Black
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray,
            fontFamily = Helvetica
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = valueColor,
            fontFamily = Helvetica
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MapViewPreview() {
    MapViewScreen()
}