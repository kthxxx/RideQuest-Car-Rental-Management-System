package com.example.ridequestcarrentalapp.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ridequestcarrentalapp.R
import com.example.ridequestcarrentalapp.ui.theme.Helvetica
import com.example.ridequestcarrentalapp.ui.theme.Orange

@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onGoogleSignInClick: () -> Unit = {},
    onFacebookSignInClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

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

            Spacer(modifier = Modifier.height(32.dp))

            // Title
            Text(
                text = "Sign In",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Helvetica,
                color = Orange
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Sign in now and enjoy rental ease\nlike never before.",
                fontSize = 14.sp,
                fontFamily = Helvetica,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("example@gmail.com", color = Color.Gray, fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
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

            Spacer(modifier = Modifier.height(20.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("••••••••", color = Color.Gray, fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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

            Spacer(modifier = Modifier.height(16.dp))

            // Forgot Password
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(
                    text = "Forgot Password?",
                    color = Orange,
                    fontSize = 14.sp,
                    fontFamily = Helvetica,
                    modifier = Modifier.clickable { onForgotPasswordClick() }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sign In Button
            Button(
                onClick = {
                    // Simple validation and callback
                    if (email.isNotBlank() && password.isNotBlank()) {
                        onLoginClick(email, password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(28.dp),
                enabled = email.isNotBlank() && password.isNotBlank()
            ) {
                Text("Sign In", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, fontFamily = Helvetica)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Or sign in with
            Text(
                text = "Or sign in with",
                color = Color.Gray,
                fontSize = 14.sp,
                fontFamily = Helvetica
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Facebook
                IconButton(
                    onClick = { onFacebookSignInClick() },
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFF1877F2), CircleShape)
                ) {
                    Text("f", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }

                // Google
                IconButton(
                    onClick = { onGoogleSignInClick() },
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Text("G", color = Color(0xFF4285F4), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Sign Up Link
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Text("Don't have an account? ", color = Color.Gray, fontSize = 14.sp, fontFamily = Helvetica)
                Text(
                    text = "Sign Up",
                    color = Orange,
                    fontSize = 14.sp,
                    fontFamily = Helvetica,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onSignUpClick() }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onLoginClick = { email, password ->
            println("Login: $email")
        },
        onSignUpClick = { },
        onForgotPasswordClick = { },
        onGoogleSignInClick = { },
        onFacebookSignInClick = { }
    )
}