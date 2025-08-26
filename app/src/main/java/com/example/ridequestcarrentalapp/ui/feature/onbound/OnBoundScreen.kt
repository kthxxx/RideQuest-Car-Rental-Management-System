package com.example.ridequestcarrentalapp.ui.feature.onbound

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import com.example.ridequestcarrentalapp.R
import com.example.ridequestcarrentalapp.ui.theme.Helvetica
import com.example.ridequestcarrentalapp.ui.theme.Orange

@Composable
fun OnBoundScreen(onContinueClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Orange)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.white_back_logo),
                contentDescription = "Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(250.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = "RideQuest",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Helvetica,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(350.dp)) // space before button

            // Button
            Button(
                onClick = { onContinueClicked() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Orange
                ),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
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
}
