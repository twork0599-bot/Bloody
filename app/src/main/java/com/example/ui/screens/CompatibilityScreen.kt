package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.DonorMatchingEngine
import com.example.localization.AppLanguage
import com.example.localization.LifeStrings
import com.example.model.BloodComponent
import com.example.model.BloodGroup
import com.example.ui.components.BloodGroupBadge
import com.example.ui.theme.AlertOrange
import com.example.ui.theme.CrimsonDark
import com.example.ui.theme.CrimsonPrimary
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.MedicalGreen

@Composable
fun CompatibilityScreen(
    lang: AppLanguage
) {
    var selectedGroup by remember { mutableStateOf(BloodGroup.O_POS) }
    var selectedComponent by remember { mutableStateOf(BloodComponent.RBC) }

    val canDonateTo = BloodGroup.entries.filter { patient ->
        DonorMatchingEngine.isComponentCompatible(selectedGroup, patient, selectedComponent)
    }

    val canReceiveFrom = BloodGroup.entries.filter { donor ->
        DonorMatchingEngine.isComponentCompatible(donor, selectedGroup, selectedComponent)
    }

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
                colors = CardDefaults.cardColors(containerColor = CrimsonPrimary)
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bloodtype,
                            contentDescription = "Blood Compatibility",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = LifeStrings.get("compat_title", lang),
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Interactive ABO/Rh and Component Compatibility Matrix",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Blood Group Selector
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Select Blood Group to Check",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(BloodGroup.entries) { bg ->
                            FilterChip(
                                selected = selectedGroup == bg,
                                onClick = { selectedGroup = bg },
                                label = { Text(bg.label, fontWeight = FontWeight.Black, fontSize = 14.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CrimsonPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Select Component",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(BloodComponent.RBC, BloodComponent.PLASMA, BloodComponent.PLATELETS).forEach { comp ->
                            FilterChip(
                                selected = selectedComponent == comp,
                                onClick = { selectedComponent = comp },
                                label = { Text(comp.id, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF2563EB),
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }

        // Results Section: Can Donate To & Can Receive From
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Can Donate To
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(MedicalGreen))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = LifeStrings.get("can_donate_to", lang),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MedicalGreen
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        canDonateTo.chunked(2).forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                row.forEach { bg ->
                                    Surface(
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp),
                                        color = MedicalGreen.copy(alpha = 0.12f)
                                    ) {
                                        Text(
                                            text = bg.label,
                                            modifier = Modifier.padding(vertical = 6.dp),
                                            fontWeight = FontWeight.Black,
                                            fontSize = 14.sp,
                                            color = MedicalGreen,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Can Receive From
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF2563EB)))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = LifeStrings.get("can_receive_from", lang),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFF2563EB)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        canReceiveFrom.chunked(2).forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                row.forEach { bg ->
                                    Surface(
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFF2563EB).copy(alpha = 0.12f)
                                    ) {
                                        Text(
                                            text = bg.label,
                                            modifier = Modifier.padding(vertical = 6.dp),
                                            fontWeight = FontWeight.Black,
                                            fontSize = 14.sp,
                                            color = Color(0xFF2563EB),
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Universal Facts Callout
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("💡 Key Medical Rules", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("• " + LifeStrings.get("universal_rbc_donor", lang), fontSize = 12.sp)
                    Text("• " + LifeStrings.get("universal_rbc_recipient", lang), fontSize = 12.sp)
                    Text("• " + LifeStrings.get("universal_plasma_donor", lang), fontSize = 12.sp)
                    Text("• " + LifeStrings.get("universal_plasma_recipient", lang), fontSize = 12.sp)
                }
            }
        }

        // Mandatory Medical Supervision Notice
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = EmergencyRed.copy(alpha = 0.1f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Medical Alert",
                        tint = EmergencyRed,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = LifeStrings.get("transfusion_disclaimer", lang),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
