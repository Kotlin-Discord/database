package com.kotlindiscord.database

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException

fun <T : LongEntity> LongEntityClass<T>.getOrNull(long: Long): T? = try {
    this[long]
} catch (_: EntityNotFoundException) {
    null
}


