package com.example.ridequestcarrentalapp.data

import com.example.ridequestcarrentalapp.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

// Enhanced Car data class with more comprehensive features
data class Car(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val brand: String,
    val model: String,
    val year: Int,
    val pricePerDay: Double, // in PHP
    val pricePerHour: Double = pricePerDay / 24.0, // hourly rate
    val rating: Float,
    val reviewCount: Int = 0,
    val imageRes: Int,
    val imageUrls: List<String> = emptyList(), // Additional images
    val fuelType: FuelType,
    val transmission: Transmission,
    val seats: Int,
    val isAvailable: Boolean = true,
    val category: CarCategory,
    val description: String = "",
    val features: List<String> = emptyList(),
    val location: String = "Metro Manila",
    val mileage: Double = 0.0, // km/L
    val engineSize: String = "",
    val color: String = "",
    val licensePlate: String = "",
    val insurance: InsuranceType = InsuranceType.BASIC,
    val deposit: Double = pricePerDay * 2, // Security deposit
    val minimumAge: Int = 21,
    val availableFrom: String = "", // Date availability starts
    val availableTo: String = "", // Date availability ends
    val pickupLocations: List<String> = listOf("Metro Manila"),
    val tags: List<String> = emptyList() // For filtering (e.g., "Family-friendly", "Fuel-efficient")
)

// Enums for better type safety and consistency
enum class FuelType(val displayName: String) {
    GASOLINE("Gasoline"),
    DIESEL("Diesel"),
    HYBRID("Hybrid"),
    ELECTRIC("Electric"),
    LPG("LPG")
}

enum class Transmission(val displayName: String) {
    MANUAL("Manual"),
    AUTOMATIC("Automatic"),
    CVT("CVT"),
    DCT("DCT")
}

enum class CarCategory(val displayName: String, val description: String) {
    ECONOMY("Economy", "Fuel-efficient and budget-friendly"),
    SUV("SUV", "Spacious and powerful for all terrains"),
    VAN_MPV("Van/MPV", "Perfect for groups and families"),
    LUXURY("Luxury", "Premium comfort and style"),
    ELECTRIC_HYBRID("Electric/Hybrid", "Eco-friendly and innovative"),
    PICKUP("Pickup", "Heavy-duty and cargo-friendly"),
    SPORTS("Sports", "High-performance and sporty")
}

enum class InsuranceType(val displayName: String) {
    BASIC("Basic Coverage"),
    COMPREHENSIVE("Comprehensive Coverage"),
    PREMIUM("Premium Coverage")
}

// Search and filter criteria
data class CarSearchCriteria(
    val query: String = "",
    val category: CarCategory? = null,
    val brand: String = "",
    val minPrice: Double = 0.0,
    val maxPrice: Double = Double.MAX_VALUE,
    val minSeats: Int = 1,
    val maxSeats: Int = 20,
    val fuelType: FuelType? = null,
    val transmission: Transmission? = null,
    val location: String = "",
    val availableFrom: String = "",
    val availableTo: String = "",
    val minRating: Float = 0.0f,
    val features: List<String> = emptyList(),
    val sortBy: SortOption = SortOption.PRICE_LOW_TO_HIGH
)

enum class SortOption {
    PRICE_LOW_TO_HIGH,
    PRICE_HIGH_TO_LOW,
    RATING_HIGH_TO_LOW,
    NEWEST_FIRST,
    MOST_POPULAR,
    ALPHABETICAL
}

// Repository interface for better testability
interface CarRepository {
    suspend fun getAllCars(): List<Car>
    suspend fun getCarById(carId: String): Car?
    suspend fun searchCars(criteria: CarSearchCriteria): List<Car>
    suspend fun getCarsByCategory(category: CarCategory): List<Car>
    suspend fun getCarsByBrand(brand: String): List<Car>
    suspend fun getFeaturedCars(): List<Car>
    suspend fun getPopularCars(): List<Car>
    suspend fun updateCarAvailability(carId: String, isAvailable: Boolean): Boolean
    fun observeCarAvailability(): Flow<List<Car>>
    suspend fun addCar(car: Car): Boolean
    suspend fun updateCar(car: Car): Boolean
    suspend fun deleteCar(carId: String): Boolean
}

class CarRepositoryImpl : CarRepository {

    private val _carsStateFlow = MutableStateFlow(getPhilippineCars())
    private val carsFlow: Flow<List<Car>> = _carsStateFlow.asStateFlow()

    override suspend fun getAllCars(): List<Car> {
        return _carsStateFlow.value.sortedBy { it.name }
    }

    override suspend fun getCarById(carId: String): Car? {
        return _carsStateFlow.value.find { it.id == carId }
    }

    override suspend fun searchCars(criteria: CarSearchCriteria): List<Car> {
        var cars = _carsStateFlow.value

        // Apply filters
        if (criteria.query.isNotEmpty()) {
            cars = cars.filter { car ->
                car.name.contains(criteria.query, ignoreCase = true) ||
                        car.brand.contains(criteria.query, ignoreCase = true) ||
                        car.model.contains(criteria.query, ignoreCase = true) ||
                        car.description.contains(criteria.query, ignoreCase = true) ||
                        car.tags.any { it.contains(criteria.query, ignoreCase = true) }
            }
        }

        criteria.category?.let { category ->
            cars = cars.filter { it.category == category }
        }

        if (criteria.brand.isNotEmpty() && criteria.brand != "All Brands") {
            cars = cars.filter { it.brand.equals(criteria.brand, ignoreCase = true) }
        }

        cars = cars.filter {
            it.pricePerDay >= criteria.minPrice && it.pricePerDay <= criteria.maxPrice
        }

        cars = cars.filter {
            it.seats >= criteria.minSeats && it.seats <= criteria.maxSeats
        }

        criteria.fuelType?.let { fuelType ->
            cars = cars.filter { it.fuelType == fuelType }
        }

        criteria.transmission?.let { transmission ->
            cars = cars.filter { it.transmission == transmission }
        }

        if (criteria.location.isNotEmpty()) {
            cars = cars.filter {
                it.pickupLocations.any { location ->
                    location.contains(criteria.location, ignoreCase = true)
                }
            }
        }

        cars = cars.filter { it.rating >= criteria.minRating }

        if (criteria.features.isNotEmpty()) {
            cars = cars.filter { car ->
                criteria.features.all { requiredFeature ->
                    car.features.any { it.contains(requiredFeature, ignoreCase = true) }
                }
            }
        }

        // Apply sorting
        return when (criteria.sortBy) {
            SortOption.PRICE_LOW_TO_HIGH -> cars.sortedBy { it.pricePerDay }
            SortOption.PRICE_HIGH_TO_LOW -> cars.sortedByDescending { it.pricePerDay }
            SortOption.RATING_HIGH_TO_LOW -> cars.sortedByDescending { it.rating }
            SortOption.NEWEST_FIRST -> cars.sortedByDescending { it.year }
            SortOption.MOST_POPULAR -> cars.sortedByDescending { it.reviewCount }
            SortOption.ALPHABETICAL -> cars.sortedBy { it.name }
        }
    }

    override suspend fun getCarsByCategory(category: CarCategory): List<Car> {
        return _carsStateFlow.value.filter { it.category == category }
    }

    override suspend fun getCarsByBrand(brand: String): List<Car> {
        return if (brand == "All Brands") {
            _carsStateFlow.value
        } else {
            _carsStateFlow.value.filter { it.brand.equals(brand, ignoreCase = true) }
        }
    }

    override suspend fun getFeaturedCars(): List<Car> {
        return _carsStateFlow.value
            .filter { it.rating >= 4.5f && it.isAvailable }
            .take(6)
    }

    override suspend fun getPopularCars(): List<Car> {
        return _carsStateFlow.value
            .sortedByDescending { it.reviewCount }
            .take(10)
    }

    override suspend fun updateCarAvailability(carId: String, isAvailable: Boolean): Boolean {
        val updatedCars = _carsStateFlow.value.map { car ->
            if (car.id == carId) {
                car.copy(isAvailable = isAvailable)
            } else car
        }
        _carsStateFlow.value = updatedCars
        return true
    }

    override fun observeCarAvailability(): Flow<List<Car>> {
        return carsFlow
    }

    override suspend fun addCar(car: Car): Boolean {
        val currentCars = _carsStateFlow.value.toMutableList()
        currentCars.add(car)
        _carsStateFlow.value = currentCars
        return true
    }

    override suspend fun updateCar(car: Car): Boolean {
        val updatedCars = _carsStateFlow.value.map { existingCar ->
            if (existingCar.id == car.id) car else existingCar
        }
        _carsStateFlow.value = updatedCars
        return true
    }

    override suspend fun deleteCar(carId: String): Boolean {
        val updatedCars = _carsStateFlow.value.filter { it.id != carId }
        _carsStateFlow.value = updatedCars
        return true
    }

    // Helper function to get all brands
    suspend fun getAllBrands(): List<String> {
        return _carsStateFlow.value.map { it.brand }.distinct().sorted()
    }

    // Helper function to get price range
    suspend fun getPriceRange(): Pair<Double, Double> {
        val prices = _carsStateFlow.value.map { it.pricePerDay }
        return Pair(prices.minOrNull() ?: 0.0, prices.maxOrNull() ?: 0.0)
    }

    private fun getPhilippineCars(): List<Car> = listOf(
        // Economy & Compact Cars with enhanced data
        Car(
            id = "1",
            name = "Wigo",
            brand = "Toyota",
            model = "Wigo",
            year = 2023,
            pricePerDay = 1500.0,
            rating = 4.2f,
            reviewCount = 127,
            imageRes = R.drawable.wigo,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.MANUAL,
            seats = 5,
            category = CarCategory.ECONOMY,
            description = "Perfect city car with excellent fuel economy and easy parking. Ideal for daily commuting and short trips.",
            features = listOf("Air Conditioning", "Power Steering", "Central Locking", "USB Charging", "Bluetooth"),
            mileage = 18.5,
            engineSize = "1.0L",
            color = "White",
            tags = listOf("Fuel-efficient", "Compact", "City-friendly"),
            pickupLocations = listOf("Metro Manila", "Cebu City", "Davao")
        ),
        Car(
            id = "2",
            name = "Vios",
            brand = "Toyota",
            model = "Vios",
            year = 2022,
            pricePerDay = 1800.0,
            rating = 4.4f,
            reviewCount = 203,
            imageRes = R.drawable.vios,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.AUTOMATIC,
            seats = 5,
            category = CarCategory.ECONOMY,
            description = "Reliable sedan with spacious interior and smooth automatic transmission. Great for business and leisure trips.",
            features = listOf("Automatic Transmission", "Air Conditioning", "ABS", "Airbags", "Touch Screen Display"),
            mileage = 16.2,
            engineSize = "1.3L",
            color = "Silver",
            tags = listOf("Reliable", "Comfortable", "Automatic"),
            pickupLocations = listOf("Metro Manila", "Cebu City")
        ),
        Car(
            id = "3",
            name = "Brio",
            brand = "Honda",
            model = "Brio",
            year = 2023,
            pricePerDay = 1600.0,
            rating = 4.1f,
            reviewCount = 95,
            imageRes = R.drawable.honda_brio,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.MANUAL,
            seats = 5,
            category = CarCategory.ECONOMY,
            description = "Sporty hatchback with Honda's reliable engineering. Nimble handling perfect for city driving and weekend getaways.",
            features = listOf("Sport Mode", "Air Conditioning", "Central Locking", "USB Port", "Multi-function Steering Wheel"),
            mileage = 17.8,
            engineSize = "1.2L",
            color = "Red",
            tags = listOf("Sporty", "Reliable", "Honda-quality", "Fuel-efficient"),
            pickupLocations = listOf("Metro Manila", "Cebu City", "Iloilo")
        ),
        Car(
            id = "4",
            name = "City",
            brand = "Honda",
            model = "City",
            year = 2022,
            pricePerDay = 2000.0,
            rating = 4.3f,
            reviewCount = 156,
            imageRes = R.drawable.honda_city,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.CVT,
            seats = 5,
            category = CarCategory.ECONOMY,
            description = "Premium compact sedan with spacious cabin and smooth CVT transmission. Excellent balance of comfort and efficiency.",
            features = listOf("CVT Transmission", "Touch Screen", "Cruise Control", "Keyless Entry", "Auto Climate Control"),
            mileage = 15.9,
            engineSize = "1.5L",
            color = "White Pearl",
            tags = listOf("Premium-economy", "Spacious", "CVT", "Comfortable"),
            pickupLocations = listOf("Metro Manila", "Cebu City", "Davao")
        ),
        Car(
            id = "5",
            name = "Almera",
            brand = "Nissan",
            model = "Almera",
            year = 2023,
            pricePerDay = 1700.0,
            rating = 4.0f,
            reviewCount = 112,
            imageRes = R.drawable.almera,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.CVT,
            seats = 5,
            category = CarCategory.ECONOMY,
            description = "Modern sedan with intelligent mobility features and efficient CVT. Great value with contemporary styling.",
            features = listOf("Intelligent Key", "Push Start", "Auto AC", "Rear Camera", "Bluetooth Connectivity"),
            mileage = 16.7,
            engineSize = "1.0L Turbo",
            color = "Gun Metallic",
            tags = listOf("Modern", "Turbo", "Intelligent-features", "Value-for-money"),
            pickupLocations = listOf("Metro Manila", "Cebu City")
        ),
        Car(
            id = "6",
            name = "Accent",
            brand = "Hyundai",
            model = "Accent",
            year = 2022,
            pricePerDay = 1650.0,
            rating = 4.2f,
            reviewCount = 89,
            imageRes = R.drawable.hyundai_accent,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.MANUAL,
            seats = 5,
            category = CarCategory.ECONOMY,
            description = "Korean reliability meets modern design. Affordable sedan with comprehensive safety features and stylish exterior.",
            features = listOf("6 Airbags", "ABS", "Electronic Stability Control", "Hill Start Assist", "USB Connectivity"),
            mileage = 17.2,
            engineSize = "1.4L",
            color = "Fiery Red",
            tags = listOf("Safe", "Korean-quality", "Stylish", "Affordable"),
            pickupLocations = listOf("Metro Manila", "Cebu City", "Cagayan de Oro")
        ),
        Car(
            id = "7",
            name = "Soluto",
            brand = "Kia",
            model = "Soluto",
            year = 2023,
            pricePerDay = 1550.0,
            rating = 4.0f,
            reviewCount = 73,
            imageRes = R.drawable.kia_soluto,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.AUTOMATIC,
            seats = 5,
            category = CarCategory.ECONOMY,
            description = "Entry-level sedan with premium features at an affordable price. Kia's 5-year warranty provides peace of mind.",
            features = listOf("Auto Transmission", "Touchscreen", "Rear Parking Sensors", "Power Windows", "Central Locking"),
            mileage = 16.8,
            engineSize = "1.4L",
            color = "Clear White",
            tags = listOf("Warranty", "Automatic", "Entry-premium", "Reliable"),
            pickupLocations = listOf("Metro Manila", "Cebu City")
        ),
        Car(
            id = "8",
            name = "Swift",
            brand = "Suzuki",
            model = "Swift",
            year = 2022,
            pricePerDay = 1750.0,
            rating = 4.3f,
            reviewCount = 134,
            imageRes = R.drawable.swift,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.MANUAL,
            seats = 5,
            category = CarCategory.ECONOMY,
            description = "Zippy hatchback with European-inspired design. Fun to drive with responsive handling and efficient engine.",
            features = listOf("Sport Suspension", "Alloy Wheels", "Fog Lamps", "Keyless Entry", "Audio System"),
            mileage = 18.1,
            engineSize = "1.2L",
            color = "Premium Silver",
            tags = listOf("Fun-to-drive", "European-design", "Zippy", "Efficient"),
            pickupLocations = listOf("Metro Manila", "Cebu City", "Baguio")
        ),
        Car(
            id = "9",
            name = "Mirage G4",
            brand = "Mitsubishi",
            model = "Mirage G4",
            year = 2023,
            pricePerDay = 1600.0,
            rating = 4.1f,
            reviewCount = 87,
            imageRes = R.drawable.mitsibishi_g,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.CVT,
            seats = 5,
            category = CarCategory.ECONOMY,
            description = "Ultra-fuel-efficient sedan with spacious interior despite compact size. Perfect for budget-conscious travelers.",
            features = listOf("Ultra INVECS-III CVT", "Eco Drive Assist", "Smartphone Link", "Power Steering", "Digital AC"),
            mileage = 20.2,
            engineSize = "1.2L",
            color = "Red Diamond",
            tags = listOf("Ultra-fuel-efficient", "Spacious-compact", "Budget-friendly", "Eco-friendly"),
            pickupLocations = listOf("Metro Manila", "Cebu City", "Davao")
        ),
        Car(
            id = "10",
            name = "Celerio",
            brand = "Suzuki",
            model = "Celerio",
            year = 2022,
            pricePerDay = 1450.0,
            rating = 4.0f,
            reviewCount = 65,
            imageRes = R.drawable.celerio,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.MANUAL,
            seats = 5,
            isAvailable = false,
            category = CarCategory.ECONOMY,
            description = "Compact city car with surprising interior space. Easy to maneuver in tight city streets and very economical.",
            features = listOf("Compact Design", "Power Steering", "Central Locking", "AM/FM Radio", "12V Socket"),
            mileage = 19.5,
            engineSize = "1.0L",
            color = "Arctic White",
            tags = listOf("City-car", "Economical", "Easy-parking", "Compact"),
            pickupLocations = listOf("Metro Manila", "Cebu City")
        ),

        // SUVs & Crossovers
        Car(
            id = "11",
            name = "Fortuner",
            brand = "Toyota",
            model = "Fortuner",
            year = 2023,
            pricePerDay = 4500.0,
            rating = 4.6f,
            reviewCount = 89,
            imageRes = R.drawable.fortuner,
            fuelType = FuelType.DIESEL,
            transmission = Transmission.AUTOMATIC,
            seats = 7,
            category = CarCategory.SUV,
            description = "Premium SUV perfect for family adventures and rough terrains. Powerful diesel engine with 4WD capability.",
            features = listOf("4WD", "Leather Seats", "Sunroof", "Navigation", "Reverse Camera", "Hill Start Assist"),
            mileage = 12.8,
            engineSize = "2.4L Turbo Diesel",
            color = "Attitude Black",
            tags = listOf("Premium", "4WD", "Family-friendly", "Off-road"),
            pickupLocations = listOf("Metro Manila", "Cebu City", "Baguio"),
            minimumAge = 23,
            deposit = 10000.0
        ),
        Car(
            id = "12",
            name = "Montero Sport",
            brand = "Mitsubishi",
            model = "Montero Sport",
            year = 2022,
            pricePerDay = 4200.0,
            rating = 4.5f,
            reviewCount = 76,
            imageRes = R.drawable.mitsibishi_montero,
            fuelType = FuelType.DIESEL,
            transmission = Transmission.AUTOMATIC,
            seats = 7,
            category = CarCategory.SUV,
            description = "Robust SUV with proven off-road capabilities and premium interior comfort. Perfect for family road trips.",
            features = listOf("Super Select 4WD", "7 SRS Airbags", "Multi-Around Monitor", "Rockford Fosgate Audio", "Paddle Shifters"),
            mileage = 11.9,
            engineSize = "2.4L MIVEC Diesel",
            color = "Deep Bronze",
            tags = listOf("Off-road-capable", "Premium-interior", "Super-select-4wd", "Family-suv"),
            pickupLocations = listOf("Metro Manila", "Cebu City", "Davao"),
            minimumAge = 23,
            deposit = 9500.0
        ),
        Car(
            id = "13",
            name = "Everest",
            brand = "Ford",
            model = "Everest",
            year = 2023,
            pricePerDay = 4300.0,
            rating = 4.4f,
            reviewCount = 68,
            imageRes = R.drawable.ford_everest,
            fuelType = FuelType.DIESEL,
            transmission = Transmission.AUTOMATIC,
            seats = 7,
            category = CarCategory.SUV,
            description = "American engineering meets Asian practicality. Strong towing capacity and advanced safety features.",
            features = listOf("Terrain Management System", "Ford SYNC 3", "Adaptive Cruise Control", "Blind Spot Monitoring", "360° Camera"),
            mileage = 12.5,
            engineSize = "2.0L Bi-Turbo Diesel",
            color = "Meteor Grey",
            tags = listOf("American-engineering", "Towing-capacity", "Advanced-safety", "Terrain-management"),
            pickupLocations = listOf("Metro Manila", "Cebu City"),
            minimumAge = 23,
            deposit = 9800.0
        ),
        Car(
            id = "14",
            name = "CR-V",
            brand = "Honda",
            model = "CR-V",
            year = 2022,
            pricePerDay = 3500.0,
            rating = 4.5f,
            reviewCount = 142,
            imageRes = R.drawable.honda_crv,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.CVT,
            seats = 5,
            category = CarCategory.SUV,
            description = "World's best-selling SUV with Honda's legendary reliability. Perfect blend of comfort, efficiency, and practicality.",
            features = listOf("Honda SENSING", "Hands-Free Power Tailgate", "Walk Away Auto Lock", "Remote Engine Start", "Panoramic Sunroof"),
            mileage = 13.7,
            engineSize = "1.5L Turbo",
            color = "Platinum White Pearl",
            tags = listOf("World-bestseller", "Honda-reliability", "Turbo-engine", "Honda-sensing"),
            pickupLocations = listOf("Metro Manila", "Cebu City", "Davao"),
            minimumAge = 22
        ),
        Car(
            id = "15",
            name = "RAV4",
            brand = "Toyota",
            model = "RAV4",
            year = 2023,
            pricePerDay = 3800.0,
            rating = 4.6f,
            reviewCount = 98,
            imageRes = R.drawable.rav4,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.CVT,
            seats = 5,
            category = CarCategory.SUV,
            description = "Adventure-ready crossover with rugged styling and Toyota Safety Sense 2.0. Built for both city and wilderness.",
            features = listOf("Toyota Safety Sense 2.0", "Dynamic Torque Vectoring AWD", "Multi-Terrain Select", "JBL Premium Audio", "Wireless Charging"),
            mileage = 14.2,
            engineSize = "2.0L Dynamic Force",
            color = "Magnetic Gray Metallic",
            tags = listOf("Adventure-ready", "Safety-sense", "AWD", "Rugged-styling"),
            pickupLocations = listOf("Metro Manila", "Cebu City", "Baguio"),
            minimumAge = 22
        ),
        Car(
            id = "16",
            name = "Tucson",
            brand = "Hyundai",
            model = "Tucson",
            year = 2022,
            pricePerDay = 3200.0,
            rating = 4.3f,
            reviewCount = 84,
            imageRes = R.drawable.hyundai_tucson,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.AUTOMATIC,
            seats = 5,
            category = CarCategory.SUV,
            description = "Bold and innovative design with advanced smart features. Hyundai's SmartSense safety suite comes standard.",
            features = listOf("SmartSense Safety Suite", "Digital Cockpit", "Wireless Phone Charger", "HTRAC AWD", "Bose Premium Audio"),
            mileage = 13.5,
            engineSize = "2.0L Nu MPI",
            color = "Phantom Black",
            tags = listOf("Bold-design", "Smart-features", "SmartSense", "Digital-cockpit"),
            pickupLocations = listOf("Metro Manila", "Cebu City"),
            minimumAge = 22
        ),
        Car(
            id = "17",
            name = "X-Trail",
            brand = "Nissan",
            model = "X-Trail",
            year = 2023,
            pricePerDay = 3400.0,
            rating = 4.4f,
            reviewCount = 91,
            imageRes = R.drawable.x_trail,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.CVT,
            seats = 5,
            category = CarCategory.SUV,
            description = "Comfortable crossover with Nissan Intelligent Mobility. Smooth CVT and spacious interior for long journeys.",
            features = listOf("Nissan Intelligent Mobility", "ProPILOT Assist", "Intelligent Around View Monitor", "Xtronic CVT", "Zero Gravity Seats"),
            mileage = 14.0,
            engineSize = "2.5L",
            color = "Gun Metallic",
            tags = listOf("Intelligent-mobility", "ProPILOT", "Zero-gravity-seats", "Comfortable"),
            pickupLocations = listOf("Metro Manila", "Cebu City", "Davao")
        ),
        Car(
            id = "18",
            name = "Sportage",
            brand = "Kia",
            model = "Sportage",
            year = 2022,
            pricePerDay = 3100.0,
            rating = 4.2f,
            reviewCount = 72,
            imageRes = R.drawable.kia_sportage,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.AUTOMATIC,
            seats = 5,
            category = CarCategory.SUV,
            description = "Stylish compact SUV with sporty character. Advanced safety features and impressive warranty coverage.",
            features = listOf("Drive Mode Select", "Smart Cruise Control", "Blind Spot Collision Warning", "UVO Connect", "Harman Kardon Audio"),
            mileage = 13.8,
            engineSize = "2.0L MPI",
            color = "Clear White",
            tags = listOf("Sporty-character", "Advanced-safety", "UVO-connect", "Warranty"),
            pickupLocations = listOf("Metro Manila", "Cebu City")
        ),
        Car(
            id = "19",
            name = "Sorento",
            brand = "Kia",
            model = "Sorento",
            year = 2023,
            pricePerDay = 3600.0,
            rating = 4.5f,
            reviewCount = 63,
            imageRes = R.drawable.kia_sorento,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.AUTOMATIC,
            seats = 7,
            category = CarCategory.SUV,
            description = "Mid-size SUV with three rows of seating. Premium features and Kia's industry-leading warranty.",
            features = listOf("3-Row Seating", "UVO Connect", "Surround View Monitor", "Highway Driving Assist", "Premium Interior"),
            mileage = 12.3,
            engineSize = "2.2L CRDi",
            color = "Gravity Gray",
            tags = listOf("3-row-seating", "Premium-features", "Highway-assist", "Mid-size"),
            pickupLocations = listOf("Metro Manila", "Cebu City"),
            minimumAge = 23
        ),

        // Vans & MPVs
        Car(
            id = "20",
            name = "Hiace Commuter",
            brand = "Toyota",
            model = "Hiace",
            year = 2023,
            pricePerDay = 3500.0,
            rating = 4.3f,
            reviewCount = 156,
            imageRes = R.drawable.hiace,
            fuelType = FuelType.DIESEL,
            transmission = Transmission.MANUAL,
            seats = 15,
            category = CarCategory.VAN_MPV,
            description = "Reliable people mover perfect for group travels and events. Spacious interior with comfortable seating for 15 passengers.",
            features = listOf("15 Seater", "Air Conditioning", "Power Steering", "Anti-lock Braking System", "Driver Airbag"),
            mileage = 10.5,
            engineSize = "2.8L Diesel",
            color = "Super White",
            tags = listOf("Group-travel", "15-seater", "Events", "Reliable", "People-mover"),
            pickupLocations = listOf("Metro Manila", "Cebu City", "Davao"),
            minimumAge = 23,
            deposit = 8000.0
        ),
        Car(
            id = "21",
            name = "Urvan",
            brand = "Nissan",
            model = "Urvan",
            year = 2022,
            pricePerDay = 3200.0,
            rating = 4.1f,
            reviewCount = 94,
            imageRes = R.drawable.mv350,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.MANUAL,
            seats = 15,
            category = CarCategory.VAN_MPV,
            description = "Affordable group transportation with proven durability. Ideal for tours, events, and family gatherings.",
            features = listOf("15 Passenger Seating", "Dual Air Conditioning", "Power Steering", "Wide Sliding Doors", "Durable Build"),
            mileage = 9.8,
            engineSize = "4.0L",
            color = "Brilliant Silver",
            tags = listOf("Affordable", "Durable", "Tours", "Family-gatherings", "Wide-doors"),
            pickupLocations = listOf("Metro Manila", "Cebu City"),
            minimumAge = 23,
            deposit = 7500.0
        ),
        Car(
            id = "22",
            name = "Xpander",
            brand = "Mitsubishi",
            model = "Xpander",
            year = 2023,
            pricePerDay = 2500.0,
            rating = 4.4f,
            reviewCount = 187,
            imageRes = R.drawable.mitsibishi_xpander,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.CVT,
            seats = 7,
            category = CarCategory.VAN_MPV,
            description = "Modern 7-seater MPV with SUV-like styling. Perfect family car with flexible seating arrangements.",
            features = listOf("7 Seater", "Flexible Seating", "Touchscreen Display", "Keyless Operation", "ECO Mode", "Rear AC Vents"),
            mileage = 15.4,
            engineSize = "1.5L MIVEC",
            color = "Red Diamond",
            tags = listOf("Modern-mpv", "Flexible-seating", "SUV-styling", "Family-car", "7-seater"),
            pickupLocations = listOf("Metro Manila", "Cebu City", "Davao", "Iloilo")
        ),
        Car(
            id = "23",
            name = "Ertiga",
            brand = "Suzuki",
            model = "Ertiga",
            year = 2022,
            pricePerDay = 2200.0,
            rating = 4.2f,
            reviewCount = 143,
            imageRes = R.drawable.ertiga,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.MANUAL,
            seats = 7,
            category = CarCategory.VAN_MPV,
            description = "Compact 7-seater with excellent fuel economy. Ideal for families looking for space without compromising efficiency.",
            features = listOf("Captain Chairs", "Smart Play Studio", "Auto Air Conditioner", "ESP", "Hill Hold Control"),
            mileage = 16.8,
            engineSize = "1.5L",
            color = "Pearl Arctic White",
            tags = listOf("Compact-7-seater", "Fuel-economy", "Captain-chairs", "Smart-play"),
            pickupLocations = listOf("Metro Manila", "Cebu City", "Davao")
        ),
        Car(
            id = "24",
            name = "Avanza",
            brand = "Toyota",
            model = "Avanza",
            year = 2023,
            pricePerDay = 2300.0,
            rating = 4.1f,
            reviewCount = 198,
            imageRes = R.drawable.avanza,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.MANUAL,
            seats = 7,
            category = CarCategory.VAN_MPV,
            description = "Popular 7-seater MPV with Toyota reliability. Affordable family transportation with proven durability.",
            features = listOf("7 Seater", "Dual SRS Airbags", "ABS", "Power Steering", "Central Locking", "Audio System"),
            mileage = 15.9,
            engineSize = "1.5L",
            color = "Silver Metallic",
            tags = listOf("Popular-mpv", "Toyota-reliability", "Affordable-family", "Proven-durability"),
            pickupLocations = listOf("Metro Manila", "Cebu City", "Davao", "Iloilo", "Cagayan de Oro")
        ),
        Car(
            id = "25",
            name = "Starex",
            brand = "Hyundai",
            model = "Starex",
            year = 2022,
            pricePerDay = 3000.0,
            rating = 4.0f,
            reviewCount = 82,
            imageRes = R.drawable.hyundai_starex,
            fuelType = FuelType.DIESEL,
            transmission = Transmission.MANUAL,
            seats = 11,
            category = CarCategory.VAN_MPV,
            description = "Spacious 11-seater van perfect for large groups. Diesel engine provides good fuel economy for long trips.",
            features = listOf("11 Seater", "Diesel Engine", "Dual Air Conditioning", "Power Steering", "ABS", "Driver Airbag"),
            mileage = 11.2,
            engineSize = "2.5L CRDi",
            color = "Creamy White",
            tags = listOf("11-seater", "Large-groups", "Diesel-economy", "Long-trips"),
            pickupLocations = listOf("Metro Manila", "Cebu City"),
            minimumAge = 23,
            deposit = 7000.0
        ),
        Car(
            id = "26",
            name = "Alphard",
            brand = "Toyota",
            model = "Alphard",
            year = 2023,
            pricePerDay = 8000.0,
            rating = 4.8f,
            reviewCount = 34,
            imageRes = R.drawable.alphard,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.CVT,
            seats = 7,
            category = CarCategory.LUXURY,
            description = "Ultimate luxury van with premium amenities. Executive lounge on wheels with unmatched comfort and prestige.",
            features = listOf("Executive Lounge", "Premium Leather", "Dual Sunroofs", "JBL Audio", "Rear Entertainment", "Power Seats"),
            mileage = 9.8,
            engineSize = "3.5L V6",
            color = "White Pearl Crystal Shine",
            tags = listOf("Ultimate-luxury", "Executive-lounge", "Premium-amenities", "Prestige"),
            pickupLocations = listOf("Metro Manila"),
            minimumAge = 25,
            deposit = 20000.0,
            insurance = InsuranceType.COMPREHENSIVE
        ),
        Car(
            id = "27",
            name = "Transit",
            brand = "Ford",
            model = "Transit",
            year = 2022,
            pricePerDay = 3800.0,
            rating = 4.2f,
            reviewCount = 67,
            imageRes = R.drawable.ford_transit,
            fuelType = FuelType.DIESEL,
            transmission = Transmission.MANUAL,
            seats = 12,
            category = CarCategory.VAN_MPV,
            description = "Commercial-grade van with excellent payload capacity. Perfect for business operations and group transportation.",
            features = listOf("12 Seater", "Commercial Grade", "High Payload", "Durable Build", "Power Steering", "Air Conditioning"),
            mileage = 10.3,
            engineSize = "2.2L TDCi",
            color = "Frozen White",
            tags = listOf("Commercial-grade", "High-payload", "Business-operations", "Durable"),
            pickupLocations = listOf("Metro Manila", "Cebu City"),
            minimumAge = 23,
            deposit = 8500.0
        ),

        // Premium & Luxury Cars
        Car(
            id = "29",
            name = "5 Series",
            brand = "BMW",
            model = "5 Series",
            year = 2022,
            pricePerDay = 8500.0,
            rating = 4.8f,
            reviewCount = 45,
            imageRes = R.drawable.bmw,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.AUTOMATIC,
            seats = 5,
            category = CarCategory.LUXURY,
            description = "Ultimate luxury sedan with cutting-edge technology and premium comfort. Perfect for executive travel and special occasions.",
            features = listOf("Premium Leather", "Massage Seats", "Premium Sound", "Advanced Safety", "Wireless Charging", "Heads-up Display"),
            mileage = 10.5,
            engineSize = "2.0L Twin Turbo",
            color = "Imperial Blue Brilliant Effect",
            tags = listOf("Ultimate-luxury", "Executive-travel", "Premium-comfort", "Cutting-edge-tech"),
            pickupLocations = listOf("Metro Manila"),
            minimumAge = 25,
            deposit = 20000.0,
            insurance = InsuranceType.COMPREHENSIVE
        ),
        Car(
            id = "30",
            name = "C-Class",
            brand = "Mercedes",
            model = "C-Class",
            year = 2023,
            pricePerDay = 7000.0,
            rating = 4.7f,
            reviewCount = 38,
            imageRes = R.drawable.mercedes_c,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.AUTOMATIC,
            seats = 5,
            category = CarCategory.LUXURY,
            description = "Entry-level luxury sedan from Mercedes-Benz. Sophisticated design with advanced driver assistance systems.",
            features = listOf("MBUX Infotainment", "64-Color Ambient Lighting", "Active Brake Assist", "Blind Spot Assist", "Premium Interior"),
            mileage = 11.8,
            engineSize = "1.5L Turbo",
            color = "Obsidian Black Metallic",
            tags = listOf("Entry-luxury", "Mercedes-quality", "MBUX", "Sophisticated-design"),
            pickupLocations = listOf("Metro Manila"),
            minimumAge = 25,
            deposit = 16000.0,
            insurance = InsuranceType.COMPREHENSIVE
        ),
        Car(
            id = "31",
            name = "E-Class",
            brand = "Mercedes",
            model = "E-Class",
            year = 2022,
            pricePerDay = 9500.0,
            rating = 4.9f,
            reviewCount = 29,
            imageRes = R.drawable.mercedes_e,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.AUTOMATIC,
            seats = 5,
            category = CarCategory.LUXURY,
            description = "Executive luxury sedan with intelligent drive systems. The benchmark for premium automotive excellence.",
            features = listOf("Intelligent Drive", "Air Body Control", "Burmester Surround Sound", "Energizing Comfort", "PRE-SAFE System"),
            mileage = 10.2,
            engineSize = "2.0L Turbo",
            color = "Selenite Grey Metallic",
            tags = listOf("Executive-luxury", "Intelligent-drive", "Premium-excellence", "Burmester-audio"),
            pickupLocations = listOf("Metro Manila"),
            minimumAge = 25,
            deposit = 22000.0,
            insurance = InsuranceType.PREMIUM
        ),
        Car(
            id = "32",
            name = "Mustang",
            brand = "Ford",
            model = "Mustang",
            year = 2023,
            pricePerDay = 12000.0,
            rating = 4.6f,
            reviewCount = 52,
            imageRes = R.drawable.ford_mustang,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.AUTOMATIC,
            seats = 4,
            category = CarCategory.LUXURY,
            description = "Iconic American muscle car with legendary performance. Pure driving excitement with modern technology.",
            features = listOf("V8 Engine", "Track Apps", "Launch Control", "Brembo Brakes", "Recaro Seats", "Active Exhaust"),
            mileage = 7.5,
            engineSize = "5.0L V8",
            color = "Race Red",
            tags = listOf("Iconic-muscle", "V8-power", "American-legend", "Track-performance"),
            pickupLocations = listOf("Metro Manila"),
            minimumAge = 25,
            deposit = 30000.0,
            insurance = InsuranceType.PREMIUM
        ),
        Car(
            id = "33",
            name = "ES",
            brand = "Lexus",
            model = "ES",
            year = 2022,
            pricePerDay = 8800.0,
            rating = 4.8f,
            reviewCount = 31,
            imageRes = R.drawable.lexus,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.CVT,
            seats = 5,
            category = CarCategory.LUXURY,
            description = "Japanese luxury with meticulous craftsmanship. Whisper-quiet cabin and exceptional build quality.",
            features = listOf("Lexus Safety System+", "Mark Levinson Audio", "Semi-Aniline Leather", "Climate Concierge", "Takumi Craftsmanship"),
            mileage = 12.5,
            engineSize = "2.5L",
            color = "Eminent White Pearl",
            tags = listOf("Japanese-luxury", "Whisper-quiet", "Takumi-craftsmanship", "Mark-levinson"),
            pickupLocations = listOf("Metro Manila"),
            minimumAge = 25,
            deposit = 20000.0,
            insurance = InsuranceType.COMPREHENSIVE
        ),
        Car(
            id = "34",
            name = "NX",
            brand = "Lexus",
            model = "NX",
            year = 2023,
            pricePerDay = 9200.0,
            rating = 4.7f,
            reviewCount = 27,
            imageRes = R.drawable.white_back_logo,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.CVT,
            seats = 5,
            category = CarCategory.LUXURY,
            description = "Luxury compact SUV with bold design and advanced technology. Perfect blend of comfort and capability.",
            features = listOf("Lexus Interface", "Premium Navigation", "Wireless Charging", "Panoramic View Monitor", "F Sport Suspension"),
            mileage = 11.9,
            engineSize = "2.4L Turbo",
            color = "Ultrasonic Blue Mica",
            tags = listOf("Luxury-compact-suv", "Bold-design", "Advanced-technology", "F-sport"),
            pickupLocations = listOf("Metro Manila"),
            minimumAge = 25,
            deposit = 21000.0,
            insurance = InsuranceType.COMPREHENSIVE
        ),
        Car(
            id = "35",
            name = "A4",
            brand = "Audi",
            model = "A4",
            year = 2022,
            pricePerDay = 7500.0,
            rating = 4.6f,
            reviewCount = 33,
            imageRes = R.drawable.audi_a,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.AUTOMATIC,
            seats = 5,
            isAvailable = false,
            category = CarCategory.LUXURY,
            description = "German engineering excellence with quattro all-wheel drive. Sophisticated luxury with sporty character.",
            features = listOf("quattro AWD", "Virtual Cockpit", "Bang & Olufsen Sound", "MMI Navigation", "Adaptive Cruise Control"),
            mileage = 11.3,
            engineSize = "2.0L TFSI",
            color = "Glacier White Metallic",
            tags = listOf("German-engineering", "quattro-awd", "Virtual-cockpit", "Bang-olufsen"),
            pickupLocations = listOf("Metro Manila"),
            minimumAge = 25,
            deposit = 18000.0,
            insurance = InsuranceType.COMPREHENSIVE
        ),
        Car(
            id = "36",
            name = "Q5",
            brand = "Audi",
            model = "Q5",
            year = 2023,
            pricePerDay = 10000.0,
            rating = 4.8f,
            reviewCount = 24,
            imageRes = R.drawable.audi_b,
            fuelType = FuelType.GASOLINE,
            transmission = Transmission.AUTOMATIC,
            seats = 5,
            category = CarCategory.LUXURY,
            description = "Premium luxury SUV with quattro ultra technology. Exceptional handling and luxurious appointments throughout.",
            features = listOf("quattro ultra AWD", "12.3 Virtual Cockpit", "Audi Pre Sense", "Matrix LED Headlights", "Bang & Olufsen 3D Sound"),
            mileage = 10.8,
            engineSize = "2.0L TFSI",
            color = "Manhattan Gray Metallic",
            tags = listOf("Premium-luxury-suv", "quattro-ultra", "Matrix-led", "3D-sound"),
            pickupLocations = listOf("Metro Manila"),
            minimumAge = 25,
            deposit = 23000.0,
            insurance = InsuranceType.PREMIUM
        ),

        // Electric & Hybrid
        Car(
            id = "37",
            name = "Prius",
            brand = "Toyota",
            model = "Prius",
            year = 2023,
            pricePerDay = 3500.0,
            rating = 4.5f,
            reviewCount = 118,
            imageRes = R.drawable.prius,
            fuelType = FuelType.HYBRID,
            transmission = Transmission.CVT,
            seats = 5,
            category = CarCategory.ELECTRIC_HYBRID,
            description = "Pioneer of hybrid technology with exceptional fuel efficiency. Environmentally conscious choice without compromising comfort.",
            features = listOf("Toyota Hybrid System", "Solar Roof", "Head-Up Display", "Wireless Charging", "Toyota Safety Sense 2.0"),
            mileage = 23.8, // km/L equivalent
            engineSize = "1.8L Hybrid",
            color = "Super White",
            tags = listOf("Hybrid-pioneer", "Exceptional-efficiency", "Solar-roof", "Eco-conscious"),
            pickupLocations = listOf("Metro Manila", "Cebu City")
        ),
        Car(
            id = "38",
            name = "Leaf",
            brand = "Nissan",
            model = "Leaf",
            year = 2022,
            pricePerDay = 3200.0,
            rating = 4.3f,
            reviewCount = 89,
            imageRes = R.drawable.leaf,
            fuelType = FuelType.ELECTRIC,
            transmission = Transmission.AUTOMATIC,
            seats = 5,
            category = CarCategory.ELECTRIC_HYBRID,
            description = "100% electric vehicle with zero emissions. Quiet operation and instant torque delivery for smooth city driving.",
            features = listOf("100% Electric", "e-Pedal", "ProPILOT Assist", "NissanConnect EV", "Zero Emissions", "Instant Torque"),
            mileage = 0.0, // Electric - measured in kWh/100km
            engineSize = "Electric Motor",
            color = "Gun Metallic",
            tags = listOf("100-electric", "Zero-emissions", "e-pedal", "ProPILOT"),
            pickupLocations = listOf("Metro Manila", "Cebu City")
        ),
        Car(
            id = "39",
            name = "Outlander PHEV",
            brand = "Mitsubishi",
            model = "Outlander PHEV",
            year = 2023,
            pricePerDay = 4500.0,
            rating = 4.6f,
            reviewCount = 67,
            imageRes = R.drawable.mitsibishi_outlander,
            fuelType = FuelType.HYBRID,
            transmission = Transmission.CVT,
            seats = 5,
            category = CarCategory.ELECTRIC_HYBRID,
            description = "Plug-in hybrid SUV with all-wheel control. Best of both worlds - electric efficiency and petrol flexibility.",
            features = listOf("Plug-in Hybrid", "Super All Wheel Control", "Multi Around Monitor", "Smartphone Link", "Eco Mode"),
            mileage = 18.5,
            engineSize = "2.4L + Electric Motors",
            color = "White Diamond",
            tags = listOf("Plug-in-hybrid", "Super-AWC", "Electric-flexibility", "Best-both-worlds"),
            pickupLocations = listOf("Metro Manila", "Cebu City")
        ),
        Car(
            id = "40",
            name = "Ioniq",
            brand = "Hyundai",
            model = "Ioniq",
            year = 2022,
            pricePerDay = 3400.0,
            rating = 4.4f,
            reviewCount = 76,
            imageRes = R.drawable.hyundai_ioniq,
            fuelType = FuelType.HYBRID,
            transmission = Transmission.AUTOMATIC,
            seats = 5,
            category = CarCategory.ELECTRIC_HYBRID,
            description = "Advanced hybrid with aerodynamic design. Industry-leading efficiency with modern connectivity features.",
            features = listOf("Advanced Hybrid System", "Aerodynamic Design", "8-inch Display", "Blue Link Connected Services", "SmartSense"),
            mileage = 22.4,
            engineSize = "1.6L GDI + Electric Motor",
            color = "Electric Shadow",
            tags = listOf("Advanced-hybrid", "Aerodynamic", "Blue-link", "Industry-leading-efficiency"),
            pickupLocations = listOf("Metro Manila", "Cebu City")
        ),
        Car(
            id = "41",
            name = "Model 3",
            brand = "Tesla",
            model = "Model 3",
            year = 2023,
            pricePerDay = 8500.0,
            rating = 4.9f,
            reviewCount = 72,
            imageRes = R.drawable.tesla3,
            fuelType = FuelType.ELECTRIC,
            transmission = Transmission.AUTOMATIC,
            seats = 5,
            category = CarCategory.ELECTRIC_HYBRID,
            description = "Revolutionary electric sedan with autopilot features and zero emissions. Experience the future of driving.",
            features = listOf("Autopilot", "Supercharging Network", "Premium Interior", "Over-the-Air Updates", "Mobile App Control", "Glass Roof"),
            mileage = 0.0, // Electric - no fuel consumption
            engineSize = "Dual Motor All-Wheel Drive",
            color = "Pearl White Multi-Coat",
            tags = listOf("Revolutionary-electric", "Autopilot", "Future-driving", "Over-air-updates"),
            pickupLocations = listOf("Metro Manila"),
            minimumAge = 25,
            deposit = 25000.0,
            insurance = InsuranceType.PREMIUM
        ),
        Car(
            id = "42",
            name = "Dolphin",
            brand = "BYD",
            model = "Dolphin",
            year = 2023,
            pricePerDay = 3800.0,
            rating = 4.4f,
            reviewCount = 54,
            imageRes = R.drawable.byddol,
            fuelType = FuelType.ELECTRIC,
            transmission = Transmission.AUTOMATIC,
            seats = 5,
            category = CarCategory.ELECTRIC_HYBRID,
            description = "Affordable electric vehicle with impressive range. Chinese innovation meets practical electric mobility.",
            features = listOf("Blade Battery Technology", "Rotating Touchscreen", "NFC Card Key", "OTA Updates", "DC Fast Charging"),
            mileage = 0.0, // Electric vehicle
            engineSize = "Electric Motor",
            color = "Coral Pink",
            tags = listOf("Affordable-electric", "Blade-battery", "Chinese-innovation", "Fast-charging"),
            pickupLocations = listOf("Metro Manila", "Cebu City")
        ),
        Car(
            id = "43",
            name = "ZS EV",
            brand = "MG",
            model = "ZS EV",
            year = 2022,
            pricePerDay = 3600.0,
            rating = 4.2f,
            reviewCount = 48,
            imageRes = R.drawable.mg,
            fuelType = FuelType.ELECTRIC,
            transmission = Transmission.AUTOMATIC,
            seats = 5,
            category = CarCategory.ELECTRIC_HYBRID,
            description = "Electric SUV with British heritage and modern technology. Spacious interior with zero-emission driving.",
            features = listOf("Electric SUV", "10.1-inch Touchscreen", "i-SMART Technology", "Fast Charging", "Regenerative Braking"),
            mileage = 0.0, // Electric vehicle
            engineSize = "Electric Motor",
            color = "Arctic White",
            tags = listOf("Electric-suv", "British-heritage", "i-smart-tech", "Zero-emission"),
            pickupLocations = listOf("Metro Manila", "Cebu City")
        )
    )
}

// Companion object for easy access to static data
object CarRepositoryFactory {
    fun create(): CarRepository = CarRepositoryImpl()
}