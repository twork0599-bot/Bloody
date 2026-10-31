package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.WifiTethering
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.localization.AppLanguage
import com.example.localization.LifeStrings
import com.example.model.BloodComponent
import com.example.model.BloodGroup
import com.example.model.BloodRequest
import com.example.model.LifedropUser
import com.example.model.RequestStatus
import com.example.model.UrgencyLevel
import com.example.ui.components.BloodGroupBadge
import com.example.ui.theme.AlertOrange
import com.example.ui.theme.CrimsonPrimary
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.MedicalGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyBroadcastScreen(
    currentUser: LifedropUser,
    activeRequests: List<BloodRequest>,
    lang: AppLanguage,
    onBroadcastRequest: (BloodRequest) -> Unit,
    onFulfillRequest: (String) -> Unit,
    onCancelRequest: (String) -> Unit
) {
    val context = LocalContext.current

    // Form fields
    var patientName by remember { mutableStateOf("") }
    var selectedBloodGroup by remember { mutableStateOf(BloodGroup.O_POS) }
    var bloodMenuExpanded by remember { mutableStateOf(false) }

    var selectedComponent by remember { mutableStateOf(BloodComponent.WHOLE_BLOOD) }
    var unitsNeeded by remember { mutableIntStateOf(1) }

    var hospitalName by remember { mutableStateOf("Dhaka Medical College Hospital") }
    var hospitalAddress by remember { mutableStateOf("Secretariat Rd, Shahbagh, Dhaka") }
    var contactPhone by remember { mutableStateOf(currentUser.phone) }
    var notes by remember { mutableStateOf("") }

    var urgency by remember { mutableStateOf(UrgencyLevel.CRITICAL) }
    var broadcastRadiusKm by remember { mutableIntStateOf(15) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EmergencyRed)
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.WifiTethering,
                            contentDescription = "Broadcast",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = LifeStrings.get("emergency_title", lang),
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Instantly alert compatible donors within geo-radius.",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Emergency Request Form Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "New Blood Request",
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Patient Name
                    OutlinedTextField(
                        value = patientName,
                        onValueChange = { patientName = it },
                        label = { Text(LifeStrings.get("patient_name", lang)) },
                        placeholder = { Text("e.g. Rahima Khatun") },
                        modifier = Modifier.fillMaxWidth().testTag("patient_name_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Blood Group & Component
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Blood group dropdown
                        ExposedDropdownMenuBox(
                            expanded = bloodMenuExpanded,
                            onExpandedChange = { bloodMenuExpanded = !bloodMenuExpanded },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = selectedBloodGroup.label,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text(LifeStrings.get("blood_group", lang)) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = bloodMenuExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = bloodMenuExpanded,
                                onDismissRequest = { bloodMenuExpanded = false }
                            ) {
                                BloodGroup.entries.forEach { bg ->
                                    DropdownMenuItem(
                                        text = { Text(bg.label, fontWeight = FontWeight.Bold) },
                                        onClick = {
                                            selectedBloodGroup = bg
                                            bloodMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Units Needed Stepper
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(LifeStrings.get("required_units", lang), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                IconButton(
                                    onClick = { if (unitsNeeded > 1) unitsNeeded-- },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "Decrease")
                                }
                                Text(
                                    text = "$unitsNeeded",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.sp,
                                    color = CrimsonPrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                IconButton(
                                    onClick = { if (unitsNeeded < 8) unitsNeeded++ },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Increase")
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Component Chips
                    Text(LifeStrings.get("blood_component", lang), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        BloodComponent.entries.forEach { comp ->
                            FilterChip(
                                selected = selectedComponent == comp,
                                onClick = { selectedComponent = comp },
                                label = { Text(comp.name.replace("_", " "), fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CrimsonPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Hospital Details
                    OutlinedTextField(
                        value = hospitalName,
                        onValueChange = { hospitalName = it },
                        label = { Text(LifeStrings.get("hospital_name", lang)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = hospitalAddress,
                        onValueChange = { hospitalAddress = it },
                        label = { Text(LifeStrings.get("hospital_address", lang)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Urgency Level Chips
                    Text(LifeStrings.get("urgency_level", lang), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        UrgencyLevel.entries.forEach { lvl ->
                            FilterChip(
                                selected = urgency == lvl,
                                onClick = { urgency = lvl },
                                label = { Text(lvl.name, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = when (lvl) {
                                        UrgencyLevel.CRITICAL -> EmergencyRed
                                        UrgencyLevel.URGENT -> AlertOrange
                                        UrgencyLevel.NORMAL -> MedicalGreen
                                    },
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Broadcast Radius selection
                    Text("${LifeStrings.get("broadcast_radius", lang)}: $broadcastRadiusKm KM", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(3, 5, 10, 15, 25, 50).forEach { r ->
                            FilterChip(
                                selected = broadcastRadiusKm == r,
                                onClick = { broadcastRadiusKm = r },
                                label = { Text("${r}km", fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CrimsonPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = contactPhone,
                        onValueChange = { contactPhone = it },
                        label = { Text(LifeStrings.get("contact_phone", lang)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text(LifeStrings.get("emergency_notes", lang)) },
                        placeholder = { Text("e.g. Surgery at 3 PM, bring blood donor card") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Launch broadcast CTA
                    Button(
                        onClick = {
                            if (patientName.isBlank()) {
                                Toast.makeText(context, "Please enter patient name", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            val req = BloodRequest(
                                patientName = patientName,
                                bloodGroup = selectedBloodGroup,
                                component = selectedComponent,
                                unitsNeeded = unitsNeeded,
                                hospitalName = hospitalName,
                                hospitalAddress = hospitalAddress,
                                urgency = urgency,
                                contactPhone = contactPhone,
                                notes = notes,
                                requesterId = currentUser.id,
                                requesterName = currentUser.fullName,
                                broadcastRadiusKm = broadcastRadiusKm
                            )
                            onBroadcastRequest(req)
                            patientName = ""
                            notes = ""
                            Toast.makeText(context, "Live Emergency Broadcast Dispatched!", Toast.LENGTH_LONG).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("submit_broadcast_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed)
                    ) {
                        Icon(Icons.Default.Emergency, contentDescription = "Emergency")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(LifeStrings.get("submit_broadcast", lang), fontWeight = FontWeight.Black, fontSize = 15.sp)
                    }
                }
            }
        }

        // Active Broadcasts Management Section
        item {
            Text(
                text = "Live Active Broadcasts (${activeRequests.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        }

        items(activeRequests) { req ->
            Card(
                modifier = Modifier.fillMaxWidth(),
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
                            BloodGroupBadge(bloodGroup = req.bloodGroup, size = 44.dp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(req.patientName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("${req.unitsNeeded} unit(s) • ${req.hospitalName}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when (req.status) {
                                RequestStatus.MATCHING -> EmergencyRed.copy(alpha = 0.15f)
                                RequestStatus.ACCEPTED -> AlertOrange.copy(alpha = 0.15f)
                                RequestStatus.FULFILLED -> MedicalGreen.copy(alpha = 0.15f)
                                else -> Color.LightGray.copy(alpha = 0.2f)
                            }
                        ) {
                            Text(
                                text = req.status.name,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (req.status) {
                                    RequestStatus.MATCHING -> EmergencyRed
                                    RequestStatus.ACCEPTED -> AlertOrange
                                    RequestStatus.FULFILLED -> MedicalGreen
                                    else -> Color.DarkGray
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Radius: ${req.broadcastRadiusKm} km • Responded Donors: ${req.respondedDonorIds.size}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (req.status != RequestStatus.FULFILLED) {
                            Button(
                                onClick = { onFulfillRequest(req.id) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MedicalGreen)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = "Fulfill", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(LifeStrings.get("mark_fulfilled", lang), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        if (req.status != RequestStatus.CANCELLED && req.status != RequestStatus.FULFILLED) {
                            OutlinedButton(
                                onClick = { onCancelRequest(req.id) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Cancel", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(LifeStrings.get("cancel_request", lang), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
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
