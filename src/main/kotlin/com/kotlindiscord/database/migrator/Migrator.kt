package com.kotlindiscord.database.migrator

import com.kotlindiscord.database.migrator.commands.CreateMigration
import com.kotlindiscord.database.migrator.commands.MigrateUp
import picocli.CommandLine
import java.util.concurrent.Callable
import kotlin.system.exitProcess

@CommandLine.Command(
    name = "Migrator",
    subcommands = [CreateMigration::class, MigrateUp::class]
)
class Migrator : Callable<Int> {
    override fun call(): Int {
        CommandLine.usage(this, System.out)
        return 0
    }
    fun main(args: Array<String>) {
        val returnValue = CommandLine(Migrator()).execute(*args)
        exitProcess(returnValue)
    }

}

fun main(args: Array<String>) {
    val returnValue = CommandLine(Migrator()).execute(*args)
    exitProcess(returnValue)
}
