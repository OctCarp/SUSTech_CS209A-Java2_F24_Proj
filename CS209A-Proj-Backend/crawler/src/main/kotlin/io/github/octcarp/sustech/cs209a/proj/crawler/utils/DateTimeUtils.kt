package io.github.octcarp.sustech.cs209a.proj.crawler.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun Long.secondToLocalDateTime(): LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochSecond(this), ZoneId.systemDefault())

fun LocalDateTime.toTimestamp(): Long =
    this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()