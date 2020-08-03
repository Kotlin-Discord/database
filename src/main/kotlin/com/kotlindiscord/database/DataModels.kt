package com.kotlindiscord.database

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class User(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<User>(Users)

    var discriminator by Users.discriminator
    var userName by Users.username
    var avatarUrl by Users.avatarUrl
    val present by Users.present

    var roles by Role via UserRoles
}

class Infraction(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Infraction>(Infractions)

    var infractor by User referencedOn Infractions.infractor
    var user by User referencedOn Infractions.user
    var reason by Infractions.reason
    var type by Infractions.type
    var expires by Infractions.expires
    var created by Infractions.created
}

class Role(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Role>(Roles)

    var name by Roles.name
    var colour by Roles.colour
    var holders by User via UserRoles
}
