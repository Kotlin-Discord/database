package com.kotlindiscord.database.migrator

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.reflections8.Reflections

object Migrations : IdTable<Long>() {
    val applied = bool("applied").default(false)

    override val id = long("id").entityId()
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
    private val currentMigrationId: Long?
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
    private val migrationsMap: Map<Long, AbstractMigration> by lazy {

        val reflections = Reflections("com.kotlindiscord.database.migrations")
        val classes = reflections.getSubTypesOf(AbstractMigration::class.java)

        val migrationInstances = classes
            .filter { it.constructors.isNotEmpty() }
            .map { it.constructors.first().newInstance() }
            .filterIsInstance<AbstractMigration>()

        migrationInstances.map { it.id }.zip(migrationInstances).toMap()
    }

    /**
     * The first unapplied migration.
     */
    private val nextMigration: AbstractMigration?
        get() {
            val sortedMigrationKeys: List<Long> = migrationsMap.keys.sorted()
            return if (currentMigrationId == null) {
                migrationsMap[sortedMigrationKeys.min()!!]
            } else {
                migrationsMap[sortedMigrationKeys[sortedMigrationKeys.indexOf(currentMigrationId as Long) + 1]]
            }
        }

    /**
     * Moves the database up a single migration.
     *
     * If there are any recorded migrations that haven't been applied, this will apply that. Otherwise this will look
     * for the next migration class that hasn't been applied yet, and apply that.
     */
    fun migrateUp() {
        check(migrationsMap.isNotEmpty()) { throw IllegalStateException("No migrations found.") }
        check(currentMigrationId != migrationsMap.keys.max()) {
            throw IllegalStateException("Database fully migrated.")
        }

        val migrationToApply = nextMigration
        checkNotNull(migrationToApply) { throw IllegalStateException("There was a problem migrating.") }

        transaction {

            migrationToApply.migrateUp()

            SchemaUtils.create(Migrations)
            val appliedMigrationDatabaseObject =
                Migration.findById(migrationToApply.id) ?: Migration.new(migrationToApply.id) { applied = true }
            appliedMigrationDatabaseObject.applied = true
        }
    }

    /**
     * Migrates the database down a step by calling the `down` method on the currently applied migration.
     */
    fun migrateDown() {
        val currentMigrationObj = migrationsMap[currentMigrationId]
        checkNotNull(currentMigrationObj) { throw IllegalStateException("The database has no applied migrations.") }

        transaction {
            currentMigrationObj.migrateDown()
            Migration[currentMigrationObj.id].applied = false
        }
    }

    /**
     * Moves the database to the most recent migration, applying all migrations in order.
     *
     * If this method has been called, the database should be able to migrate up at least once. If not, the user has
     * done something wrong.
     */
    fun migrateAll() {
        migrateUp()
        while (true) {
            try {
                migrateUp()
            } catch (e: IllegalStateException) {
                break
            }
        }

    }
}
