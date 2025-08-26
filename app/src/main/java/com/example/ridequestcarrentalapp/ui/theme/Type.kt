package com.example.ridequestcarrentalapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ridequestcarrentalapp.R

// ✅ Define Helvetica font family
val Helvetica = FontFamily(
    Font(R.font.helvetica_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.helvetica_bold_oblique, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.helvetica_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.helvetica_oblique, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.helvetica_compressed, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.helvetica_rounded_bold, FontWeight.SemiBold, FontStyle.Normal)
)

// ✅ Apply Helvetica in Material Typography
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Helvetica,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Helvetica,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Helvetica,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
