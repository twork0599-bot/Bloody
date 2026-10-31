package com.example.engine

import com.example.model.BloodRequest

data class FraudEvaluation(
    val riskScore: Int, // 0 - 100
    val isFlagged: Boolean,
    val flagReasons: List<String>
)

object FraudDetectionEngine {

    /**
     * Evaluate incoming blood request for fraud or suspicious signals.
     */
    fun evaluateRequest(newRequest: BloodRequest, existingRequests: List<BloodRequest>): FraudEvaluation {
        var score = 0
        val reasons = mutableListOf<String>()

        // 1. Check duplicate submissions within 24 hours for the same patient/hospital
        val recentDuplicates = existingRequests.count {
            it.id != newRequest.id &&
            it.patientName.equals(newRequest.patientName, ignoreCase = true) &&
            it.hospitalName.equals(newRequest.hospitalName, ignoreCase = true) &&
            Math.abs(it.createdAt - newRequest.createdAt) < 24 * 3600 * 1000
        }
        if (recentDuplicates > 0) {
            score += 35
            reasons.add("Multiple requests created for same patient/hospital within 24 hours ($recentDuplicates found)")
        }

        // 2. High units check: > 4 units requires hospital verification
        if (newRequest.unitsNeeded > 4) {
            score += 25
            reasons.add("High volume unit request (${newRequest.unitsNeeded} units) flagged for hospital verification")
        }

        // 3. Short / suspicious contact number check
        val cleanedPhone = newRequest.contactPhone.replace(Regex("[^0-9]"), "")
        if (cleanedPhone.length < 10) {
            score += 30
            reasons.add("Incomplete or suspicious contact phone number")
        }

        // 4. Empty or generic hospital name
        if (newRequest.hospitalName.isBlank() || newRequest.hospitalName.length < 4) {
            score += 20
            reasons.add("Unverified or generic hospital name")
        }

        val finalScore = score.coerceIn(0, 100)
        return FraudEvaluation(
            riskScore = finalScore,
            isFlagged = finalScore >= 40,
            flagReasons = reasons
        )
    }
}
