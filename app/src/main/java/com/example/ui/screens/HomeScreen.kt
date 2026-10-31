package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.localization.AppLanguage
import com.example.localization.LifeStrings
import com.example.model.BloodRequest
import com.example.model.LifedropUser
import com.example.model.UrgencyLevel
import com.example.ui.components.BloodGroupBadge
import com.example.ui.navigation.Screen
import com.example.ui.theme.AlertOrange
import com.example.ui.theme.CrimsonDark
import com.example.ui.theme.CrimsonPrimary
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.MedicalGreen

@Composable
fun HomeScreen(
    user: LifedropUser,
    activeRequests: List<BloodRequest>,
    nearbyDonors: List<LifedropUser>,
    lang: AppLanguage,
    onNavigate: (Screen) -> Unit,
    onToggleAvailability: () -> Unit,
    onRespondToRequest: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Greeting & Availability Card
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BloodGroupBadge(bloodGroup = user.bloodGroup, size = 52.dp)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "${LifeStrings.get("hello_donor", lang)} ${user.fullName.split(" ").firstOrNull() ?: ""}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Location",
                                    tint = CrimsonPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "${user.upazila}, ${user.district}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = if (user.isAvailable) LifeStrings.get("eligible_now", lang) else LifeStrings.get("status_unavailable", lang),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (user.isAvailable) MedicalGreen else AlertOrange
                            )
                        }
                    }

                    // Availability Toggle
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Switch(
                            checked = user.isAvailable,
                            onCheckedChange = { onToggleAvailability() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = MedicalGreen
                            )
                        )
                        Text(
                            text = if (user.isAvailable) LifeStrings.get("filter_available_only", lang) else "Paused",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Hero Banner with Lifedrop illustration
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.lifedrop_hero_banner_1793475208104),
                        contentDescription = "Lifedrop Heroes",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color(0xCC000000))
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = LifeStrings.get("tagline", lang),
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp
                        )
                        Text(
                            text = "Instant smart matching across all blood components.",
                            color = Color.LightGray,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Prominent Emergency Broadcast CTA
        item {
            Button(
                onClick = { onNavigate(Screen.EmergencyBroadcast) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("broadcast_emergency_cta"),
                colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Emergency,
                    contentDescription = "Emergency Icon",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = LifeStrings.get("broadcast_emergency_btn", lang),
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }
        }

        // Quick Feature Shortcut Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionCard(
                    title = LifeStrings.get("nav_compatibility", lang),
                    icon = Icons.Default.Bloodtype,
                    color = CrimsonPrimary,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigate(Screen.Compatibility) }
                )
                QuickActionCard(
                    title = LifeStrings.get("nav_hospitals", lang),
                    icon = Icons.Default.LocalHospital,
                    color = Color(0xFF2563EB),
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigate(Screen.Hospitals) }
                )
                QuickActionCard(
                    title = LifeStrings.get("nav_history", lang),
                    icon = Icons.Default.WorkspacePremium,
                    color = Color(0xFFD97706),
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigate(Screen.DonationHistory) }
                )
                QuickActionCard(
                    title = LifeStrings.get("nav_admin", lang),
                    icon = Icons.Default.AdminPanelSettings,
                    color = Color(0xFF4B5563),
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigate(Screen.AdminHub) }
                )
            }
        }

        // Active Emergency Blood Requests Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = LifeStrings.get("urgent_requests_near_you", lang),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
                Text(
                    text = "${activeRequests.size} Active",
                    fontSize = 12.sp,
                    color = EmergencyRed,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Active Emergency Blood Requests List
        items(activeRequests) { request ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("request_item_${request.id}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            BloodGroupBadge(bloodGroup = request.bloodGroup, size = 42.dp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = request.patientName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "${request.unitsNeeded} unit(s) • ${request.component.id}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = when (request.urgency) {
                                UrgencyLevel.CRITICAL -> EmergencyRed.copy(alpha = 0.15f)
                                UrgencyLevel.URGENT -> AlertOrange.copy(alpha = 0.15f)
                                UrgencyLevel.NORMAL -> MedicalGreen.copy(alpha = 0.15f)
                            }
                        ) {
                            Text(
                                text = request.urgency.name,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (request.urgency) {
                                    UrgencyLevel.CRITICAL -> EmergencyRed
                                    UrgencyLevel.URGENT -> AlertOrange
                                    UrgencyLevel.NORMAL -> MedicalGreen
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "🏥 ${request.hospitalName}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = request.hospitalAddress,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (request.notes.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "\"${request.notes}\"",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onRespondToRequest(request.id) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary)
                        ) {
                            Text("I Can Donate", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Button(
                            onClick = { onNavigate(Screen.Messages) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        ) {
                            Text("Contact Family", fontWeight = FontWeight.Medium, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Smart Matched Donors Near You
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = LifeStrings.get("smart_matched_donors", lang),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
                Text(
                    text = "See All",
                    fontSize = 12.sp,
                    color = CrimsonPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigate(Screen.FindDonors) }
                )
            }
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(nearbyDonors) { donor ->
                    Card(
                        modifier = Modifier
                            .width(180.dp)
                            .clickable { onNavigate(Screen.FindDonors) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            BloodGroupBadge(bloodGroup = donor.bloodGroup, size = 44.dp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = donor.fullName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                maxLines = 1
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Verified, contentDescription = "Verified", tint = MedicalGreen, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text("Verified", fontSize = 10.sp, color = MedicalGreen)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${donor.upazila} • ${donor.verifiedDonationsCount} donations",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Platform Impact Statistics
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = LifeStrings.get("quick_stats_title", lang),
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(number = "1,420+", label = LifeStrings.get("stat_total_donations", lang), color = CrimsonPrimary, modifier = Modifier.weight(1f))
                StatCard(number = "3,900+", label = LifeStrings.get("stat_lives_saved", lang), color = MedicalGreen, modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(number = "650+", label = LifeStrings.get("stat_active_donors", lang), color = AlertOrange, modifier = Modifier.weight(1f))
                StatCard(number = "48", label = LifeStrings.get("stat_hospitals", lang), color = Color(0xFF2563EB), modifier = Modifier.weight(1f))
            }
        }

        // Medical Transfusion Safety Warning Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.HealthAndSafety,
                        contentDescription = "Medical Safety",
                        tint = CrimsonPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = LifeStrings.get("medical_safety_banner", lang),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

@Composable
fun StatCard(
    number: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = number, fontWeight = FontWeight.Black, fontSize = 20.sp, color = color)
            Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
