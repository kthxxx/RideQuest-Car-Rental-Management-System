package com.example.ridequestcarrentalapp.ui.feature.onbound

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ridequestcarrentalapp.R
import com.example.ridequestcarrentalapp.ui.theme.Helvetica
import com.example.ridequestcarrentalapp.ui.theme.Orange
import com.example.ridequestcarrentalapp.ui.theme.White

@Composable
fun OnBoundScreen2(onContinueClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.onboarding_2),
            contentDescription = "Logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Button on top of the Image
        Button(
            onClick = { onContinueClicked () },
            colors = ButtonDefaults.buttonColors(
                containerColor = Orange,
                contentColor = White
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp)
                .width(305.dp)
                .height(57.dp)
        ) {
            Text(
                text = "Continue",
                fontFamily = Helvetica,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}
