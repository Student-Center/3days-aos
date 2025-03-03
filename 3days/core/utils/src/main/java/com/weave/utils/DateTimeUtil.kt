package com.weave.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object DateTimeUtil {
    fun parseDateTime(input: String): LocalDateTime? {
        return runCatching {
            val formattedInput = if (input.length > 26) {
                input.substring(0, 26)
            } else {
                input
            }

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
            LocalDateTime.parse(formattedInput, formatter)
        }.getOrNull()
    }



    fun formatChatTime(input: String): String {
        val dateTime = parseDateTime(input) ?: return ""

        val hour = dateTime.hour
        val minute = dateTime.minute

        val ampm = if (hour < 12) "오전" else "오후"
        val formattedHour = if (hour % 12 == 0) 12 else hour % 12
        val formattedMinute = String.format(Locale.KOREA, "%02d", minute)

        return "$ampm $formattedHour:$formattedMinute"
    }

    fun formatDayDivider(input: String): String {
        val dateTime = parseDateTime(input) ?: return ""

        val month = dateTime.month.getDisplayName(TextStyle.SHORT, Locale.KOREA)
        val dayOfMonth = dateTime.dayOfMonth
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREA)

        return "$month ${dayOfMonth}일 (${dayOfWeek.take(1)})"
    }
}

