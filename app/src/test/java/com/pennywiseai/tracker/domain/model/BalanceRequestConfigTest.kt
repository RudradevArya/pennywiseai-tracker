package com.pennywiseai.tracker.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for BalanceRequestConfig - balance inquiry via missed-call banking.
 */
class BalanceRequestConfigTest {

    @Test
    fun `Axis Bank is supported`() {
        assertTrue(BalanceRequestConfig.supportsBalanceRequest("Axis Bank", isCreditCard = false))
    }

    @Test
    fun `IDFC First Bank is supported`() {
        assertTrue(BalanceRequestConfig.supportsBalanceRequest("IDFC First Bank", isCreditCard = false))
    }

    @Test
    fun `Kotak Bank is supported`() {
        assertTrue(BalanceRequestConfig.supportsBalanceRequest("Kotak Bank", isCreditCard = false))
    }

    @Test
    fun `HDFC Bank is not supported`() {
        assertFalse(BalanceRequestConfig.supportsBalanceRequest("HDFC Bank", isCreditCard = false))
    }

    @Test
    fun `SBI Bank is not supported`() {
        assertFalse(BalanceRequestConfig.supportsBalanceRequest("SBI Bank", isCreditCard = false))
    }

    @Test
    fun `credit cards are never supported even for supported banks`() {
        assertFalse(BalanceRequestConfig.supportsBalanceRequest("Axis Bank", isCreditCard = true))
        assertFalse(BalanceRequestConfig.supportsBalanceRequest("IDFC First Bank", isCreditCard = true))
        assertFalse(BalanceRequestConfig.supportsBalanceRequest("Kotak Bank", isCreditCard = true))
    }

    @Test
    fun `loans are never supported even for supported banks`() {
        assertFalse(BalanceRequestConfig.supportsBalanceRequest("Axis Bank", isCreditCard = false, isLoan = true))
        assertFalse(BalanceRequestConfig.supportsBalanceRequest("IDFC First Bank", isCreditCard = false, isLoan = true))
        assertFalse(BalanceRequestConfig.supportsBalanceRequest("Kotak Bank", isCreditCard = false, isLoan = true))
    }

    @Test
    fun `credit cards and loans both excluded`() {
        assertFalse(BalanceRequestConfig.supportsBalanceRequest("Axis Bank", isCreditCard = true, isLoan = true))
    }

    @Test
    fun `Axis Bank phone number is correct`() {
        assertEquals("18004195959", BalanceRequestConfig.getBalanceRequestNumber("Axis Bank"))
    }

    @Test
    fun `IDFC First Bank phone number is correct`() {
        assertEquals("18002700720", BalanceRequestConfig.getBalanceRequestNumber("IDFC First Bank"))
    }

    @Test
    fun `Kotak Bank phone number is correct`() {
        assertEquals("18002707300", BalanceRequestConfig.getBalanceRequestNumber("Kotak Bank"))
    }

    @Test
    fun `unsupported bank returns null phone number`() {
        assertNull(BalanceRequestConfig.getBalanceRequestNumber("HDFC Bank"))
        assertNull(BalanceRequestConfig.getBalanceRequestNumber("SBI Bank"))
    }

    @Test
    fun `supportedBankNames returns all supported banks`() {
        val supported = BalanceRequestConfig.supportedBankNames()
        assertEquals(3, supported.size)
        assertTrue(supported.contains("Axis Bank"))
        assertTrue(supported.contains("IDFC First Bank"))
        assertTrue(supported.contains("Kotak Bank"))
    }
}
