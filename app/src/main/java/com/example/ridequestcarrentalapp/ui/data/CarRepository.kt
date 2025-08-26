package com.example.ridequestcarrentalapp.data

import com.example.ridequestcarrentalapp.R

data class Car(
    val id: String,
    val name: String,
    val brand: String,
    val model: String,
    val year: Int,
    val pricePerDay: Double, // in PHP
    val rating: Float,
    val imageRes: Int,
    val fuelType: String,
    val transmission: String,
    val seats: Int,
    val isAvailable: Boolean = true,
    val category: String,
    val description: String = ""
)

object CarRepository {
    // Philippine car rental data with realistic pricing in PHP
    val philippineCars = listOf(
        // Economy & Compact Cars
        Car("1", "Wigo", "Toyota", "Wigo", 2023, 1500.0, 4.2f, R.drawable.white_back_logo, "Gasoline", "Manual", 5, true, "Economy"),
        Car("2", "Vios", "Toyota", "Vios", 2022, 1800.0, 4.4f, R.drawable.white_back_logo, "Gasoline", "Automatic", 5, true, "Economy"),
        Car("3", "Brio", "Honda", "Brio", 2023, 1600.0, 4.1f, R.drawable.white_back_logo, "Gasoline", "Manual", 5, true, "Economy"),
        Car("4", "City", "Honda", "City", 2022, 2000.0, 4.3f, R.drawable.white_back_logo, "Gasoline", "CVT", 5, true, "Economy"),
        Car("5", "Almera", "Nissan", "Almera", 2023, 1700.0, 4.0f, R.drawable.white_back_logo, "Gasoline", "CVT", 5, true, "Economy"),
        Car("6", "Accent", "Hyundai", "Accent", 2022, 1650.0, 4.2f, R.drawable.white_back_logo, "Gasoline", "Manual", 5, true, "Economy"),
        Car("7", "Soluto", "Kia", "Soluto", 2023, 1550.0, 4.0f, R.drawable.white_back_logo, "Gasoline", "Automatic", 5, true, "Economy"),
        Car("8", "Swift", "Suzuki", "Swift", 2022, 1750.0, 4.3f, R.drawable.white_back_logo, "Gasoline", "Manual", 5, true, "Economy"),
        Car("9", "Mirage G4", "Mitsubishi", "Mirage G4", 2023, 1600.0, 4.1f, R.drawable.white_back_logo, "Gasoline", "CVT", 5, true, "Economy"),
        Car("10", "Celerio", "Suzuki", "Celerio", 2022, 1450.0, 4.0f, R.drawable.white_back_logo, "Gasoline", "Manual", 5, false, "Economy"),

        // SUVs & Crossovers
        Car("11", "Fortuner", "Toyota", "Fortuner", 2023, 4500.0, 4.6f, R.drawable.white_back_logo, "Diesel", "Automatic", 7, true, "SUV"),
        Car("12", "Montero Sport", "Mitsubishi", "Montero Sport", 2022, 4200.0, 4.5f, R.drawable.white_back_logo, "Diesel", "Automatic", 7, true, "SUV"),
        Car("13", "Everest", "Ford", "Everest", 2023, 4300.0, 4.4f, R.drawable.white_back_logo, "Diesel", "Automatic", 7, true, "SUV"),
        Car("14", "CR-V", "Honda", "CR-V", 2022, 3500.0, 4.5f, R.drawable.white_back_logo, "Gasoline", "CVT", 5, true, "SUV"),
        Car("15", "RAV4", "Toyota", "RAV4", 2023, 3800.0, 4.6f, R.drawable.white_back_logo, "Gasoline", "CVT", 5, true, "SUV"),
        Car("16", "Tucson", "Hyundai", "Tucson", 2022, 3200.0, 4.3f, R.drawable.white_back_logo, "Gasoline", "Automatic", 5, true, "SUV"),
        Car("17", "X-Trail", "Nissan", "X-Trail", 2023, 3400.0, 4.4f, R.drawable.white_back_logo, "Gasoline", "CVT", 5, true, "SUV"),
        Car("18", "Sportage", "Kia", "Sportage", 2022, 3100.0, 4.2f, R.drawable.white_back_logo, "Gasoline", "Automatic", 5, true, "SUV"),
        Car("19", "Sorento", "Kia", "Sorento", 2023, 3600.0, 4.5f, R.drawable.white_back_logo, "Gasoline", "Automatic", 7, true, "SUV"),

        // Vans & MPVs
        Car("20", "Hiace Commuter", "Toyota", "Hiace", 2023, 3500.0, 4.3f, R.drawable.white_back_logo, "Diesel", "Manual", 15, true, "Van/MPV"),
        Car("21", "Urvan", "Nissan", "Urvan", 2022, 3200.0, 4.1f, R.drawable.white_back_logo, "Gasoline", "Manual", 15, true, "Van/MPV"),
        Car("22", "Xpander", "Mitsubishi", "Xpander", 2023, 2500.0, 4.4f, R.drawable.white_back_logo, "Gasoline", "CVT", 7, true, "Van/MPV"),
        Car("23", "Ertiga", "Suzuki", "Ertiga", 2022, 2200.0, 4.2f, R.drawable.white_back_logo, "Gasoline", "Manual", 7, true, "Van/MPV"),
        Car("24", "Avanza", "Toyota", "Avanza", 2023, 2300.0, 4.1f, R.drawable.white_back_logo, "Gasoline", "Manual", 7, true, "Van/MPV"),
        Car("25", "Starex", "Hyundai", "Starex", 2022, 3000.0, 4.0f, R.drawable.white_back_logo, "Diesel", "Manual", 11, true, "Van/MPV"),
        Car("26", "Alphard", "Toyota", "Alphard", 2023, 8000.0, 4.8f, R.drawable.white_back_logo, "Gasoline", "CVT", 7, true, "Luxury"),
        Car("27", "Transit", "Ford", "Transit", 2022, 3800.0, 4.2f, R.drawable.white_back_logo, "Diesel", "Manual", 12, true, "Van/MPV"),

        // Premium & Luxury Cars
        Car("28", "3 Series", "BMW", "3 Series", 2023, 6500.0, 4.7f, R.drawable.white_back_logo, "Gasoline", "Automatic", 5, true, "Luxury"),
        Car("29", "5 Series", "BMW", "5 Series", 2022, 8500.0, 4.8f, R.drawable.white_back_logo, "Gasoline", "Automatic", 5, true, "Luxury"),
        Car("30", "C-Class", "Mercedes", "C-Class", 2023, 7000.0, 4.7f, R.drawable.white_back_logo, "Gasoline", "Automatic", 5, true, "Luxury"),
        Car("31", "E-Class", "Mercedes", "E-Class", 2022, 9500.0, 4.9f, R.drawable.white_back_logo, "Gasoline", "Automatic", 5, true, "Luxury"),
        Car("32", "Mustang", "Ford", "Mustang", 2023, 12000.0, 4.6f, R.drawable.white_back_logo, "Gasoline", "Automatic", 4, true, "Luxury"),
        Car("33", "ES", "Lexus", "ES", 2022, 8800.0, 4.8f, R.drawable.white_back_logo, "Gasoline", "CVT", 5, true, "Luxury"),
        Car("34", "NX", "Lexus", "NX", 2023, 9200.0, 4.7f, R.drawable.white_back_logo, "Gasoline", "CVT", 5, true, "Luxury"),
        Car("35", "A4", "Audi", "A4", 2022, 7500.0, 4.6f, R.drawable.white_back_logo, "Gasoline", "Automatic", 5, false, "Luxury"),
        Car("36", "Q5", "Audi", "Q5", 2023, 10000.0, 4.8f, R.drawable.white_back_logo, "Gasoline", "Automatic", 5, true, "Luxury"),

        // Electric & Hybrid
        Car("37", "Prius", "Toyota", "Prius", 2023, 3500.0, 4.5f, R.drawable.white_back_logo, "Hybrid", "CVT", 5, true, "Electric/Hybrid"),
        Car("38", "Leaf", "Nissan", "Leaf", 2022, 3200.0, 4.3f, R.drawable.white_back_logo, "Electric", "Automatic", 5, true, "Electric/Hybrid"),
        Car("39", "Outlander PHEV", "Mitsubishi", "Outlander PHEV", 2023, 4500.0, 4.6f, R.drawable.white_back_logo, "Hybrid", "CVT", 5, true, "Electric/Hybrid"),
        Car("40", "Ioniq", "Hyundai", "Ioniq", 2022, 3400.0, 4.4f, R.drawable.white_back_logo, "Hybrid", "Automatic", 5, true, "Electric/Hybrid"),
        Car("41", "Model 3", "Tesla", "Model 3", 2023, 8500.0, 4.9f, R.drawable.white_back_logo, "Electric", "Automatic", 5, true, "Electric/Hybrid"),
        Car("42", "Dolphin", "BYD", "Dolphin", 2023, 3800.0, 4.4f, R.drawable.white_back_logo, "Electric", "Automatic", 5, true, "Electric/Hybrid"),
        Car("43", "ZS EV", "MG", "ZS EV", 2022, 3600.0, 4.2f, R.drawable.white_back_logo, "Electric", "Automatic", 5, true, "Electric/Hybrid")
    )

    fun getCarById(carId: String): Car? {
        return philippineCars.find { it.id == carId }
    }

    fun getAllCars(): List<Car> {
        return philippineCars
    }

    fun getCarsByCategory(category: String): List<Car> {
        return if (category == "All") {
            philippineCars
        } else {
            philippineCars.filter { it.category == category }
        }
    }

    fun getCarsByBrand(brand: String): List<Car> {
        return if (brand == "All Brands") {
            philippineCars
        } else {
            philippineCars.filter { it.brand == brand }
        }
    }

    fun searchCars(query: String): List<Car> {
        return philippineCars.filter { car ->
            car.name.contains(query, ignoreCase = true) ||
                    car.brand.contains(query, ignoreCase = true) ||
                    car.model.contains(query, ignoreCase = true)
        }
    }
}