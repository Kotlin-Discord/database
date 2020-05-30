package com.kotlindiscord.database.migrator

interface AbstractMigration {
    fun migrateUp()
    fun migrateDown()
}
