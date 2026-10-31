package com.example.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object FindDonors : Screen("find_donors")
    data object EmergencyBroadcast : Screen("emergency_broadcast")
    data object Messages : Screen("messages")
    data object Profile : Screen("profile")
    data object Compatibility : Screen("compatibility")
    data object DonationHistory : Screen("donation_history")
    data object Hospitals : Screen("hospitals")
    data object AdminHub : Screen("admin_hub")
    data object SafetyChecklist : Screen("safety_checklist")
}
