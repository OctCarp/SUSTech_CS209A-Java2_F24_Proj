package io.github.octcarp.sustech.cs209a.proj.crawler.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun Long.toLocalDateTime(): LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())

fun LocalDateTime.toTimestamp(): Long =
    this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()