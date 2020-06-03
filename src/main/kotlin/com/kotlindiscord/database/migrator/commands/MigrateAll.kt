package com.kotlindiscord.database.migrator.commands

import com.kotlindiscord.database.connectToDb
import com.kotlindiscord.database.migrator.MigrationsManager
import picocli.CommandLine.Command
import java.util.concurrent.Callable

@Command(
    name = "all",
    description = ["Apply all unapplied migrations."]
)
class MigrateAll : Callable<Int> {

    override fun call(): Int {
        connectToDb()
        MigrationsManager().migrateAll()
        println("All migrations successfully applied.")
        return 0
    }
}

