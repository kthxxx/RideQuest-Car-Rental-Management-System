package com.example.ridequestcarrentalapp.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import androidx.compose.ui.tooling.preview.Preview // Import for Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ridequestcarrentalapp.R
import com.example.ridequestcarrentalapp.ui.theme.Helvetica
import com.example.ridequestcarrentalapp.ui.theme.Orange

@Composable
fun SignUpScreen(
    onSignUpClick: () -> Unit,
    onLoginClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var agreeToTerms by remember { mutableStateOf(false) }

    val isFormValid = name.isNotBlank() &&
            email.isNotBlank() &&
            phone.isNotBlank() &&
            password.length >= 6 &&
            agreeToTerms

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Logo
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Orange),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.white_back_logo),
                    contentDescription = "RideQuest Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Create Account",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Helvetica,
                color = Orange
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Sign up now and enjoy rental ease like\nnever before.",
                fontSize = 14.sp,
                fontFamily = Helvetica,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Name Field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Full Name",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Helvetica,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("Juan Dela Cruz", color = Color.Gray, fontSize = 14.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Orange,
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                        unfocusedContainerColor = Color.Gray.copy(alpha = 0.05f),
                        focusedContainerColor = Color.Gray.copy(alpha = 0.05f)
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Email Field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Email",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Helvetica,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it.trim() },
                    placeholder = { Text("example@gmail.com", color = Color.Gray, fontSize = 14.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Orange,
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                        unfocusedContainerColor = Color.Gray.copy(alpha = 0.05f),
                        focusedContainerColor = Color.Gray.copy(alpha = 0.05f)
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Phone Field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Phone Number",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Helvetica,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = { Text("+63 912 345 6789", color = Color.Gray, fontSize = 14.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Orange,
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                        unfocusedContainerColor = Color.Gray.copy(alpha = 0.05f),
                        focusedContainerColor = Color.Gray.copy(alpha = 0.05f)
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Password",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Helvetica,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Minimum 6 characters", color = Color.Gray, fontSize = 14.sp) },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Orange,
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                        unfocusedContainerColor = Color.Gray.copy(alpha = 0.05f),
                        focusedContainerColor = Color.Gray.copy(alpha = 0.05f)
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Terms and Conditions
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Checkbox(
                    checked = agreeToTerms,
                    onCheckedChange = { agreeToTerms = it },
                    colors = CheckboxDefaults.colors(checkedColor = Orange)
                )

                val annotatedText = buildAnnotatedString {
                    append("I agree to the ")
                    withStyle(
                        style = SpanStyle(
                            color = Orange,
                            textDecoration = TextDecoration.Underline,
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append("Terms & Conditions")
                    }
                    append(" and ")
                    withStyle(
                        style = SpanStyle(
                            color = Orange,
                            textDecoration = TextDecoration.Underline,
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append("Privacy Policy")
                    }
                }

                Text(
                    text = annotatedText,
                    fontSize = 13.sp,
                    fontFamily = Helvetica,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 8.dp, top = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign Up Button
            Button(
                onClick = { onSignUpClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(28.dp),
                enabled = isFormValid
            ) {
                Text(
                    text = "Create Account",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Helvetica
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign In Link
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Text(
                    text = "Already have an account? ",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontFamily = Helvetica
                )
                Text(
                    text = "Sign In",
                    color = Orange,
                    fontSize = 14.sp,
                    fontFamily = Helvetica,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }
        }
    }
}

// Composable Preview
@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(
        onSignUpClick = { /* Do nothing for preview */ },
        onLoginClick = { /* Do nothing for preview */ },
        onBackClick = { /* Do nothing for preview */ }
    )
}