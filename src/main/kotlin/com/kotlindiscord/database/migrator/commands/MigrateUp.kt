package com.kotlindiscord.database.migrator.commands

import com.kotlindiscord.database.connectToDb
import com.kotlindiscord.database.migrator.MigrationsManager
import picocli.CommandLine.Command
import java.util.concurrent.Callable

@Command(
    name = "up",
    description = ["Migrate the database upwards."]
)
class MigrateUp : Callable<Int> {

    override fun call(): Int {
        connectToDb()
        MigrationsManager().migrateUp()
        println("Database successfully migrated up.")
        return 0
    }
}

