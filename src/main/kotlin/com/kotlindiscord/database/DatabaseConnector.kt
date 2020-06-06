package com.kotlindiscord.database

import io.sentry.Sentry
import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database

/**
 * Establish a connection to the database and logging settings.
 */
fun startDbConnection() {
    val logger = KotlinLogging.logger {}
    if (System.getenv().getOrDefault("SENTRY_DSN", null) != null) {
        val sentry = Sentry.init()
    }
    logger.info { "Conne" }
    val db = Database.connect(
        "jdbc:postgresql://" + System.getenv("DB_URL"),
        driver = "org.postgresql.Driver",
        user = System.getenv("DB_USER"),
        password = System.getenv("DB_PASSWORD")
    )
    db.useNestedTransactions = true

}
