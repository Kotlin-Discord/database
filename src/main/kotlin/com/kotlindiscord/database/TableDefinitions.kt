package com.kotlindiscord.database

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.postgresql.util.PGobject

@Suppress("MagicNumber")
object Roles : IdTable<Long>() {
    val name = varchar("name", 100)
    val colour = integer("colour")

    override val id = long("id").entityId()
    override val primaryKey = PrimaryKey(id, name = "PK_Role_ID")
}

object UserRoles : Table() {
    val user = reference("user", Users)
    val role = reference("role", Roles)

    override val primaryKey = PrimaryKey(user, role, name = "PK_user_roles")
}

@Suppress("MagicNumber")
object Users : IdTable<Long>() {
    val username = varchar("name", 32)
    val discriminator = varchar("discriminator", 50)
    val avatarUrl = varchar("is_member", 200)

    override val id = long("id").entityId()
    override val primaryKey = PrimaryKey(id, name = "PK_User_ID")
}

@Suppress("MagicNumber")
object Infractions : LongIdTable() {
    val reason = varchar("reason", 2000)
    val infractor = reference("infractor", Users)
    val user = reference("user", Users)
    val created = datetime("created")
    val expires = datetime("expires")
    val type = customEnumeration(
        "type",
        "InfractionTypes",
        { value -> InfractionTypes.valueOf(value as String) },
        { PGEnum("InfractionTypes", it) }
    )
}


enum class InfractionTypes { Kick, Ban, Warn, }
class PGEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}
