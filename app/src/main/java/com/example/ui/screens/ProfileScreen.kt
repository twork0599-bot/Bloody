package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.LifeStrings
import com.example.model.BloodGroup
import com.example.model.LifedropUser
import com.example.ui.components.BloodGroupBadge
import com.example.ui.navigation.Screen
import com.example.ui.theme.AlertOrange
import com.example.ui.theme.CrimsonPrimary
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.MedicalGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: LifedropUser,
    lang: AppLanguage,
    isDarkTheme: Boolean,
    onLanguageChange: (AppLanguage) -> Unit,
    onThemeToggle: () -> Unit,
    onUpdateUser: (LifedropUser) -> Unit,
    onSignOut: () -> Unit,
    onDeleteAccount: () -> Unit,
    onNavigate: (Screen) -> Unit
) {
    val context = LocalContext.current
    var isEditing by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showSafetyGuidelineDialog by remember { mutableStateOf(false) }

    // Edit fields
    var editName by remember { mutableStateOf(user.fullName) }
    var editBio by remember { mutableStateOf(user.bio) }
    var editDistrict by remember { mutableStateOf(user.district) }
    var editUpazila by remember { mutableStateOf(user.upazila) }
    var editBloodGroup by remember { mutableStateOf(user.bloodGroup) }
    var bloodMenuExpanded by remember { mutableStateOf(false) }

    var hidePhone by remember { mutableStateOf(user.hidePhone) }
    var approxLocationOnly by remember { mutableStateOf(user.approximateLocationOnly) }
    var biometricsEnabled by remember { mutableStateOf(user.biometricsEnabled) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Summary Card
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BloodGroupBadge(bloodGroup = user.bloodGroup, size = 68.dp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = user.fullName,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )
                        if (user.isVerified) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified",
                                tint = MedicalGreen,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Text(
                        text = "${user.upazila}, ${user.district}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (user.isAvailable) MedicalGreen.copy(alpha = 0.12f) else AlertOrange.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = if (user.isAvailable) LifeStrings.get("status_available", lang) else LifeStrings.get("status_unavailable", lang),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            color = if (user.isAvailable) MedicalGreen else AlertOrange,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }

                    if (user.bio.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = user.bio,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { isEditing = true },
                        modifier = Modifier.height(38.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(LifeStrings.get("edit_profile", lang), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Language & Appearance Settings
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Preferences",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Language Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Language, contentDescription = "Language", tint = CrimsonPrimary)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(LifeStrings.get("app_language_setting", lang), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            FilterChip(
                                selected = lang == AppLanguage.ENGLISH,
                                onClick = { onLanguageChange(AppLanguage.ENGLISH) },
                                label = { Text("English", fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CrimsonPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                            FilterChip(
                                selected = lang == AppLanguage.BANGLA,
                                onClick = { onLanguageChange(AppLanguage.BANGLA) },
                                label = { Text("বাংলা", fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CrimsonPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Dark Mode Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.DarkMode, contentDescription = "Dark Theme", tint = CrimsonPrimary)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(LifeStrings.get("dark_mode_setting", lang), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }

                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { onThemeToggle() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = CrimsonPrimary
                            )
                        )
                    }
                }
            }
        }

        // Privacy & Security Controls
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = LifeStrings.get("privacy_settings", lang),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Hide Phone Number
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(LifeStrings.get("hide_phone_toggle", lang), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            Text("Only accepted match participants can see your contact", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = hidePhone,
                            onCheckedChange = {
                                hidePhone = it
                                onUpdateUser(user.copy(hidePhone = it))
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = CrimsonPrimary
                            )
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Approximate Location Only
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(LifeStrings.get("approximate_location_toggle", lang), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            Text("Never expose precise GPS or house address publicly", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = approxLocationOnly,
                            onCheckedChange = {
                                approxLocationOnly = it
                                onUpdateUser(user.copy(approximateLocationOnly = it))
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = CrimsonPrimary
                            )
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Biometric Authentication
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(LifeStrings.get("biometrics_prompt", lang), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            Text(LifeStrings.get("biometrics_desc", lang), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = biometricsEnabled,
                            onCheckedChange = {
                                biometricsEnabled = it
                                onUpdateUser(user.copy(biometricsEnabled = it))
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = CrimsonPrimary
                            )
                        )
                    }
                }
            }
        }

        // Donor Safety & Medical Checklist
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showSafetyGuidelineDialog = true },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.HealthAndSafety, contentDescription = "Safety", tint = MedicalGreen, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(LifeStrings.get("safety_guide_title", lang), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("View standard donation intervals and self-declaration checklist", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // Account Actions
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onSignOut,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Log out", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(LifeStrings.get("logout", lang), fontSize = 12.sp)
                }

                Button(
                    onClick = { showDeleteConfirm = true },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(LifeStrings.get("delete_account", lang), color = Color.White, fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Edit Profile Dialog
    if (isEditing) {
        AlertDialog(
            onDismissRequest = { isEditing = false },
            title = { Text(LifeStrings.get("edit_profile", lang), fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Blood Group
                    ExposedDropdownMenuBox(
                        expanded = bloodMenuExpanded,
                        onExpandedChange = { bloodMenuExpanded = !bloodMenuExpanded }
                    ) {
                        OutlinedTextField(
                            value = editBloodGroup.label,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Blood Group") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = bloodMenuExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = bloodMenuExpanded,
                            onDismissRequest = { bloodMenuExpanded = false }
                        ) {
                            BloodGroup.entries.forEach { bg ->
                                DropdownMenuItem(
                                    text = { Text(bg.label, fontWeight = FontWeight.Bold) },
                                    onClick = {
                                        editBloodGroup = bg
                                        bloodMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = editDistrict,
                        onValueChange = { editDistrict = it },
                        label = { Text("District") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = editUpazila,
                        onValueChange = { editUpazila = it },
                        label = { Text("Upazila / Area") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = editBio,
                        onValueChange = { editBio = it },
                        label = { Text("Short Bio") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val updated = user.copy(
                            fullName = editName,
                            bloodGroup = editBloodGroup,
                            district = editDistrict,
                            upazila = editUpazila,
                            bio = editBio
                        )
                        onUpdateUser(updated)
                        isEditing = false
                        Toast.makeText(context, LifeStrings.get("save_changes", lang), Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary)
                ) {
                    Text(LifeStrings.get("save_changes", lang))
                }
            },
            dismissButton = {
                TextButton(onClick = { isEditing = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Safety Guidelines Modal
    if (showSafetyGuidelineDialog) {
        AlertDialog(
            onDismissRequest = { showSafetyGuidelineDialog = false },
            title = { Text(LifeStrings.get("safety_guide_title", lang), fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("• " + LifeStrings.get("rule_age", lang), fontSize = 13.sp)
                    Text("• " + LifeStrings.get("rule_weight", lang), fontSize = 13.sp)
                    Text("• " + LifeStrings.get("rule_interval", lang), fontSize = 13.sp)
                    Text("• " + LifeStrings.get("rule_hemoglobin", lang), fontSize = 13.sp)
                    Text("• " + LifeStrings.get("rule_health", lang), fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Clinical Notice: Actual medical eligibility must be determined by qualified medical personnel at the donation center.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showSafetyGuidelineDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary)
                ) {
                    Text("Understood")
                }
            }
        )
    }

    // Delete Account Confirmation Dialog
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text(LifeStrings.get("delete_account", lang), color = EmergencyRed, fontWeight = FontWeight.Bold) },
            text = { Text(LifeStrings.get("delete_account_confirm", lang)) },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirm = false
                        onDeleteAccount()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed)
                ) {
                    Text("Permanently Delete", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
