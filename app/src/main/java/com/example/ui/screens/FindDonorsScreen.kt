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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.DonorMatchingEngine
import com.example.localization.AppLanguage
import com.example.localization.LifeStrings
import com.example.model.BloodGroup
import com.example.model.LifedropUser
import com.example.ui.components.BloodGroupBadge
import com.example.ui.components.RadarMapView
import com.example.ui.navigation.Screen
import com.example.ui.theme.CrimsonPrimary
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.MedicalGreen

@Composable
fun FindDonorsScreen(
    donors: List<LifedropUser>,
    currentUser: LifedropUser,
    lang: AppLanguage,
    onNavigate: (Screen) -> Unit,
    onReportDonor: (donorId: String, reason: String, details: String) -> Unit
) {
    val context = LocalContext.current
    var selectedBloodGroupFilter by remember { mutableStateOf<BloodGroup?>(null) }
    var radiusKm by remember { mutableFloatStateOf(25f) }
    var isAvailableOnly by remember { mutableStateOf(false) }
    var viewModeTab by remember { mutableIntStateOf(0) } // 0: Radar, 1: List

    var reportDonorTarget by remember { mutableStateOf<LifedropUser?>(null) }
    var reportReason by remember { mutableStateOf("") }
    var reportDetails by remember { mutableStateOf("") }

    val filteredDonors = donors.filter { donor ->
        val bgMatch = selectedBloodGroupFilter == null || donor.bloodGroup == selectedBloodGroupFilter
        val availMatch = !isAvailableOnly || donor.isAvailable
        val dist = DonorMatchingEngine.calculateDistanceKm(
            currentUser.latitude, currentUser.longitude,
            donor.latitude, donor.longitude
        )
        val distMatch = dist <= radiusKm
        bgMatch && availMatch && distMatch
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Tab switcher: Radar Map vs List
        TabRow(
            selectedTabIndex = viewModeTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = CrimsonPrimary
        ) {
            Tab(
                selected = viewModeTab == 0,
                onClick = { viewModeTab = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Radar, contentDescription = "Radar", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(LifeStrings.get("view_radar", lang), fontWeight = FontWeight.Bold)
                    }
                }
            )
            Tab(
                selected = viewModeTab == 1,
                onClick = { viewModeTab = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.FilterList, contentDescription = "List", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(LifeStrings.get("view_list", lang), fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Radar Map View if tab 0
            if (viewModeTab == 0) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    RadarMapView(
                        radiusKm = radiusKm.toInt(),
                        donorCount = filteredDonors.size,
                        hospitalCount = 4
                    )
                }
            }

            // Blood Group Horizontal Filter Chips
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = LifeStrings.get("blood_group", lang),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = selectedBloodGroupFilter == null,
                            onClick = { selectedBloodGroupFilter = null },
                            label = { Text(LifeStrings.get("filter_all", lang)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CrimsonPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                    items(BloodGroup.entries) { bg ->
                        FilterChip(
                            selected = selectedBloodGroupFilter == bg,
                            onClick = { selectedBloodGroupFilter = bg },
                            label = { Text(bg.label, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CrimsonPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Distance Radius Slider
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${LifeStrings.get("filter_distance", lang)}: ${radiusKm.toInt()} km",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "${filteredDonors.size} Donors in range",
                                fontWeight = FontWeight.Bold,
                                color = CrimsonPrimary,
                                fontSize = 13.sp
                            )
                        }
                        Slider(
                            value = radiusKm,
                            onValueChange = { radiusKm = it },
                            valueRange = 1f..50f,
                            steps = 5,
                            colors = SliderDefaults.colors(
                                thumbColor = CrimsonPrimary,
                                activeTrackColor = CrimsonPrimary
                            )
                        )
                    }
                }
            }

            // Filter status summary
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Compatible Candidates (${filteredDonors.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }

            // Donor Cards
            items(filteredDonors) { donor ->
                val distance = DonorMatchingEngine.calculateDistanceKm(
                    currentUser.latitude, currentUser.longitude,
                    donor.latitude, donor.longitude
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("donor_card_${donor.id}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                BloodGroupBadge(bloodGroup = donor.bloodGroup, size = 46.dp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = donor.fullName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        )
                                        if (donor.isVerified) {
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(
                                                imageVector = Icons.Default.Verified,
                                                contentDescription = "Verified",
                                                tint = MedicalGreen,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        text = "${donor.upazila}, ${donor.district} • $distance km",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // Report button
                            IconButton(onClick = { reportDonorTarget = donor }) {
                                Icon(
                                    imageVector = Icons.Default.Flag,
                                    contentDescription = "Report",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        if (donor.bio.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = donor.bio,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${LifeStrings.get("total_donations", lang)}: ${donor.verifiedDonationsCount}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${LifeStrings.get("last_donated", lang)}: ${donor.lastDonationDate}",
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
                                onClick = { onNavigate(Screen.Messages) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary)
                            ) {
                                Icon(Icons.Default.Chat, contentDescription = "Chat", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(LifeStrings.get("contact_donor", lang), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            OutlinedButton(
                                onClick = {
                                    Toast.makeText(context, "Dialing ${donor.phone}...", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = "Call", tint = CrimsonPrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(LifeStrings.get("call_donor", lang), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CrimsonPrimary)
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Report User / Fake Account Dialog
    if (reportDonorTarget != null) {
        AlertDialog(
            onDismissRequest = { reportDonorTarget = null },
            title = { Text(LifeStrings.get("report_request", lang), fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Reporting ${reportDonorTarget?.fullName}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = reportReason,
                        onValueChange = { reportReason = it },
                        label = { Text(LifeStrings.get("report_reason", lang)) },
                        placeholder = { Text("e.g. Inactive phone / Asking for money") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = reportDetails,
                        onValueChange = { reportDetails = it },
                        label = { Text("Additional Details") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val target = reportDonorTarget
                        if (target != null) {
                            onReportDonor(target.id, reportReason, reportDetails)
                            Toast.makeText(context, LifeStrings.get("report_submitted", lang), Toast.LENGTH_LONG).show()
                        }
                        reportDonorTarget = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed)
                ) {
                    Text("Submit Report", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { reportDonorTarget = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}
