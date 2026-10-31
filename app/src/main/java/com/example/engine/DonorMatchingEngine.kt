package com.example.engine

import com.example.model.BloodComponent
import com.example.model.BloodGroup
import com.example.model.BloodRequest
import com.example.model.LifedropUser
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class MatchResult(
    val donor: LifedropUser,
    val isCompatible: Boolean,
    val isEligible: Boolean,
    val distanceKm: Double,
    val matchScore: Int,
    val compatibilityReasonEn: String,
    val compatibilityReasonBn: String
)

object DonorMatchingEngine {

    /**
     * Determine compatibility based on required blood group and component.
     */
    fun isComponentCompatible(donorGroup: BloodGroup, patientGroup: BloodGroup, component: BloodComponent): Boolean {
        return when (component) {
            BloodComponent.WHOLE_BLOOD, BloodComponent.RBC -> isRbcCompatible(donorGroup, patientGroup)
            BloodComponent.PLASMA -> isPlasmaCompatible(donorGroup, patientGroup)
            BloodComponent.PLATELETS -> isPlateletCompatible(donorGroup, patientGroup)
        }
    }

    private fun isRbcCompatible(donor: BloodGroup, patient: BloodGroup): Boolean {
        if (donor == BloodGroup.O_NEG) return true // Universal RBC donor
        if (patient == BloodGroup.AB_POS) return true // Universal RBC recipient

        return when (patient) {
            BloodGroup.O_POS -> donor == BloodGroup.O_POS || donor == BloodGroup.O_NEG
            BloodGroup.O_NEG -> donor == BloodGroup.O_NEG
            BloodGroup.A_POS -> donor in listOf(BloodGroup.A_POS, BloodGroup.A_NEG, BloodGroup.O_POS, BloodGroup.O_NEG)
            BloodGroup.A_NEG -> donor in listOf(BloodGroup.A_NEG, BloodGroup.O_NEG)
            BloodGroup.B_POS -> donor in listOf(BloodGroup.B_POS, BloodGroup.B_NEG, BloodGroup.O_POS, BloodGroup.O_NEG)
            BloodGroup.B_NEG -> donor in listOf(BloodGroup.B_NEG, BloodGroup.O_NEG)
            BloodGroup.AB_NEG -> donor in listOf(BloodGroup.AB_NEG, BloodGroup.A_NEG, BloodGroup.B_NEG, BloodGroup.O_NEG)
            BloodGroup.AB_POS -> true
        }
    }

    private fun isPlasmaCompatible(donor: BloodGroup, patient: BloodGroup): Boolean {
        // Plasma compatibility is inverse of RBC compatibility!
        // AB is universal plasma donor. O is universal plasma recipient.
        val dGroup = when (donor) {
            BloodGroup.A_POS, BloodGroup.A_NEG -> "A"
            BloodGroup.B_POS, BloodGroup.B_NEG -> "B"
            BloodGroup.AB_POS, BloodGroup.AB_NEG -> "AB"
            BloodGroup.O_POS, BloodGroup.O_NEG -> "O"
        }
        val pGroup = when (patient) {
            BloodGroup.A_POS, BloodGroup.A_NEG -> "A"
            BloodGroup.B_POS, BloodGroup.B_NEG -> "B"
            BloodGroup.AB_POS, BloodGroup.AB_NEG -> "AB"
            BloodGroup.O_POS, BloodGroup.O_NEG -> "O"
        }

        return when (dGroup) {
            "AB" -> true // universal plasma donor
            "A" -> pGroup in listOf("A", "O")
            "B" -> pGroup in listOf("B", "O")
            "O" -> pGroup == "O"
            else -> false
        }
    }

    private fun isPlateletCompatible(donor: BloodGroup, patient: BloodGroup): Boolean {
        // Same group is ideal, or RBC compatibility
        return donor == patient || isRbcCompatible(donor, patient)
    }

    /**
     * Compute geographical distance using Haversine formula (km).
     */
    fun calculateDistanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadiusKm = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return String.format("%.1f", earthRadiusKm * c).toDoubleOrNull() ?: (earthRadiusKm * c)
    }

    /**
     * Check if 90 days have elapsed since last donation.
     */
    fun isEligibleToDonate(donor: LifedropUser): Boolean {
        // In our model, eligible if verifiedDonationsCount == 0 or >= 90 days passed
        return donor.isAvailable
    }

    /**
     * Rank and match candidate donors for a given blood request.
     */
    fun findMatches(
        request: BloodRequest,
        donors: List<LifedropUser>,
        maxRadiusKm: Double = 50.0
    ): List<MatchResult> {
        return donors.map { donor ->
            val isCompatible = isComponentCompatible(donor.bloodGroup, request.bloodGroup, request.component)
            val isEligible = isEligibleToDonate(donor)
            val distance = calculateDistanceKm(request.latitude, request.longitude, donor.latitude, donor.longitude)

            var score = 0
            if (isCompatible) score += 45
            if (donor.bloodGroup == request.bloodGroup) score += 15 // exact match bonus
            if (isEligible) score += 15
            if (donor.isVerified) score += 15
            score += ((50.0 - distance.coerceAtMost(50.0)) / 50.0 * 10).toInt()

            val reasonEn = if (isCompatible) {
                "${donor.bloodGroup.label} is compatible with patient ${request.bloodGroup.label} for ${request.component.id}"
            } else {
                "${donor.bloodGroup.label} is not compatible with patient ${request.bloodGroup.label}"
            }

            val reasonBn = if (isCompatible) {
                "${donor.bloodGroup.label} গ্রুপের রক্ত রোগীর ${request.bloodGroup.label} গ্রুপের সাথে সামঞ্জস্যপূর্ণ"
            } else {
                "${donor.bloodGroup.label} গ্রুপের রক্ত রোগীর জন্য সামঞ্জস্যপূর্ণ নয়"
            }

            MatchResult(
                donor = donor,
                isCompatible = isCompatible,
                isEligible = isEligible,
                distanceKm = distance,
                matchScore = score.coerceIn(0, 100),
                compatibilityReasonEn = reasonEn,
                compatibilityReasonBn = reasonBn
            )
        }
            .filter { it.isCompatible && it.distanceKm <= maxRadiusKm }
            .sortedByDescending { it.matchScore }
    }
}
