package com.kotlindiscord.database.migrator.commands

import picocli.CommandLine.Command
import java.util.concurrent.Callable

@Command(
    name = "up",
    description = ["migrate the database"]
)
class MigrateUp : Callable<Int> {

    override fun call(): Int {
        println("You have called the yet-to-be-implemented up command")

        // TODO:
        // Get the current migration from the database
        // Try and get a more recent migration from kom.kotlindiscord.database.migrations
        // Call the .migrateUp() method on that migration
        // Return a message if the migration is successful
        // Give info about any failures
        return 0
    }
}

