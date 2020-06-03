package com.kotlindiscord.database.migrator.commands

import com.kotlindiscord.database.connectToDb
import com.kotlindiscord.database.migrator.MigrationsManager
import picocli.CommandLine.Command
import java.util.concurrent.Callable

@Command(
    name = "down",
    description = ["Migrate the database downwards."]
)
class MigrateDown : Callable<Int> {

    override fun call(): Int {
        connectToDb()
        MigrationsManager().migrateDown()
        println("Database successfully migrated down.")
        return 0
    }
}

