package com.roxfollow.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private enum class AuthMode {
    SIGN_IN,
    SIGN_UP,
    FORGOT_PASSWORD
}

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {}
) {
    var authMode by remember { mutableStateOf(AuthMode.SIGN_IN) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }
    var referralCode by remember { mutableStateOf("") }

    var otp by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    var success by remember { mutableStateOf("") }

    val darkBg = Color(0xFF08090D)
    val cardBg = Color(0xFF111318)
    val fieldBg = Color(0xFF181A20)
    val primary = Color(0xFF7C4DFF)
    val textPrimary = Color.White
    val textSecondary = Color(0xFF9CA3AF)

    fun clearMessages() {
        error = ""
        success = ""
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBg),
        color = darkBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "ROX FOLLOW",
                color = textPrimary,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (authMode == AuthMode.SIGN_UP) {
                Text(
                    text = "+50 Free Coins on Sign Up",
                    color = primary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            } else {
                Text(
                    text = "Secure Cloud",
                    color = textSecondary,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardBg
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    if (authMode == AuthMode.FORGOT_PASSWORD) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    clearMessages()
                                    authMode = AuthMode.SIGN_IN
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = textPrimary
                                )
                            }

                            Text(
                                text = "Reset Password",
                                color = textPrimary,
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Enter your registered email to receive a 6-digit OTP.",
                            color = textSecondary,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        RoxField(
                            value = email,
                            onValueChange = {
                                email = it
                                clearMessages()
                            },
                            label = "Email",
                            enabled = !loading
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        RoxField(
                            value = otp,
                            onValueChange = {
                                otp = it.take(6)
                                clearMessages()
                            },
                            label = "6-Digit OTP",
                            enabled = !loading
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        RoxPasswordField(
                            value = newPassword,
                            onValueChange = {
                                newPassword = it
                                clearMessages()
                            },
                            label = "New Password",
                            visible = passwordVisible,
                            onVisibilityChange = {
                                passwordVisible = !passwordVisible
                            },
                            enabled = !loading
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        RoxButton(
                            text = "Reset Password",
                            loading = loading,
                            primary = primary,
                            onClick = {
                                clearMessages()

                                if (email.isBlank()) {
                                    error = "Please enter your email."
                                    return@RoxButton
                                }

                                if (otp.length != 6) {
                                    error = "Please enter the 6-digit OTP."
                                    return@RoxButton
                                }

                                if (newPassword.length < 6) {
                                    error = "Password must be at least 6 characters."
                                    return@RoxButton
                                }

                                success = "Reset request ready."
                            }
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        TextButton(
                            onClick = {
                                clearMessages()
                                authMode = AuthMode.SIGN_IN
                            }
                        ) {
                            Text(
                                text = "Back to Sign In",
                                color = primary
                            )
                        }

                    } else {

                        Text(
                            text = if (authMode == AuthMode.SIGN_IN)
                                "Sign In to Your Account"
                            else
                                "Create Your Account",
                            color = textPrimary,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        RoxButton(
                            text = "Sign in with Google",
                            loading = false,
                            primary = primary,
                            onClick = {
                                clearMessages()
                                success = "Google sign-in will be connected in the Firebase integration step."
                            }
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = "OR",
                            modifier = Modifier.fillMaxWidth(),
                            color = textSecondary,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        RoxField(
                            value = email,
                            onValueChange = {
                                email = it
                                clearMessages()
                            },
                            label = "Email",
                            enabled = !loading
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        RoxPasswordField(
                            value = password,
                            onValueChange = {
                                password = it
                                clearMessages()
                            },
                            label = "Password",
                            visible = passwordVisible,
                            onVisibilityChange = {
                                passwordVisible = !passwordVisible
                            },
                            enabled = !loading
                        )

                        if (authMode == AuthMode.SIGN_UP) {

                            Spacer(modifier = Modifier.height(14.dp))

                            RoxField(
                                value = displayName,
                                onValueChange = {
                                    displayName = it
                                    clearMessages()
                                },
                                label = "Display Name",
                                enabled = !loading
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            RoxPasswordField(
                                value = confirmPassword,
                                onValueChange = {
                                    confirmPassword = it
                                    clearMessages()
                                },
                                label = "Confirm Password",
                                visible = passwordVisible,
                                onVisibilityChange = {
                                    passwordVisible = !passwordVisible
                                },
                                enabled = !loading
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            RoxField(
                                value = referralCode,
                                onValueChange = {
                                    referralCode = it
                                    clearMessages()
                                },
                                label = "Referral Code (Optional)",
                                enabled = !loading
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        RoxButton(
                            text = if (authMode == AuthMode.SIGN_IN)
                                "Sign In"
                            else
                                "Create Account",
                            loading = loading,
                            primary = primary,
                            onClick = {
                                clearMessages()

                                if (email.isBlank() || !email.contains("@")) {
                                    error = "Please enter a valid email."
                                    return@RoxButton
                                }

                                if (password.length < 6) {
                                    error = "Password must be at least 6 characters."
                                    return@RoxButton
                                }

                                if (
                                    authMode == AuthMode.SIGN_UP &&
                                    password != confirmPassword
                                ) {
                                    error = "Passwords do not match."
                                    return@RoxButton
                                }

                                loading = true

                                // Firebase Auth connection is intentionally
                                // kept for the next integration step.
                                loading = false
                                success =
                                    if (authMode == AuthMode.SIGN_IN)
                                        "Sign-in ready for Firebase connection."
                                    else
                                        "Account creation ready for Firebase connection."
                            }
                        )

                        if (authMode == AuthMode.SIGN_IN) {

                            TextButton(
                                onClick = {
                                    clearMessages()
                                    authMode = AuthMode.FORGOT_PASSWORD
                                }
                            ) {
                                Text(
                                    text = "Forgot password?",
                                    color = primary
                                )
                            }

                            TextButton(
                                onClick = {
                                    clearMessages()
                                    authMode = AuthMode.SIGN_UP
                                }
                            ) {
                                Text(
                                    text = "Create Account",
                                    color = textSecondary
                                )
                            )

                        } else {

                            TextButton(
                                onClick = {
                                    clearMessages()
                                    authMode = AuthMode.SIGN_IN
                                }
                            ) {
                                Text(
                                    text = "Switch to Sign In",
                                    color = primary
                                )
                            }
                        }
                    }

                    if (error.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = error,
                            color = Color(0xFFFF6B6B),
                            fontSize = 13.sp
                        )
                    }

                    if (success.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = success,
                            color = Color(0xFF4ADE80),
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "ROX FOLLOW v3.6.0 • Secure Cloud",
                color = textSecondary,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun RoxField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    enabled: Boolean
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        label = { Text(label) },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF7C4DFF),
            unfocusedBorderColor = Color(0xFF353943),
            focusedLabelColor = Color(0xFF7C4DFF),
            unfocusedLabelColor = Color(0xFF9CA3AF),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        )
    )
}

@Composable
private fun RoxPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visible: Boolean,
    onVisibilityChange: () -> Unit,
    enabled: Boolean
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        label = { Text(label) },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        visualTransformation =
            if (visible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onVisibilityChange) {
                Icon(
                    imageVector =
                        if (visible)
                            Icons.Default.VisibilityOff
                        else
                            Icons.Default.Visibility,
                    contentDescription = "Password visibility",
                    tint = Color(0xFF9CA3AF)
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF7C4DFF),
            unfocusedBorderColor = Color(0xFF353943),
            focusedLabelColor = Color(0xFF7C4DFF),
            unfocusedLabelColor = Color(0xFF9CA3AF),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        )
    )
}

@Composable
private fun RoxButton(
    text: String,
    loading: Boolean,
    primary: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = !loading,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = primary
        )
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = text,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
