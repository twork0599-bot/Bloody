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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.LifeStrings
import com.example.model.BloodGroup
import com.example.model.FraudReport
import com.example.ui.theme.AlertOrange
import com.example.ui.theme.CrimsonPrimary
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.MedicalGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    reports: List<FraudReport>,
    lang: AppLanguage,
    onVerifyDonation: (donorId: String, donorName: String, bloodGroup: BloodGroup, hospital: String, patient: String) -> Unit,
    onBroadcastAnnouncement: (title: String, message: String) -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }

    // Verification Form State
    var donorName by remember { mutableStateOf("Tanvir Rahman") }
    var recipientName by remember { mutableStateOf("Emergency ICU Patient") }
    var hospitalName by remember { mutableStateOf("Dhaka Medical College Hospital") }
    var selectedBloodGroup by remember { mutableStateOf(BloodGroup.O_POS) }
    var bloodMenuExpanded by remember { mutableStateOf(false) }

    // Announcement state
    var announcementTitle by remember { mutableStateOf("") }
    var announcementMessage by remember { mutableStateOf("") }

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
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Admin",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = LifeStrings.get("admin_hub_title", lang),
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Hospital partner verification & fraud protection queue",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Tabs
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = CrimsonPrimary
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text(LifeStrings.get("tab_verify_donations", lang), fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text(LifeStrings.get("tab_fraud_reports", lang), fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text(LifeStrings.get("tab_announcements", lang), fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
            }
        }

        // Tab 0: Hospital Staff Donation Verification Form
        if (selectedTab == 0) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = "Verify", tint = MedicalGreen)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Hospital Staff: Verify Donation & Issue Certificate", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = donorName,
                            onValueChange = { donorName = it },
                            label = { Text("Donor Full Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Blood group dropdown
                        ExposedDropdownMenuBox(
                            expanded = bloodMenuExpanded,
                            onExpandedChange = { bloodMenuExpanded = !bloodMenuExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedBloodGroup.label,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Donated Blood Group") },
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
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = recipientName,
                            onValueChange = { recipientName = it },
                            label = { Text("Recipient / Patient Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = hospitalName,
                            onValueChange = { hospitalName = it },
                            label = { Text("Hospital Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = {
                                onVerifyDonation("usr_current_hero", donorName, selectedBloodGroup, hospitalName, recipientName)
                                Toast.makeText(context, "Donation verified and certificate issued!", Toast.LENGTH_LONG).show()
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MedicalGreen)
                        ) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = "Verify")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(LifeStrings.get("verify_donation_btn", lang), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Tab 1: Fraud Reports Queue
        if (selectedTab == 1) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Trust & Safety Queue (${reports.size})", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Surface(shape = RoundedCornerShape(6.dp), color = AlertOrange.copy(alpha = 0.15f)) {
                        Text("Active Monitoring", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), color = AlertOrange, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (reports.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.Shield, contentDescription = "Safe", tint = MedicalGreen, modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No pending suspicious reports. Platform is safe!", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            items(reports) { rep ->
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
                            Text("Target ID: ${rep.targetId}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Risk Score: ${rep.riskScore}", color = EmergencyRed, fontWeight = FontWeight.Black, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Reason: ${rep.reason}", fontSize = 12.sp)
                        if (rep.evidenceDescription.isNotBlank()) {
                            Text("Details: ${rep.evidenceDescription}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    Toast.makeText(context, "Report marked resolved", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MedicalGreen)
                            ) {
                                Text("Resolve", fontSize = 11.sp)
                            }
                            OutlinedButton(
                                onClick = {
                                    Toast.makeText(context, "Report dismissed", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Dismiss", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        // Tab 2: System Announcements
        if (selectedTab == 2) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Campaign, contentDescription = "Announce", tint = CrimsonPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Broadcast Platform Announcement", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        OutlinedTextField(
                            value = announcementTitle,
                            onValueChange = { announcementTitle = it },
                            label = { Text("Announcement Headline") },
                            placeholder = { Text("e.g. Critical O- Blood Shortage at Red Crescent") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = announcementMessage,
                            onValueChange = { announcementMessage = it },
                            label = { Text("Message Body") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 4,
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                if (announcementTitle.isNotBlank()) {
                                    onBroadcastAnnouncement(announcementTitle, announcementMessage)
                                    announcementTitle = ""
                                    announcementMessage = ""
                                    Toast.makeText(context, "Announcement broadcasted to all users!", Toast.LENGTH_LONG).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary)
                        ) {
                            Text(LifeStrings.get("broadcast_announcement", lang), fontWeight = FontWeight.Bold)
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
