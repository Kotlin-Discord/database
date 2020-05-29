package com.kotlindiscord.database.migrator.commands

import picocli.CommandLine.Command
import java.util.concurrent.Callable

@Command(
    name = "create",
    description = ["Create a new migration folder"]
)

class CreateMigration : Callable<Int> {
    override fun call(): Int {
        println("You have called the yet-to-be-implemented create command")
        // TODO:
        // Create a file named with the current time in com.kotlindiscord.database.migrations
        // Add a boilerplate class to it
        return 0
    }
}

