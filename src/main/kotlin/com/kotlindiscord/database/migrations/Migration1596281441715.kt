package com.kotlindiscord.database.migrations

import com.kotlindiscord.database.migrator.AbstractMigration
import mu.KotlinLogging
import org.jetbrains.exposed.sql.transactions.transaction

class Migration1596281441715 : AbstractMigration {
    val logger = KotlinLogging.logger {}

    override fun migrateUp() {
        transaction {
            exec("ALTER TABLE infractions ALTER COLUMN expires DROP NOT NULL;")
            exec("ALTER TYPE InfractionTypes ADD VALUE IF NOT EXISTS 'Mute';")
        }
    }

    override fun migrateDown() {
        transaction {
            exec("ALTER TABLE infractions ALTER COLUMN expires SET NOT NULL;")
            logger.error { "Impossible to fully downgrade - values may not be removed from enum types!" }
        }
    }
}
