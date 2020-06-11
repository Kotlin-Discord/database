package com.kotlindiscord.database.migrator
/** The base for user defined migrations. */
interface AbstractMigration {


    /**
     * The ID for this migration.
     *
     * Assuming this Migration was created in the default way, then this property is the unix timestamp of when it
     * was created.
     */
    val id@Suppress("MagicNumber")
        get() = this.javaClass.name.takeLast(13).toLong()

    /**
     * Called when migrating the database upwards. Should contain a transaction, and assume there is a db connection.
     *
     * The function will be called within a transaction, rolling back the parent transaction if any errors are
     * encountered. When called, a database connection will be in scope.
     */
    fun migrateUp()

    /**
     * Called when migrating the database downwards. Should contain a transaction, and assume there is a db connection.
     *
     * As this is used to migrate the database downwards, if it is not valid, then it will not be possible to migrate
     * down. This will be called within a transaction with a database connection, if any errors are encountered the
     * parent transaction will rollback.
     */
    fun migrateDown()
}
