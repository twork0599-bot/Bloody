package com.example.model

enum class BloodGroup(val label: String) {
    A_POS("A+"),
    A_NEG("A-"),
    B_POS("B+"),
    B_NEG("B-"),
    AB_POS("AB+"),
    AB_NEG("AB-"),
    O_POS("O+"),
    O_NEG("O-");

    companion object {
        fun fromString(str: String): BloodGroup {
            return entries.find { it.label.equals(str, ignoreCase = true) || it.name.equals(str, ignoreCase = true) } ?: O_POS
        }
    }
}

enum class BloodComponent(val id: String) {
    WHOLE_BLOOD("Whole Blood"),
    RBC("Red Blood Cells (RBC)"),
    PLASMA("Plasma"),
    PLATELETS("Platelets")
}

enum class UrgencyLevel {
    CRITICAL,
    URGENT,
    NORMAL
}

enum class RequestStatus {
    PENDING,
    MATCHING,
    ACCEPTED,
    FULFILLED,
    CANCELLED
}

enum class UserRole {
    DONOR,
    HOSPITAL,
    ADMIN
}

data class LifedropUser(
    val id: String = "",
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val bloodGroup: BloodGroup = BloodGroup.O_POS,
    val district: String = "Dhaka",
    val upazila: String = "Dhanmondi",
    val latitude: Double = 23.7461,
    val longitude: Double = 90.3742,
    val isAvailable: Boolean = true,
    val isVerified: Boolean = true,
    val verifiedDonationsCount: Int = 3,
    val lastDonationDate: String = "2026-07-15",
    val nextEligibleDate: String = "2026-10-15",
    val bio: String = "Regular blood donor committed to saving lives in emergencies.",
    val role: UserRole = UserRole.DONOR,
    val hidePhone: Boolean = false,
    val approximateLocationOnly: Boolean = true,
    val biometricsEnabled: Boolean = true,
    val preferredLanguage: String = "en"
)

data class BloodRequest(
    val id: String = "",
    val patientName: String = "",
    val bloodGroup: BloodGroup = BloodGroup.O_POS,
    val component: BloodComponent = BloodComponent.WHOLE_BLOOD,
    val unitsNeeded: Int = 1,
    val hospitalName: String = "",
    val hospitalAddress: String = "",
    val district: String = "Dhaka",
    val upazila: String = "Dhanmondi",
    val latitude: Double = 23.7461,
    val longitude: Double = 90.3742,
    val urgency: UrgencyLevel = UrgencyLevel.URGENT,
    val contactPhone: String = "",
    val notes: String = "",
    val status: RequestStatus = RequestStatus.MATCHING,
    val requesterId: String = "",
    val requesterName: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val broadcastRadiusKm: Int = 10,
    val respondedDonorIds: List<String> = emptyList(),
    val acceptedDonorId: String? = null,
    val fraudRiskScore: Int = 5 // low risk
)

data class DonationRecord(
    val id: String = "",
    val donorId: String = "",
    val donorName: String = "",
    val bloodGroup: BloodGroup = BloodGroup.O_POS,
    val recipientName: String = "",
    val hospitalName: String = "",
    val date: String = "",
    val units: Int = 1,
    val isVerified: Boolean = true,
    val certificateId: String = "",
    val verificationNotes: String = "Verified by Chief Blood Bank Officer"
)

data class HospitalPartner(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val district: String = "Dhaka",
    val phone: String = "+880 1711-000000",
    val isVerified: Boolean = true,
    val stockUnits: Map<String, Int> = mapOf(
        "A+" to 14, "A-" to 4, "B+" to 18, "B-" to 3,
        "AB+" to 8, "AB-" to 2, "O+" to 25, "O-" to 5
    ),
    val latitude: Double = 23.7461,
    val longitude: Double = 90.3742
)

data class ChatMessage(
    val id: String = "",
    val conversationId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val receiverId: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = true
)

data class NotificationItem(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val message: String = "",
    val type: String = "EMERGENCY",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val relatedRequestId: String? = null
)

data class FraudReport(
    val id: String = "",
    val targetType: String = "REQUEST",
    val targetId: String = "",
    val reporterId: String = "",
    val reason: String = "",
    val evidenceDescription: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val riskScore: Int = 20,
    val status: String = "PENDING"
)

data class AchievementBadge(
    val id: String,
    val nameEn: String,
    val nameBn: String,
    val descEn: String,
    val descBn: String,
    val requiredDonations: Int,
    val unlocked: Boolean
)
