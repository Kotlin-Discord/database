package com.kotlindiscord.database


import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

@Suppress("MagicNumber")
fun main() {
    Database.connect(
        "jdbc:postgresql://" + System.getenv("DB_URL"),
        driver = "org.postgresql.Driver",
        user = System.getenv("DB_USER"),
        password = System.getenv("DB_PASSWORD")
    )
    transaction {
        addLogger(StdOutSqlLogger)
        exec("CREATE TYPE InfractionTypes AS ENUM ('Kick', 'Ban', 'Warn');")
        exec("CREATE TYPE AuditActions AS ENUM ('EditChannel', 'CreateChannel', 'GiveInfraction');")
        SchemaUtils.create(Roles, Users, Infractions,AuditLog)
        SchemaUtils.create(UserRoles )
        Role.new(13) {
            name = "Test Role"
            colour = 55
        }
        User.new(13) {
            discriminator = "Test#1234"
            userName = "Test user's display name"
            avatarUrl = "https://example.com"
            roles = Role.all()
        }
        Infraction.new(13) {
            infractor = User[13]
            user = User[13]
            reason = "ur bad"
            type = InfractionTypes.Warn
            created = LocalDateTime.now()
            expires = LocalDateTime.now()
        }
        AuditLogEntry.new {
            action = AuditActions.CreateChannel
            description = "did summat"
            user = User[13]
        }
    }
}

