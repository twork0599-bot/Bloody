package com.example.data

import android.content.Context
import com.example.engine.DonorMatchingEngine
import com.example.engine.FraudDetectionEngine
import com.example.engine.MatchResult
import com.example.localization.AppLanguage
import com.example.model.AchievementBadge
import com.example.model.BloodComponent
import com.example.model.BloodGroup
import com.example.model.BloodRequest
import com.example.model.ChatMessage
import com.example.model.DonationRecord
import com.example.model.FraudReport
import com.example.model.HospitalPartner
import com.example.model.LifedropUser
import com.example.model.NotificationItem
import com.example.model.RequestStatus
import com.example.model.UrgencyLevel
import com.example.model.UserRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class LifedropRepository(private val context: Context) {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore? by lazy {
        try {
            val resId = context.resources.getIdentifier("firestore_database_id", "string", context.packageName)
            if (resId != 0) {
                FirebaseFirestore.getInstance(context.getString(resId))
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    // Current User Session
    private val _currentUser = MutableStateFlow<LifedropUser?>(createDefaultUser())
    val currentUser: StateFlow<LifedropUser?> = _currentUser.asStateFlow()

    // App Preferences
    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    // Data Collections
    private val _donors = MutableStateFlow<List<LifedropUser>>(emptyList())
    val donors: StateFlow<List<LifedropUser>> = _donors.asStateFlow()

    private val _requests = MutableStateFlow<List<BloodRequest>>(emptyList())
    val requests: StateFlow<List<BloodRequest>> = _requests.asStateFlow()

    private val _donations = MutableStateFlow<List<DonationRecord>>(emptyList())
    val donations: StateFlow<List<DonationRecord>> = _donations.asStateFlow()

    private val _hospitals = MutableStateFlow<List<HospitalPartner>>(emptyList())
    val hospitals: StateFlow<List<HospitalPartner>> = _hospitals.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _notifications = MutableStateFlow<List<NotificationItem>>(emptyList())
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _reports = MutableStateFlow<List<FraudReport>>(emptyList())
    val reports: StateFlow<List<FraudReport>> = _reports.asStateFlow()

    init {
        initializeSampleData()
    }

    private fun createDefaultUser(): LifedropUser {
        return LifedropUser(
            id = "usr_current_hero",
            fullName = "Tanvir Rahman",
            email = "tanvir.lifedrop@example.com",
            phone = "+880 1711-234567",
            bloodGroup = BloodGroup.O_POS,
            district = "Dhaka",
            upazila = "Dhanmondi",
            latitude = 23.7461,
            longitude = 90.3742,
            isAvailable = true,
            isVerified = true,
            verifiedDonationsCount = 4,
            lastDonationDate = "2026-06-20",
            nextEligibleDate = "2026-09-20",
            bio = "Lifedrop volunteer & emergency blood donor. Happy to help patients in need.",
            role = UserRole.DONOR,
            hidePhone = false,
            approximateLocationOnly = true,
            biometricsEnabled = true
        )
    }

    private fun initializeSampleData() {
        _donors.value = listOf(
            LifedropUser(
                id = "d1",
                fullName = "Dr. Farhana Ahmed",
                email = "farhana@example.com",
                phone = "+880 1819-112233",
                bloodGroup = BloodGroup.O_NEG, // Universal RBC donor!
                district = "Dhaka",
                upazila = "Shahbagh",
                latitude = 23.7380,
                longitude = 90.3957,
                isAvailable = true,
                isVerified = true,
                verifiedDonationsCount = 8,
                lastDonationDate = "2026-04-10",
                nextEligibleDate = "2026-07-10",
                bio = "Physician at BSMMU. Regular O- universal donor."
            ),
            LifedropUser(
                id = "d2",
                fullName = "Kazi Mahfuz",
                email = "mahfuz@example.com",
                phone = "+880 1912-334455",
                bloodGroup = BloodGroup.A_POS,
                district = "Dhaka",
                upazila = "Mohammadpur",
                latitude = 23.7542,
                longitude = 90.3602,
                isAvailable = true,
                isVerified = true,
                verifiedDonationsCount = 5,
                lastDonationDate = "2026-05-18",
                nextEligibleDate = "2026-08-18",
                bio = "Software engineer, ready to donate whole blood or platelets."
            ),
            LifedropUser(
                id = "d3",
                fullName = "Sabrina Yasmin",
                email = "sabrina@example.com",
                phone = "+880 1713-778899",
                bloodGroup = BloodGroup.B_POS,
                district = "Dhaka",
                upazila = "Gulshan",
                latitude = 23.7925,
                longitude = 90.4078,
                isAvailable = true,
                isVerified = true,
                verifiedDonationsCount = 3,
                lastDonationDate = "2026-03-22",
                nextEligibleDate = "2026-06-22",
                bio = "Teacher and community volunteer."
            ),
            LifedropUser(
                id = "d4",
                fullName = "Nafis Imtiaz",
                email = "nafis@example.com",
                phone = "+880 1675-998877",
                bloodGroup = BloodGroup.AB_POS,
                district = "Dhaka",
                upazila = "Uttara",
                latitude = 23.8759,
                longitude = 90.3795,
                isAvailable = true,
                isVerified = true,
                verifiedDonationsCount = 6,
                lastDonationDate = "2026-02-14",
                nextEligibleDate = "2026-05-14",
                bio = "Universal plasma donor."
            ),
            LifedropUser(
                id = "d5",
                fullName = "Rashedul Karim",
                email = "rashed@example.com",
                phone = "+880 1552-445566",
                bloodGroup = BloodGroup.O_POS,
                district = "Dhaka",
                upazila = "Mirpur",
                latitude = 23.8071,
                longitude = 90.3686,
                isAvailable = true,
                isVerified = true,
                verifiedDonationsCount = 11,
                lastDonationDate = "2026-07-01",
                nextEligibleDate = "2026-10-01",
                bio = "Lifedrop Community Hero badge holder with 11 donations."
            )
        )

        _requests.value = listOf(
            BloodRequest(
                id = "req_101",
                patientName = "Rahima Khatun",
                bloodGroup = BloodGroup.O_POS,
                component = BloodComponent.RBC,
                unitsNeeded = 2,
                hospitalName = "Dhaka Medical College Hospital",
                hospitalAddress = "Secretariat Rd, Shahbagh, Dhaka",
                district = "Dhaka",
                upazila = "Shahbagh",
                latitude = 23.7258,
                longitude = 90.3976,
                urgency = UrgencyLevel.CRITICAL,
                contactPhone = "+880 1711-889900",
                notes = "Emergency surgery scheduled for 4 PM. Attendant waiting at blood bank 2nd floor.",
                status = RequestStatus.MATCHING,
                requesterId = "usr_patient_fam",
                requesterName = "Kamal Hossain (Brother)",
                createdAt = System.currentTimeMillis() - 3600 * 1000 * 2,
                broadcastRadiusKm = 15,
                respondedDonorIds = listOf("d5")
            ),
            BloodRequest(
                id = "req_102",
                patientName = "Sadman Sakib",
                bloodGroup = BloodGroup.A_POS,
                component = BloodComponent.PLATELETS,
                unitsNeeded = 1,
                hospitalName = "Square Hospital",
                hospitalAddress = "18/F Bir Uttam Qazi Nuruzzaman Sarak, Panthapath, Dhaka",
                district = "Dhaka",
                upazila = "Panthapath",
                latitude = 23.7533,
                longitude = 90.3842,
                urgency = UrgencyLevel.URGENT,
                contactPhone = "+880 1819-445566",
                notes = "Dengue patient with low platelet count. Immediate apheresis donor needed.",
                status = RequestStatus.MATCHING,
                requesterId = "usr_fam_2",
                requesterName = "Nazrul Sakib",
                createdAt = System.currentTimeMillis() - 3600 * 1000 * 5,
                broadcastRadiusKm = 10
            )
        )

        _hospitals.value = listOf(
            HospitalPartner(
                id = "hosp_1",
                name = "Dhaka Medical College & Hospital",
                address = "Secretariat Rd, Shahbagh, Dhaka",
                district = "Dhaka",
                phone = "+880 2-55165088",
                isVerified = true,
                stockUnits = mapOf("A+" to 18, "A-" to 5, "B+" to 22, "B-" to 4, "AB+" to 9, "AB-" to 2, "O+" to 30, "O-" to 6),
                latitude = 23.7258,
                longitude = 90.3976
            ),
            HospitalPartner(
                id = "hosp_2",
                name = "Square Hospitals Limited",
                address = "Panthapath, Dhaka",
                district = "Dhaka",
                phone = "+880 2-8159457",
                isVerified = true,
                stockUnits = mapOf("A+" to 12, "A-" to 3, "B+" to 15, "B-" to 2, "AB+" to 6, "AB-" to 1, "O+" to 19, "O-" to 4),
                latitude = 23.7533,
                longitude = 90.3842
            ),
            HospitalPartner(
                id = "hosp_3",
                name = "Evercare Hospital Dhaka",
                address = "Plot 81, Block E, Bashundhara R/A, Dhaka",
                district = "Dhaka",
                phone = "+880 2-8431661",
                isVerified = true,
                stockUnits = mapOf("A+" to 15, "A-" to 4, "B+" to 20, "B-" to 3, "AB+" to 8, "AB-" to 2, "O+" to 24, "O-" to 5),
                latitude = 23.8118,
                longitude = 90.4326
            ),
            HospitalPartner(
                id = "hosp_4",
                name = "Bangladesh Red Crescent Society Blood Center",
                address = "7/5 Aurangzeb Road, Mohammadpur, Dhaka",
                district = "Dhaka",
                phone = "+880 2-9116563",
                isVerified = true,
                stockUnits = mapOf("A+" to 35, "A-" to 10, "B+" to 40, "B-" to 8, "AB+" to 15, "AB-" to 4, "O+" to 50, "O-" to 12),
                latitude = 23.7610,
                longitude = 90.3627
            )
        )

        _donations.value = listOf(
            DonationRecord(
                id = "don_1",
                donorId = "usr_current_hero",
                donorName = "Tanvir Rahman",
                bloodGroup = BloodGroup.O_POS,
                recipientName = "Hafizur Rahman",
                hospitalName = "Dhaka Medical College Hospital",
                date = "2026-06-20",
                units = 1,
                isVerified = true,
                certificateId = "LIFEDROP-CERT-2026-88219",
                verificationNotes = "Whole blood donation verified by Dr. Masud (DMCH Blood Bank)"
            ),
            DonationRecord(
                id = "don_2",
                donorId = "usr_current_hero",
                donorName = "Tanvir Rahman",
                bloodGroup = BloodGroup.O_POS,
                recipientName = "Baby of Sumaiya",
                hospitalName = "Square Hospital",
                date = "2026-02-10",
                units = 1,
                isVerified = true,
                certificateId = "LIFEDROP-CERT-2026-31940",
                verificationNotes = "Emergency neonatology transfusion verified by Chief Medical Officer"
            ),
            DonationRecord(
                id = "don_3",
                donorId = "usr_current_hero",
                donorName = "Tanvir Rahman",
                bloodGroup = BloodGroup.O_POS,
                recipientName = "Md. Selim",
                hospitalName = "Bangladesh Red Crescent Society",
                date = "2025-10-15",
                units = 1,
                isVerified = true,
                certificateId = "LIFEDROP-CERT-2025-10943",
                verificationNotes = "Voluntary blood drive donation"
            )
        )

        _messages.value = listOf(
            ChatMessage(
                id = "m1",
                conversationId = "conv_1",
                senderId = "usr_patient_fam",
                senderName = "Kamal Hossain",
                receiverId = "usr_current_hero",
                text = "Hello Tanvir Bhai, we received your match alert for Rahima Khatun. Are you available to come to DMCH?",
                timestamp = System.currentTimeMillis() - 3600 * 1000,
                isRead = true
            ),
            ChatMessage(
                id = "m2",
                conversationId = "conv_1",
                senderId = "usr_current_hero",
                senderName = "Tanvir Rahman",
                receiverId = "usr_patient_fam",
                text = "Yes Kamal Bhai, I am leaving now. I will reach the blood bank within 35 minutes.",
                timestamp = System.currentTimeMillis() - 1800 * 1000,
                isRead = true
            )
        )

        _notifications.value = listOf(
            NotificationItem(
                id = "notif_1",
                userId = "usr_current_hero",
                title = "Critical Blood Match Alert!",
                message = "Patient Rahima Khatun needs O+ RBC at Dhaka Medical College (Shahbagh). You are 2.8 km away.",
                type = "EMERGENCY",
                timestamp = System.currentTimeMillis() - 7200 * 1000,
                isRead = false,
                relatedRequestId = "req_101"
            ),
            NotificationItem(
                id = "notif_2",
                userId = "usr_current_hero",
                title = "Eligibility Reminder",
                message = "Congratulations! You are now eligible to donate blood again and save lives.",
                type = "REMINDER",
                timestamp = System.currentTimeMillis() - 24 * 3600 * 1000,
                isRead = true
            )
        )
    }

    fun setLanguage(lang: AppLanguage) {
        _currentLanguage.value = lang
    }

    fun toggleTheme() {
        _isDarkTheme.update { !it }
    }

    fun updateCurrentUser(user: LifedropUser) {
        _currentUser.value = user
    }

    fun toggleDonorAvailability() {
        _currentUser.update { current ->
            current?.copy(isAvailable = !current.isAvailable)
        }
    }

    fun authenticateWithPhoneOrEmail(
        name: String,
        identifier: String,
        bloodGroup: BloodGroup,
        isRegister: Boolean
    ): Boolean {
        val user = LifedropUser(
            id = "usr_" + UUID.randomUUID().toString().take(8),
            fullName = name.ifBlank { "Lifesaver Hero" },
            email = if (identifier.contains("@")) identifier else "donor@lifedrop.org",
            phone = if (identifier.startsWith("+") || identifier.all { it.isDigit() || it == '-' || it == ' ' }) identifier else "+880 1700-000000",
            bloodGroup = bloodGroup,
            isAvailable = true,
            isVerified = true,
            verifiedDonationsCount = 1
        )
        _currentUser.value = user
        return true
    }

    fun signOut() {
        _currentUser.value = null
    }

    fun deleteAccount() {
        _currentUser.value = null
    }

    fun createEmergencyRequest(request: BloodRequest): BloodRequest {
        val fraudEval = FraudDetectionEngine.evaluateRequest(request, _requests.value)
        val finalRequest = request.copy(
            id = "req_" + UUID.randomUUID().toString().take(8),
            createdAt = System.currentTimeMillis(),
            fraudRiskScore = fraudEval.riskScore
        )
        _requests.update { listOf(finalRequest) + it }

        // Trigger notification
        val notif = NotificationItem(
            id = "notif_" + UUID.randomUUID().toString().take(6),
            userId = _currentUser.value?.id ?: "",
            title = "Emergency Broadcast Dispatched",
            message = "Broadcast sent to compatible donors within ${finalRequest.broadcastRadiusKm} km radius.",
            type = "EMERGENCY",
            relatedRequestId = finalRequest.id
        )
        _notifications.update { listOf(notif) + it }

        return finalRequest
    }

    fun fulfillRequest(requestId: String) {
        _requests.update { list ->
            list.map { req ->
                if (req.id == requestId) req.copy(status = RequestStatus.FULFILLED) else req
            }
        }
    }

    fun cancelRequest(requestId: String) {
        _requests.update { list ->
            list.map { req ->
                if (req.id == requestId) req.copy(status = RequestStatus.CANCELLED) else req
            }
        }
    }

    fun respondToRequest(requestId: String, donorId: String) {
        _requests.update { list ->
            list.map { req ->
                if (req.id == requestId) {
                    val updatedList = (req.respondedDonorIds + donorId).distinct()
                    req.copy(respondedDonorIds = updatedList, status = RequestStatus.ACCEPTED)
                } else req
            }
        }
    }

    fun sendMessage(receiverId: String, text: String) {
        val current = _currentUser.value ?: return
        val msg = ChatMessage(
            id = "msg_" + UUID.randomUUID().toString().take(6),
            conversationId = "conv_1",
            senderId = current.id,
            senderName = current.fullName,
            receiverId = receiverId,
            text = text,
            timestamp = System.currentTimeMillis(),
            isRead = true
        )
        _messages.update { it + msg }
    }

    fun submitFraudReport(targetId: String, reason: String, details: String) {
        val current = _currentUser.value
        val report = FraudReport(
            id = "rep_" + UUID.randomUUID().toString().take(6),
            targetId = targetId,
            reporterId = current?.id ?: "anon",
            reason = reason,
            evidenceDescription = details,
            timestamp = System.currentTimeMillis()
        )
        _reports.update { listOf(report) + it }
    }

    fun verifyDonation(donorId: String, donorName: String, bloodGroup: BloodGroup, hospitalName: String, recipientName: String) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = dateFormat.format(Date())
        val certId = "LIFEDROP-CERT-" + SimpleDateFormat("yyyy", Locale.getDefault()).format(Date()) + "-" + (10000..99999).random()

        val record = DonationRecord(
            id = "don_" + UUID.randomUUID().toString().take(6),
            donorId = donorId,
            donorName = donorName,
            bloodGroup = bloodGroup,
            recipientName = recipientName.ifBlank { "Emergency Patient" },
            hospitalName = hospitalName,
            date = today,
            units = 1,
            isVerified = true,
            certificateId = certId,
            verificationNotes = "Officially verified by partner hospital staff"
        )
        _donations.update { listOf(record) + it }

        // Also update donor's total count if it's current user
        if (_currentUser.value?.id == donorId) {
            _currentUser.update { user ->
                user?.copy(
                    verifiedDonationsCount = user.verifiedDonationsCount + 1,
                    lastDonationDate = today
                )
            }
        }
    }

    fun getAchievements(donationCount: Int): List<AchievementBadge> {
        return listOf(
            AchievementBadge(
                id = "b1",
                nameEn = "First Drop",
                nameBn = "প্রথম রক্তদান",
                descEn = "Completed 1 verified blood donation",
                descBn = "১টি সফল ও যাচাইকৃত রক্তদান সম্পন্ন",
                requiredDonations = 1,
                unlocked = donationCount >= 1
            ),
            AchievementBadge(
                id = "b2",
                nameEn = "Helping Hand",
                nameBn = "সহায়তাকারী",
                descEn = "Completed 3 verified blood donations",
                descBn = "৩টি সফল রক্তদানের মাধ্যমে মানবসেবা",
                requiredDonations = 3,
                unlocked = donationCount >= 3
            ),
            AchievementBadge(
                id = "b3",
                nameEn = "Life Saver",
                nameBn = "জীবন রক্ষাকারী",
                descEn = "Completed 5 verified blood donations",
                descBn = "৫টি জীবন রক্ষায় গৌরবময় অবদান",
                requiredDonations = 5,
                unlocked = donationCount >= 5
            ),
            AchievementBadge(
                id = "b4",
                nameEn = "Community Hero",
                nameBn = "কমিউনিটি হিরো",
                descEn = "Completed 10+ verified blood donations",
                descBn = "১০টির বেশি রক্তদানে সমাজের আলোকবর্তিকা",
                requiredDonations = 10,
                unlocked = donationCount >= 10
            )
        )
    }
}
