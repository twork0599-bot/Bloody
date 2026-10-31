package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.window.Dialog
import com.example.data.LifedropRepository
import com.example.localization.AppLanguage
import com.example.model.BloodGroup
import com.example.model.NotificationItem
import com.example.ui.components.LifedropBottomBar
import com.example.ui.components.LifedropTopBar
import com.example.ui.navigation.Screen
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.CompatibilityScreen
import com.example.ui.screens.DonationHistoryScreen
import com.example.ui.screens.EmergencyBroadcastScreen
import com.example.ui.screens.FindDonorsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.HospitalsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.CrimsonPrimary
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.LifedropTheme

class MainActivity : ComponentActivity() {

    private lateinit var repository: LifedropRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        repository = LifedropRepository(applicationContext)

        setContent {
            val isDarkTheme by repository.isDarkTheme.collectAsState()
            val currentLanguage by repository.currentLanguage.collectAsState()
            val currentUser by repository.currentUser.collectAsState()

            val activeRequests by repository.requests.collectAsState()
            val donors by repository.donors.collectAsState()
            val donations by repository.donations.collectAsState()
            val hospitals by repository.hospitals.collectAsState()
            val messages by repository.messages.collectAsState()
            val notifications by repository.notifications.collectAsState()
            val reports by repository.reports.collectAsState()

            var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
            var showNotificationsDialog by remember { mutableStateOf(false) }

            LifedropTheme(darkTheme = isDarkTheme) {
                // If not logged in, show AuthScreen
                if (currentUser == null) {
                    AuthScreen(
                        lang = currentLanguage,
                        onLoginSuccess = { name, identifier, bloodGroup, isRegister ->
                            repository.authenticateWithPhoneOrEmail(name, identifier, bloodGroup, isRegister)
                        }
                    )
                } else {
                    val user = currentUser!!

                    // Handle system back navigation to return to Home screen
                    BackHandler(enabled = currentScreen != Screen.Home) {
                        currentScreen = Screen.Home
                    }

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            LifedropTopBar(
                                lang = currentLanguage,
                                isDarkTheme = isDarkTheme,
                                unreadNotificationCount = notifications.count { !it.isRead },
                                onLanguageToggle = {
                                    val newLang = if (currentLanguage == AppLanguage.ENGLISH) AppLanguage.BANGLA else AppLanguage.ENGLISH
                                    repository.setLanguage(newLang)
                                },
                                onThemeToggle = {
                                    repository.toggleTheme()
                                },
                                onNotificationsClick = {
                                    showNotificationsDialog = true
                                }
                            )
                        },
                        bottomBar = {
                            LifedropBottomBar(
                                currentScreen = currentScreen,
                                lang = currentLanguage,
                                onNavigate = { screen -> currentScreen = screen }
                            )
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            when (currentScreen) {
                                Screen.Home -> HomeScreen(
                                    user = user,
                                    activeRequests = activeRequests,
                                    nearbyDonors = donors,
                                    lang = currentLanguage,
                                    onNavigate = { screen -> currentScreen = screen },
                                    onToggleAvailability = { repository.toggleDonorAvailability() },
                                    onRespondToRequest = { reqId ->
                                        repository.respondToRequest(reqId, user.id)
                                        Toast.makeText(this@MainActivity, "Thank you! Family notified of your response.", Toast.LENGTH_LONG).show()
                                        currentScreen = Screen.Messages
                                    }
                                )

                                Screen.FindDonors -> FindDonorsScreen(
                                    donors = donors,
                                    currentUser = user,
                                    lang = currentLanguage,
                                    onNavigate = { screen -> currentScreen = screen },
                                    onReportDonor = { donorId, reason, details ->
                                        repository.submitFraudReport(donorId, reason, details)
                                    }
                                )

                                Screen.EmergencyBroadcast -> EmergencyBroadcastScreen(
                                    currentUser = user,
                                    activeRequests = activeRequests,
                                    lang = currentLanguage,
                                    onBroadcastRequest = { req ->
                                        repository.createEmergencyRequest(req)
                                    },
                                    onFulfillRequest = { reqId ->
                                        repository.fulfillRequest(reqId)
                                        Toast.makeText(this@MainActivity, "Request marked as fulfilled. Blessings!", Toast.LENGTH_SHORT).show()
                                    },
                                    onCancelRequest = { reqId ->
                                        repository.cancelRequest(reqId)
                                        Toast.makeText(this@MainActivity, "Request cancelled.", Toast.LENGTH_SHORT).show()
                                    }
                                )

                                Screen.Messages -> ChatScreen(
                                    currentUser = user,
                                    messages = messages,
                                    lang = currentLanguage,
                                    onSendMessage = { receiverId, text ->
                                        repository.sendMessage(receiverId, text)
                                    }
                                )

                                Screen.Profile -> ProfileScreen(
                                    user = user,
                                    lang = currentLanguage,
                                    isDarkTheme = isDarkTheme,
                                    onLanguageChange = { lang -> repository.setLanguage(lang) },
                                    onThemeToggle = { repository.toggleTheme() },
                                    onUpdateUser = { updated -> repository.updateCurrentUser(updated) },
                                    onSignOut = { repository.signOut() },
                                    onDeleteAccount = { repository.deleteAccount() },
                                    onNavigate = { screen -> currentScreen = screen }
                                )

                                Screen.Compatibility -> CompatibilityScreen(
                                    lang = currentLanguage
                                )

                                Screen.DonationHistory -> DonationHistoryScreen(
                                    currentUser = user,
                                    donations = donations,
                                    badges = repository.getAchievements(user.verifiedDonationsCount),
                                    lang = currentLanguage
                                )

                                Screen.Hospitals -> HospitalsScreen(
                                    hospitals = hospitals,
                                    lang = currentLanguage
                                )

                                Screen.AdminHub -> AdminDashboardScreen(
                                    reports = reports,
                                    lang = currentLanguage,
                                    onVerifyDonation = { donorId, donorName, bg, hospital, patient ->
                                        repository.verifyDonation(donorId, donorName, bg, hospital, patient)
                                    },
                                    onBroadcastAnnouncement = { title, message ->
                                        // add system notification
                                        val notif = NotificationItem(
                                            id = "ann_" + System.currentTimeMillis(),
                                            userId = user.id,
                                            title = "ANNOUNCEMENT: $title",
                                            message = message,
                                            type = "ANNOUNCEMENT"
                                        )
                                    }
                                )

                                Screen.SafetyChecklist -> CompatibilityScreen(
                                    lang = currentLanguage
                                )
                            }
                        }

                        // Notifications Dialog
                        if (showNotificationsDialog) {
                            Dialog(onDismissRequest = { showNotificationsDialog = false }) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                                ) {
                                    Column(modifier = Modifier.padding(18.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.Notifications, contentDescription = "Alerts", tint = CrimsonPrimary)
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text("Emergency Alerts & Notices", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                            }
                                            IconButton(onClick = { showNotificationsDialog = false }) {
                                                Icon(Icons.Default.Close, contentDescription = "Close")
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        LazyColumn(
                                            verticalArrangement = Arrangement.spacedBy(10.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            items(notifications) { notif ->
                                                Surface(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    shape = RoundedCornerShape(12.dp),
                                                    color = MaterialTheme.colorScheme.surfaceVariant
                                                ) {
                                                    Column(modifier = Modifier.padding(12.dp)) {
                                                        Text(notif.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = CrimsonPrimary)
                                                        Spacer(modifier = Modifier.height(2.dp))
                                                        Text(notif.message, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
