package com.platinumassistant.lite

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Locale

class CommandParserTest {
    @Test
    fun normalizeInput_trimsAndLowercases() {
        val input = "  Open  Gmail "
        val out = CommandParser.normalizeInput(input)
        assertEquals("open  gmail", out)
    }

    @Test
    fun batteryResponse_english() {
        val ctx = mockk<android.content.Context>(relaxed = true)
        mockkObject(BatteryUtils)
        every { BatteryUtils.getBatteryPercent(any()) } returns 88

        val res = CommandParser.parseAndExecute(ctx, "battery status")
        assertEquals("Battery at 88%", res)
    }

    @Test
    fun batteryResponse_arabic() {
        val ctx = mockk<android.content.Context>(relaxed = true)
        mockkObject(BatteryUtils)
        every { BatteryUtils.getBatteryPercent(any()) } returns 55

        val res = CommandParser.parseAndExecute(ctx, "ما حالة البطارية")
        assertEquals("مستوى البطارية 55%", res)
    }

    @Test
    fun rememberCommand_savesFact() {
        val ctx = mockk<android.content.Context>(relaxed = true)
        mockkObject(MemoryManager)

        val res = CommandParser.parseAndExecute(ctx, "remember my name is Omar")

        verify { MemoryManager.put(any(), "fact_last", "my name is omar") }
        assertEquals("Saved: my name is omar", res)
    }
}
