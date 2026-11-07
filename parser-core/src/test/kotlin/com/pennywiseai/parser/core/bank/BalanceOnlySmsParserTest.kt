package com.pennywiseai.parser.core.bank

import com.pennywiseai.parser.core.TransactionType
import com.pennywiseai.parser.core.test.ExpectedTransaction
import com.pennywiseai.parser.core.test.ParserTestCase
import com.pennywiseai.parser.core.test.ParserTestUtils
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.math.BigDecimal

/**
 * Tests for balance-only SMS parsing from missed-call banking.
 *
 * Balance-only SMS should:
 * - Parse to a BALANCE_UPDATE transaction with zero amount
 * - Extract the correct account number (last 4 digits)
 * - Extract the balance value correctly
 * - Handle various number formats (commas, decimals, whitespace)
 * - Not create a spending/income entry (handled by SmsTransactionProcessor)
 */
class BalanceOnlySmsParserTest {

    @TestFactory
    fun `Axis Bank balance-only SMS parsing`(): List<DynamicTest> {
        val parser = AxisBankParser()

        val testCases = listOf(
            // Basic balance inquiry format (user-provided sanitized fixture)
            ParserTestCase(
                name = "Basic balance inquiry",
                message = "Bal for your Axis Bank A/c no. XX1111 - INR 999999. Download Axis Mobile App from https://example.invalid/promo",
                sender = "AX-AXISBK-S",
                expected = ExpectedTransaction(
                    amount = BigDecimal.ZERO,
                    currency = "INR",
                    type = TransactionType.BALANCE_UPDATE,
                    merchant = "Balance Inquiry",
                    accountLast4 = "1111",
                    balance = BigDecimal("999999"),
                    isFromCard = false
                )
            ),

            // With decimal balance
            ParserTestCase(
                name = "Balance with decimals",
                message = "Bal for your Axis Bank A/c no. XX2222 - INR 12345.67",
                sender = "AX-AXISBK-S",
                expected = ExpectedTransaction(
                    amount = BigDecimal.ZERO,
                    currency = "INR",
                    type = TransactionType.BALANCE_UPDATE,
                    merchant = "Balance Inquiry",
                    accountLast4 = "2222",
                    balance = BigDecimal("12345.67"),
                    isFromCard = false
                )
            ),

            // With comma separators
            ParserTestCase(
                name = "Balance with comma separators",
                message = "Bal for your Axis Bank A/c no. XX3333 - INR 1,23,456.78",
                sender = "AX-AXISBK-S",
                expected = ExpectedTransaction(
                    amount = BigDecimal.ZERO,
                    currency = "INR",
                    type = TransactionType.BALANCE_UPDATE,
                    merchant = "Balance Inquiry",
                    accountLast4 = "3333",
                    balance = BigDecimal("123456.78"),
                    isFromCard = false
                )
            ),

            // Full "Balance" word instead of "Bal"
            ParserTestCase(
                name = "Full Balance word",
                message = "Balance for your Axis Bank A/c no. XX4444 - INR 50000",
                sender = "AX-AXISBK-S",
                expected = ExpectedTransaction(
                    amount = BigDecimal.ZERO,
                    currency = "INR",
                    type = TransactionType.BALANCE_UPDATE,
                    merchant = "Balance Inquiry",
                    accountLast4 = "4444",
                    balance = BigDecimal("50000"),
                    isFromCard = false
                )
            ),

            // Rs. format instead of INR
            ParserTestCase(
                name = "Rs format balance",
                message = "Bal for your Axis Bank A/c no. XX5555 - Rs. 75000.50",
                sender = "AX-AXISBK-S",
                expected = ExpectedTransaction(
                    amount = BigDecimal.ZERO,
                    currency = "INR",
                    type = TransactionType.BALANCE_UPDATE,
                    merchant = "Balance Inquiry",
                    accountLast4 = "5555",
                    balance = BigDecimal("75000.50"),
                    isFromCard = false
                )
            ),

            // Different sender ID variants
            ParserTestCase(
                name = "JD sender prefix",
                message = "Bal for your Axis Bank A/c no. XX6666 - INR 100000",
                sender = "JD-AXISBK-S",
                expected = ExpectedTransaction(
                    amount = BigDecimal.ZERO,
                    currency = "INR",
                    type = TransactionType.BALANCE_UPDATE,
                    merchant = "Balance Inquiry",
                    accountLast4 = "6666",
                    balance = BigDecimal("100000"),
                    isFromCard = false
                )
            )
        )

        return ParserTestUtils.runTestSuite(parser, testCases, suiteName = "Axis Bank Balance-Only SMS")
    }

    @TestFactory
    fun `IDFC First Bank balance-only SMS parsing`(): List<DynamicTest> {
        val parser = IDFCFirstBankParser()

        val testCases = listOf(
            // Basic balance inquiry format (user-provided sanitized fixture)
            ParserTestCase(
                name = "Basic balance inquiry",
                message = "Available Balance in A/C XX3333 is Rs.88888 Team IDFC FIRST Bank.",
                sender = "JX-IDFCFB-S",
                expected = ExpectedTransaction(
                    amount = BigDecimal.ZERO,
                    currency = "INR",
                    type = TransactionType.BALANCE_UPDATE,
                    merchant = "Balance Inquiry",
                    accountLast4 = "3333",
                    balance = BigDecimal("88888"),
                    isFromCard = false
                )
            ),

            // With decimal balance
            ParserTestCase(
                name = "Balance with decimals",
                message = "Available Balance in A/C XX4444 is Rs.12345.67 Team IDFC FIRST Bank.",
                sender = "JX-IDFCFB-S",
                expected = ExpectedTransaction(
                    amount = BigDecimal.ZERO,
                    currency = "INR",
                    type = TransactionType.BALANCE_UPDATE,
                    merchant = "Balance Inquiry",
                    accountLast4 = "4444",
                    balance = BigDecimal("12345.67"),
                    isFromCard = false
                )
            ),

            // With comma separators
            ParserTestCase(
                name = "Balance with commas",
                message = "Available Balance in A/C XX5555 is Rs 1,50,000.00 Team IDFC FIRST Bank.",
                sender = "JX-IDFCFB-S",
                expected = ExpectedTransaction(
                    amount = BigDecimal.ZERO,
                    currency = "INR",
                    type = TransactionType.BALANCE_UPDATE,
                    merchant = "Balance Inquiry",
                    accountLast4 = "5555",
                    balance = BigDecimal("150000.00"),
                    isFromCard = false
                )
            ),

            // INR format instead of Rs
            ParserTestCase(
                name = "INR format balance",
                message = "Available Balance in A/C XX6666 is INR 250000 Team IDFC FIRST Bank.",
                sender = "BM-IDFCBK-S",
                expected = ExpectedTransaction(
                    amount = BigDecimal.ZERO,
                    currency = "INR",
                    type = TransactionType.BALANCE_UPDATE,
                    merchant = "Balance Inquiry",
                    accountLast4 = "6666",
                    balance = BigDecimal("250000"),
                    isFromCard = false
                )
            ),

            // Different IDFC sender variants
            ParserTestCase(
                name = "IDFCBK sender",
                message = "Available Balance in A/C XX7777 is Rs.99999 Team IDFC FIRST Bank.",
                sender = "AX-IDFCBK-T",
                expected = ExpectedTransaction(
                    amount = BigDecimal.ZERO,
                    currency = "INR",
                    type = TransactionType.BALANCE_UPDATE,
                    merchant = "Balance Inquiry",
                    accountLast4 = "7777",
                    balance = BigDecimal("99999"),
                    isFromCard = false
                )
            )
        )

        return ParserTestUtils.runTestSuite(parser, testCases, suiteName = "IDFC First Bank Balance-Only SMS")
    }

    @TestFactory
    fun `Kotak Bank balance-only SMS parsing`(): List<DynamicTest> {
        val parser = KotakBankParser()

        val testCases = listOf(
            // Basic balance inquiry format (user-provided sanitized fixture)
            ParserTestCase(
                name = "Basic balance inquiry",
                message = "Dear Customer, available Balance in A/C : xx5555 is INR : 66666 . Kotak Bank",
                sender = "JX-KOTAKB-S",
                expected = ExpectedTransaction(
                    amount = BigDecimal.ZERO,
                    currency = "INR",
                    type = TransactionType.BALANCE_UPDATE,
                    merchant = "Balance Inquiry",
                    accountLast4 = "5555",
                    balance = BigDecimal("66666"),
                    isFromCard = false
                )
            ),

            // With decimal balance
            ParserTestCase(
                name = "Balance with decimals",
                message = "Dear Customer, available Balance in A/C : xx6666 is INR : 12345.67 . Kotak Bank",
                sender = "JX-KOTAKB-S",
                expected = ExpectedTransaction(
                    amount = BigDecimal.ZERO,
                    currency = "INR",
                    type = TransactionType.BALANCE_UPDATE,
                    merchant = "Balance Inquiry",
                    accountLast4 = "6666",
                    balance = BigDecimal("12345.67"),
                    isFromCard = false
                )
            ),

            // With comma separators
            ParserTestCase(
                name = "Balance with commas",
                message = "Dear Customer, available Balance in A/C : xx7777 is INR : 1,50,000.00 . Kotak Bank",
                sender = "JX-KOTAKB-S",
                expected = ExpectedTransaction(
                    amount = BigDecimal.ZERO,
                    currency = "INR",
                    type = TransactionType.BALANCE_UPDATE,
                    merchant = "Balance Inquiry",
                    accountLast4 = "7777",
                    balance = BigDecimal("150000.00"),
                    isFromCard = false
                )
            ),

            // Rs format instead of INR
            ParserTestCase(
                name = "Rs format balance",
                message = "Dear Customer, available Balance in A/C : xx8888 is Rs. 250000 . Kotak Bank",
                sender = "JX-KOTAKB-S",
                expected = ExpectedTransaction(
                    amount = BigDecimal.ZERO,
                    currency = "INR",
                    type = TransactionType.BALANCE_UPDATE,
                    merchant = "Balance Inquiry",
                    accountLast4 = "8888",
                    balance = BigDecimal("250000"),
                    isFromCard = false
                )
            ),

            // Without extra spaces around colon
            ParserTestCase(
                name = "Without extra spaces",
                message = "Dear Customer, available Balance in A/C: XX9999 is INR: 55555. Kotak Bank",
                sender = "JX-KOTAKB-S",
                expected = ExpectedTransaction(
                    amount = BigDecimal.ZERO,
                    currency = "INR",
                    type = TransactionType.BALANCE_UPDATE,
                    merchant = "Balance Inquiry",
                    accountLast4 = "9999",
                    balance = BigDecimal("55555"),
                    isFromCard = false
                )
            )
        )

        return ParserTestUtils.runTestSuite(parser, testCases, suiteName = "Kotak Bank Balance-Only SMS")
    }

    @TestFactory
    fun `Balance-only SMS should not interfere with transaction parsing`(): List<DynamicTest> {
        val tests = mutableListOf<DynamicTest>()

        // Axis Bank: Normal transaction should still parse correctly
        val axisParser = AxisBankParser()
        tests.add(DynamicTest.dynamicTest("Axis transaction parsing unaffected") {
            val result = axisParser.parse(
                "INR 500.00 debited from A/c no. XX1234 on 01-01-2026 10:00:00 IST. Avl bal: INR 9500.00. Not you? SMS BLOCKCARD to +91 - Axis Bank",
                "JD-AXISBK-S",
                System.currentTimeMillis()
            )
            assert(result != null) { "Transaction should parse" }
            assert(result!!.type == TransactionType.EXPENSE) { "Should be EXPENSE, not BALANCE_UPDATE" }
            assert(result.amount == BigDecimal("500.00")) { "Amount should be 500.00" }
        })

        // IDFC: Normal transaction should still parse correctly
        val idfcParser = IDFCFirstBankParser()
        tests.add(DynamicTest.dynamicTest("IDFC transaction parsing unaffected") {
            val result = idfcParser.parse(
                "Your A/C XX1234 is debited by INR 1000.00 on 01/01/26 10:00. New Bal :INR 50000.00",
                "BM-IDFCBK-S",
                System.currentTimeMillis()
            )
            assert(result != null) { "Transaction should parse" }
            assert(result!!.type == TransactionType.EXPENSE) { "Should be EXPENSE, not BALANCE_UPDATE" }
            assert(result.amount == BigDecimal("1000.00")) { "Amount should be 1000.00" }
        })

        // Kotak: Normal transaction should still parse correctly
        val kotakParser = KotakBankParser()
        tests.add(DynamicTest.dynamicTest("Kotak transaction parsing unaffected") {
            val result = kotakParser.parse(
                "Sent Rs.500 from Kotak Bank AC X1234 to merchant@bank on 01-Jan-26. UPI Ref 123456",
                "JX-KOTAKB-S",
                System.currentTimeMillis()
            )
            assert(result != null) { "Transaction should parse" }
            assert(result!!.type == TransactionType.EXPENSE) { "Should be EXPENSE, not BALANCE_UPDATE" }
            assert(result.amount == BigDecimal("500")) { "Amount should be 500" }
        })

        return tests
    }

    @TestFactory
    fun `Edge cases for balance-only SMS`(): List<DynamicTest> {
        val tests = mutableListOf<DynamicTest>()

        val axisParser = AxisBankParser()

        // Test for malformed balance (no valid digits)
        tests.add(DynamicTest.dynamicTest("Malformed balance returns null") {
            val result = axisParser.parse(
                "Bal for your Axis Bank A/c no. XX1234 - INR invalid",
                "JD-AXISBK-S",
                System.currentTimeMillis()
            )
            assert(result == null) { "Should not parse malformed balance" }
        })

        // Balance inquiry without proper "A/c no." pattern should not parse as balance-only
        tests.add(DynamicTest.dynamicTest("Message without A/c pattern is not balance-only") {
            val result = axisParser.parse(
                "Bal for your Axis Bank account is INR 9900.00",
                "JD-AXISBK-S",
                System.currentTimeMillis()
            )
            // This doesn't have "A/c no." so won't match balance-only pattern
            // and lacks transaction keywords, so returns null
            assert(result == null) { "Should not parse incomplete balance inquiry" }
        })

        return tests
    }
}
