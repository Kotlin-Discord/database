package com.kotlindiscord.database.migrations

import com.kotlindiscord.database.Infractions
import com.kotlindiscord.database.migrator.AbstractMigration
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class NullableDates1592914484109 : AbstractMigration {
    override fun migrateUp() {
        transaction {
            // I think this also alters existing columns. Seems to in testing.
            SchemaUtils.createMissingTablesAndColumns(Infractions)
        }

    }

    override fun migrateDown() {
        throw NotImplementedError("No migrateDown for this migration.")
    }
}
