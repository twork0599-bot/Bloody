package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.localization.AppLanguage
import com.example.localization.LifeStrings
import com.example.model.BloodGroup
import com.example.model.DonationRecord
import com.example.ui.navigation.Screen
import com.example.ui.theme.BadgeGold
import com.example.ui.theme.CrimsonDark
import com.example.ui.theme.CrimsonLight
import com.example.ui.theme.CrimsonPrimary
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.MedicalGreen

@Composable
fun BloodGroupBadge(
    bloodGroup: BloodGroup,
    modifier: Modifier = Modifier,
    size: Dp = 46.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(CrimsonPrimary, CrimsonDark)
                )
            )
            .border(2.dp, Color.White.copy(alpha = 0.8f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = bloodGroup.label,
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = if (size < 40.dp) 12.sp else 16.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LifedropTopBar(
    lang: AppLanguage,
    isDarkTheme: Boolean,
    unreadNotificationCount: Int,
    onLanguageToggle: () -> Unit,
    onThemeToggle: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(CrimsonPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bloodtype,
                        contentDescription = "Logo",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = LifeStrings.get("app_name", lang),
                        fontWeight = FontWeight.Black,
                        fontSize = 19.sp,
                        color = CrimsonPrimary
                    )
                    Text(
                        text = LifeStrings.get("tagline", lang),
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        actions = {
            // Language selector button
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onLanguageToggle() }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Language",
                        modifier = Modifier.size(16.dp),
                        tint = CrimsonPrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (lang == AppLanguage.ENGLISH) "EN" else "বাংলা",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Theme toggle
            IconButton(onClick = onThemeToggle) {
                Icon(
                    imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = "Theme Toggle",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            // Notifications with badge
            IconButton(onClick = onNotificationsClick) {
                BadgedBox(
                    badge = {
                        if (unreadNotificationCount > 0) {
                            Badge(containerColor = EmergencyRed) {
                                Text("$unreadNotificationCount", color = Color.White)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    )
}

@Composable
fun LifedropBottomBar(
    currentScreen: Screen,
    lang: AppLanguage,
    onNavigate: (Screen) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentScreen == Screen.Home,
            onClick = { onNavigate(Screen.Home) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text(LifeStrings.get("nav_home", lang), fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CrimsonPrimary,
                selectedTextColor = CrimsonPrimary,
                indicatorColor = CrimsonPrimary.copy(alpha = 0.12f)
            ),
            modifier = Modifier.testTag("nav_home")
        )
        NavigationBarItem(
            selected = currentScreen == Screen.FindDonors,
            onClick = { onNavigate(Screen.FindDonors) },
            icon = { Icon(Icons.Default.Search, contentDescription = "Find Donors") },
            label = { Text(LifeStrings.get("nav_find_donors", lang), fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CrimsonPrimary,
                selectedTextColor = CrimsonPrimary,
                indicatorColor = CrimsonPrimary.copy(alpha = 0.12f)
            ),
            modifier = Modifier.testTag("nav_find_donors")
        )
        NavigationBarItem(
            selected = currentScreen == Screen.EmergencyBroadcast,
            onClick = { onNavigate(Screen.EmergencyBroadcast) },
            icon = {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(EmergencyRed),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Emergency, contentDescription = "Emergency", tint = Color.White, modifier = Modifier.size(20.dp))
                }
            },
            label = { Text(LifeStrings.get("nav_emergency", lang), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmergencyRed) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = EmergencyRed,
                selectedTextColor = EmergencyRed,
                indicatorColor = EmergencyRed.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_emergency")
        )
        NavigationBarItem(
            selected = currentScreen == Screen.Messages,
            onClick = { onNavigate(Screen.Messages) },
            icon = { Icon(Icons.Default.Chat, contentDescription = "Messages") },
            label = { Text(LifeStrings.get("nav_messages", lang), fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CrimsonPrimary,
                selectedTextColor = CrimsonPrimary,
                indicatorColor = CrimsonPrimary.copy(alpha = 0.12f)
            ),
            modifier = Modifier.testTag("nav_messages")
        )
        NavigationBarItem(
            selected = currentScreen == Screen.Profile,
            onClick = { onNavigate(Screen.Profile) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text(LifeStrings.get("nav_profile", lang), fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CrimsonPrimary,
                selectedTextColor = CrimsonPrimary,
                indicatorColor = CrimsonPrimary.copy(alpha = 0.12f)
            ),
            modifier = Modifier.testTag("nav_profile")
        )
    }
}

@Composable
fun CertificateDialog(
    record: DonationRecord,
    lang: AppLanguage,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, BadgeGold, RoundedCornerShape(20.dp))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = "Verified Stamp",
                    tint = BadgeGold,
                    modifier = Modifier.size(52.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = LifeStrings.get("certificate_title", lang),
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = CrimsonPrimary
                )
                Text(
                    text = LifeStrings.get("certificate_subtitle", lang),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = LifeStrings.get("certificate_presented_to", lang),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = record.donorName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Blood Group", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(record.bloodGroup.label, fontWeight = FontWeight.Bold, color = CrimsonPrimary)
                    }
                    Column {
                        Text("Hospital", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(record.hospitalName.take(18) + "..", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Column {
                        Text("Date", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(record.date, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "ID: ${record.certificateId}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BadgeGold
                )
                Text(
                    text = record.verificationNotes,
                    fontSize = 11.sp,
                    color = MedicalGreen,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onDismiss() }
                        .background(CrimsonPrimary)
                        .padding(horizontal = 24.dp, vertical = 10.dp)
                ) {
                    Text(LifeStrings.get("continue_btn", lang), color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun RadarMapView(
    radiusKm: Int,
    donorCount: Int,
    hospitalCount: Int,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "RadarSweep")
    val sweepAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Sweep"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(230.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0F172A)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().height(230.dp)) {
            val center = Offset(size.width / 2, size.height / 2)
            val maxR = size.height * 0.42f

            // Concentric rings
            drawCircle(color = Color(0xFF1E293B), radius = maxR, center = center, style = Stroke(width = 2f))
            drawCircle(color = Color(0xFF1E293B), radius = maxR * 0.7f, center = center, style = Stroke(width = 1.5f))
            drawCircle(color = Color(0xFF1E293B), radius = maxR * 0.4f, center = center, style = Stroke(width = 1.5f))

            // Cross hairs
            drawLine(Color(0xFF1E293B), Offset(center.x, center.y - maxR), Offset(center.x, center.y + maxR), strokeWidth = 1f)
            drawLine(Color(0xFF1E293B), Offset(center.x - maxR, center.y), Offset(center.x + maxR, center.y), strokeWidth = 1f)

            // Radar sweep line
            val rad = Math.toRadians(sweepAngle.toDouble())
            val sweepTarget = Offset(
                x = center.x + (maxR * Math.cos(rad)).toFloat(),
                y = center.y + (maxR * Math.sin(rad)).toFloat()
            )
            drawLine(
                brush = Brush.linearGradient(listOf(Color(0xFFD90429).copy(alpha = 0.8f), Color.Transparent)),
                start = center,
                end = sweepTarget,
                strokeWidth = 3f
            )

            // Current user center dot
            drawCircle(color = Color(0xFFD90429), radius = 8f, center = center)
            drawCircle(color = Color.White, radius = 4f, center = center)

            // Mock donor blips
            drawCircle(color = Color(0xFF10B981), radius = 6f, center = Offset(center.x + maxR * 0.35f, center.y - maxR * 0.25f))
            drawCircle(color = Color(0xFF10B981), radius = 6f, center = Offset(center.x - maxR * 0.5f, center.y + maxR * 0.3f))
            drawCircle(color = Color(0xFF10B981), radius = 6f, center = Offset(center.x + maxR * 0.6f, center.y + maxR * 0.5f))

            // Hospital blip
            drawCircle(color = Color(0xFF3B82F6), radius = 7f, center = Offset(center.x - maxR * 0.25f, center.y - maxR * 0.55f))
        }

        // Overlay legend
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
                .background(Color(0xCC000000), RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF10B981)))
                Spacer(modifier = Modifier.width(4.dp))
                Text("$donorCount Donors", color = Color.White, fontSize = 11.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF3B82F6)))
                Spacer(modifier = Modifier.width(4.dp))
                Text("$hospitalCount Hospitals", color = Color.White, fontSize = 11.sp)
            }
            Text("Range: ${radiusKm}km", color = Color.LightGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}
