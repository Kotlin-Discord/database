package com.kotlindiscord.database.migrator

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.reflections8.Reflections

object Migrations : IdTable<Long>() {
    override val id = long("id").entityId()
    val applied = bool("applied").default(false)
    override val primaryKey = PrimaryKey(id, name = "PK_MIGRATION_ID")
}

class Migration(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Migration>(Migrations)

    var applied by Migrations.applied
}

/**
 * Interfaces with the database to manage and apply migrations. Requires a Database connection.
 */
class MigrationsManager {

    /**
     * Gets the id of the migration which was most recently applied to the database.
     */
    val currentMigrationId: Long?
        get() {
            var maxMigrationId: Long? = null
            transaction {
                SchemaUtils.create(Migrations)
                val appliedMigrations = Migration.find { Migrations.applied eq true }
                if (!appliedMigrations.empty()) {
                    val maxMigration = appliedMigrations.maxBy { it.id }
                    if (maxMigration != null) maxMigrationId = maxMigration.id.value
                }
            }

            return maxMigrationId
        }

    /**
     * Map of the user created migrations against their IDs (the time at which they were created).
     */
    val migrationsMap: Map<Long, AbstractMigration> by lazy {
        // TODO handle the case where we find a non migration object more elegantly
        val reflections = Reflections("com.kotlindiscord.database.migrations")

        val classes = reflections.getSubTypesOf(AbstractMigration::class.java)
        @Suppress("MagicNumber")
        val migrationIds = classes.map { it.name.takeLast(13).toLong() }
        val maybeMigrations = classes.map { it.constructors.first().newInstance() }

        if (maybeMigrations.all { it is AbstractMigration }) {
            migrationIds.zip(maybeMigrations as List<AbstractMigration>).toMap()
        } else mapOf()

    }


    /**
     * Moves the database up a single migration.
     *
     * If there are any recorded migrations that haven't been applied, this will apply that. Otherwise this will look
     * for the next migration class that hasn't been applied yet, and apply that.
     */
    fun migrateUp() {
        TODO()
    }

    /**
     * Migrates the database down a step by calling the `down` method on the currently applied migration.
     */
    fun migrateDown() {
        TODO()
    }

    /**
     * Moves the database to the most recent migration, applying all migrations in order.
     */
    fun migrateAll() {
        TODO()
    }
}


/* Temporary main function for testing */
fun main() {
    Database.connect(
        "jdbc:postgresql://" + System.getenv("DB_URL"),
        driver = "org.postgresql.Driver",
        user = System.getenv("DB_USER"),
        password = System.getenv("DB_PASSWORD")
    )

    val obs = MigrationsManager().migrationsMap
    println(obs)
}
