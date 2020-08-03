package com.kotlindiscord.database.migrations

import com.kotlindiscord.database.migrator.AbstractMigration
import org.jetbrains.exposed.sql.transactions.transaction

class Migration1596458490767 : AbstractMigration {
    override fun migrateUp() {
        transaction {
            exec("ALTER TABLE users ADD COLUMN present BOOLEAN")
            exec("UPDATE users SET present = TRUE")
            exec("ALTER TABLE users ALTER COLUMN present SET NOT NULL")
        }
    }

    override fun migrateDown() {
        transaction {
            exec("ALTER TABLE users DROP COLUMN present")
        }
    }
}
