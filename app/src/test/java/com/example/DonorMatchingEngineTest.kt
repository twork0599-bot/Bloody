package com.example

import com.example.engine.DonorMatchingEngine
import com.example.engine.FraudDetectionEngine
import com.example.model.BloodComponent
import com.example.model.BloodGroup
import com.example.model.BloodRequest
import com.example.model.LifedropUser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DonorMatchingEngineTest {

    @Test
    fun testUniversalRbcDonorCompatibility() {
        // O- is universal RBC donor
        BloodGroup.entries.forEach { recipient ->
            assertTrue(
                "O- should be compatible with $recipient for RBC",
                DonorMatchingEngine.isComponentCompatible(BloodGroup.O_NEG, recipient, BloodComponent.RBC)
            )
        }
    }

    @Test
    fun testUniversalRbcRecipientCompatibility() {
        // AB+ is universal RBC recipient
        BloodGroup.entries.forEach { donor ->
            assertTrue(
                "AB+ should receive RBC from $donor",
                DonorMatchingEngine.isComponentCompatible(donor, BloodGroup.AB_POS, BloodComponent.RBC)
            )
        }
    }

    @Test
    fun testIncompatibleRbcTransfusions() {
        // A+ cannot donate to O-
        assertFalse(
            DonorMatchingEngine.isComponentCompatible(BloodGroup.A_POS, BloodGroup.O_NEG, BloodComponent.RBC)
        )
        // B+ cannot donate to A+
        assertFalse(
            DonorMatchingEngine.isComponentCompatible(BloodGroup.B_POS, BloodGroup.A_POS, BloodComponent.RBC)
        )
    }

    @Test
    fun testPlasmaUniversalDonor() {
        // AB is universal plasma donor
        BloodGroup.entries.forEach { recipient ->
            assertTrue(
                "AB+ plasma should be compatible with $recipient",
                DonorMatchingEngine.isComponentCompatible(BloodGroup.AB_POS, recipient, BloodComponent.PLASMA)
            )
        }
    }

    @Test
    fun testDistanceCalculation() {
        // Dhaka Dhanmondi (23.7461, 90.3742) to Shahbagh (23.7380, 90.3957) is approx 2.4 km
        val distance = DonorMatchingEngine.calculateDistanceKm(23.7461, 90.3742, 23.7380, 90.3957)
        assertTrue("Distance should be between 2.0 and 3.0 km, got $distance", distance in 2.0..3.0)
    }

    @Test
    fun testSmartDonorRanking() {
        val request = BloodRequest(
            id = "test_req",
            patientName = "Test Patient",
            bloodGroup = BloodGroup.O_POS,
            component = BloodComponent.RBC,
            latitude = 23.7461,
            longitude = 90.3742
        )

        val donorNear = LifedropUser(
            id = "d_near",
            fullName = "Near Donor",
            bloodGroup = BloodGroup.O_POS,
            latitude = 23.7480,
            longitude = 90.3750,
            isAvailable = true,
            isVerified = true
        )

        val donorFar = LifedropUser(
            id = "d_far",
            fullName = "Far Donor",
            bloodGroup = BloodGroup.O_POS,
            latitude = 23.9500,
            longitude = 90.5000,
            isAvailable = true,
            isVerified = true
        )

        val matches = DonorMatchingEngine.findMatches(request, listOf(donorFar, donorNear), maxRadiusKm = 50.0)
        assertEquals(2, matches.size)
        assertEquals("d_near", matches[0].donor.id)
    }

    @Test
    fun testFraudDetectionForDuplicatesAndVolume() {
        val initialRequest = BloodRequest(
            id = "req_1",
            patientName = "Rahim Mia",
            hospitalName = "General Hospital",
            unitsNeeded = 2,
            contactPhone = "+8801711223344"
        )

        val duplicateRequest = BloodRequest(
            id = "req_2",
            patientName = "Rahim Mia",
            hospitalName = "General Hospital",
            unitsNeeded = 6, // flagged for high volume too
            contactPhone = "+8801711223344"
        )

        val evaluation = FraudDetectionEngine.evaluateRequest(duplicateRequest, listOf(initialRequest))
        assertTrue("Duplicate & high volume should trigger risk score >= 40", evaluation.isFlagged)
        assertTrue(evaluation.riskScore >= 40)
    }
}
