package com.kotlindiscord.database.migrations

import com.kotlindiscord.database.*
import com.kotlindiscord.database.migrator.AbstractMigration
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class Migration1591121527450 : AbstractMigration {
    override fun migrateUp() {
        transaction {
            exec("CREATE TYPE InfractionTypes AS ENUM ('Kick', 'Ban', 'Warn');")
            SchemaUtils.create(Roles, Users, Infractions)
            SchemaUtils.create(UserRoles)
        }
    }

    override fun migrateDown() {
        transaction {
            SchemaUtils.drop(UserRoles)
            SchemaUtils.drop(Roles, Users, Infractions)
            exec("DROP TYPE InfractionTypes")
        }
    }
}
