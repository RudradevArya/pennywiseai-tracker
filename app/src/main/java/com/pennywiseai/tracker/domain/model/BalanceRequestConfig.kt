package com.pennywiseai.tracker.domain.model

/**
 * Configuration for banks that support missed-call balance inquiry.
 *
 * These are official toll-free numbers that auto-disconnect after the call
 * is placed (user hangs up or bank disconnects) and send a balance SMS
 * shortly after. No actual call charges apply.
 *
 * Bank accounts only — credit cards are not supported as missed-call balance
 * inquiry typically doesn't work for credit card accounts.
 */
object BalanceRequestConfig {

    /**
     * Map of bank name (as returned by parser's getBankName()) to its official
     * missed-call balance inquiry number.
     *
     * User-tested numbers as of 2026:
     * - Axis Bank: auto-disconnects, balance SMS arrives
     * - IDFC First Bank: may ring until user hangs up, balance SMS arrives
     * - Kotak Bank: auto-disconnects, balance SMS arrives
     */
    private val balanceRequestNumbers: Map<String, String> = mapOf(
        "Axis Bank" to "18004195959",
        "IDFC First Bank" to "18002700720",
        "Kotak Bank" to "18002707300"
    )

    /**
     * Returns the missed-call balance inquiry number for a bank, or null if
     * the bank doesn't support this feature.
     */
    fun getBalanceRequestNumber(bankName: String): String? =
        balanceRequestNumbers[bankName]

    /**
     * Returns true if the bank supports missed-call balance inquiry.
     * Only bank accounts (not credit cards or loans) are supported.
     */
    fun supportsBalanceRequest(bankName: String, isCreditCard: Boolean, isLoan: Boolean = false): Boolean =
        !isCreditCard && !isLoan && balanceRequestNumbers.containsKey(bankName)

    /**
     * Returns all supported bank names for balance request.
     */
    fun supportedBankNames(): Set<String> = balanceRequestNumbers.keys
}
