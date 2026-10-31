package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.localization.AppLanguage
import com.example.localization.LifeStrings
import com.example.model.BloodGroup
import com.example.ui.theme.CrimsonPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    lang: AppLanguage,
    onLoginSuccess: (name: String, identifier: String, bloodGroup: BloodGroup, isRegister: Boolean) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var name by remember { mutableStateOf("") }
    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedBloodGroup by remember { mutableStateOf(BloodGroup.O_POS) }
    var bloodDropdownExpanded by remember { mutableStateOf(false) }

    var showOtpDialog by remember { mutableStateOf(false) }
    var otpCode by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Brand Logo & Header
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .background(CrimsonPrimary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Bloodtype,
                contentDescription = "Lifedrop Logo",
                tint = Color.White,
                modifier = Modifier.size(44.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = LifeStrings.get("app_name", lang),
            fontWeight = FontWeight.Black,
            fontSize = 28.sp,
            color = CrimsonPrimary
        )
        Text(
            text = LifeStrings.get("tagline", lang),
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Sign In with Google Button (Mandatory standard cloud provider)
        Button(
            onClick = {
                onLoginSuccess("Tanvir Rahman", "tanvir.lifedrop@example.com", BloodGroup.O_POS, false)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("google_signin_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Google Auth",
                modifier = Modifier.size(18.dp),
                tint = CrimsonPrimary
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = LifeStrings.get("sign_in_google", lang),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(modifier = Modifier.weight(1f))
            Text(
                text = LifeStrings.get("or_use_phone_email", lang),
                modifier = Modifier.padding(horizontal = 12.dp),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Divider(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tabs: Sign In / Register
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = CrimsonPrimary
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text(LifeStrings.get("tab_login", lang), fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text(LifeStrings.get("tab_register", lang), fontWeight = FontWeight.Bold) }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Registration fields if Register tab selected
        if (selectedTab == 1) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(LifeStrings.get("full_name", lang)) },
                modifier = Modifier.fillMaxWidth().testTag("name_input"),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Blood Group selector
            ExposedDropdownMenuBox(
                expanded = bloodDropdownExpanded,
                onExpandedChange = { bloodDropdownExpanded = !bloodDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = selectedBloodGroup.label,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(LifeStrings.get("blood_group", lang)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = bloodDropdownExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = bloodDropdownExpanded,
                    onDismissRequest = { bloodDropdownExpanded = false }
                ) {
                    BloodGroup.entries.forEach { bg ->
                        DropdownMenuItem(
                            text = { Text(bg.label, fontWeight = FontWeight.Bold) },
                            onClick = {
                                selectedBloodGroup = bg
                                bloodDropdownExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Identifier: Phone or Email
        OutlinedTextField(
            value = identifier,
            onValueChange = { identifier = it },
            label = { Text(LifeStrings.get("phone_number", lang) + " / " + LifeStrings.get("email_address", lang)) },
            modifier = Modifier.fillMaxWidth().testTag("identifier_input"),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(LifeStrings.get("password", lang)) },
            modifier = Modifier.fillMaxWidth().testTag("password_input"),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Submit action
        Button(
            onClick = {
                // Trigger OTP simulation
                showOtpDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("auth_submit_button"),
            colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (selectedTab == 0) LifeStrings.get("tab_login", lang) else LifeStrings.get("tab_register", lang),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Biometric / Quick demo option
        OutlinedButton(
            onClick = {
                onLoginSuccess("Dr. Farhana Ahmed", "+880 1819-112233", BloodGroup.O_NEG, false)
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Default.Fingerprint, contentDescription = "Biometric", tint = CrimsonPrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Biometric Instant Sign-In", fontWeight = FontWeight.SemiBold, color = CrimsonPrimary)
        }
    }

    // OTP Verification Sheet/Card
    if (showOtpDialog) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { showOtpDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "OTP",
                        tint = CrimsonPrimary,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = LifeStrings.get("enter_otp", lang),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "A secure verification code has been generated for demo: 849201",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = otpCode,
                        onValueChange = { otpCode = it },
                        placeholder = { Text("849201") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            showOtpDialog = false
                            onLoginSuccess(
                                if (name.isNotBlank()) name else "Tanvir Rahman",
                                if (identifier.isNotBlank()) identifier else "+880 1711-234567",
                                selectedBloodGroup,
                                selectedTab == 1
                            )
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(LifeStrings.get("verify_otp", lang), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
