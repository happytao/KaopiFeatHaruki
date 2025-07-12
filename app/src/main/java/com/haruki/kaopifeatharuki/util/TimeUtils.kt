package com.haruki.kaopifeatharuki.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object TimeUtils {

    fun timestampToTimeStr(timestamp: Long): String {
        val instant = Instant.ofEpochMilli(timestamp)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.of("GMT+8")) // 东八区
        return formatter.format(instant)
    }
}