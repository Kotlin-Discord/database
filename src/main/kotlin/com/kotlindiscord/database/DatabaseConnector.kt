package com.kotlindiscord.database

import org.jetbrains.exposed.sql.Database

/**
 * Establish a connection to the database which can be used for exposed.
 */
fun connectToDb() {
    val db = Database.connect(
        "jdbc:postgresql://" + System.getenv("DB_URL"),
        driver = "org.postgresql.Driver",
        user = System.getenv("DB_USER"),
        password = System.getenv("DB_PASSWORD")
    )
    db.useNestedTransactions = true

}
