package com.example.ridequestcarrentalapp.ui.profile

// Updated UserProfile with complete information
data class CompleteUserProfile(
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
    val membershipTier: MembershipTier,

    // Personal Information
    val dateOfBirth: String = "",
    val gender: String = "",
    val age: Int = 0,

    // Address Information
    val streetAddress: String = "",
    val barangay: String = "",
    val city: String = "",
    val province: String = "",
    val postalCode: String = "",
    val completeAddress: String = "",

    // Emergency Contact
    val emergencyContactName: String = "",
    val emergencyContactNumber: String = "",
    val emergencyContactRelationship: String = "",

    // Driver's License Information
    val licenseNumber: String = "",
    val licenseType: String = "",
    val licenseIssueDate: String = "",
    val licenseExpiryDate: String = "",
    val licenseFrontPhoto: String = "",
    val licenseBackPhoto: String = "",
    val licenseStatus: LicenseStatus = LicenseStatus.VERIFIED
)

enum class LicenseStatus {
    VERIFIED,
    PENDING,
    EXPIRED,
    NOT_SUBMITTED
}

// Extension function to get complete address
fun CompleteUserProfile.getFullAddress(): String {
    return buildString {
        if (streetAddress.isNotEmpty()) append("$streetAddress, ")
        if (barangay.isNotEmpty()) append("$barangay, ")
        if (city.isNotEmpty()) append("$city, ")
        if (province.isNotEmpty()) append("$province ")
        if (postalCode.isNotEmpty()) append(postalCode)
    }.trim().removeSuffix(",")
}

// Extension function to check if profile is complete
fun CompleteUserProfile.isProfileComplete(): Boolean {
    return name.isNotEmpty() &&
            email.isNotEmpty() &&
            phone.isNotEmpty() &&
            dateOfBirth.isNotEmpty() &&
            gender.isNotEmpty() &&
            streetAddress.isNotEmpty() &&
            city.isNotEmpty() &&
            province.isNotEmpty() &&
            licenseNumber.isNotEmpty() &&
            licenseStatus == LicenseStatus.VERIFIED
}

// Extension function to get profile completion percentage
fun CompleteUserProfile.getProfileCompletionPercentage(): Int {
    val fields = listOf(
        name.isNotEmpty(),
        email.isNotEmpty(),
        phone.isNotEmpty(),
        dateOfBirth.isNotEmpty(),
        gender.isNotEmpty(),
        streetAddress.isNotEmpty(),
        city.isNotEmpty(),
        province.isNotEmpty(),
        emergencyContactName.isNotEmpty(),
        emergencyContactNumber.isNotEmpty(),
        licenseNumber.isNotEmpty(),
        licenseStatus == LicenseStatus.VERIFIED
    )
    return (fields.count { it } * 100) / fields.size
}