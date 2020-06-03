package com.kotlindiscord.database.migrator

import com.kotlindiscord.database.migrator.commands.CreateMigration
import com.kotlindiscord.database.migrator.commands.MigrateAll
import com.kotlindiscord.database.migrator.commands.MigrateDown
import com.kotlindiscord.database.migrator.commands.MigrateUp
import picocli.CommandLine
import java.util.concurrent.Callable
import kotlin.system.exitProcess

@CommandLine.Command(
    name = "Migrator",
    subcommands = [CreateMigration::class, MigrateUp::class, MigrateDown::class, MigrateAll::class]
)
class MigratorCli : Callable<Int> {
    override fun call(): Int {
        CommandLine.usage(this, System.out)
        return 0
    }
    fun main(args: Array<String>) {
        val returnValue = CommandLine(MigratorCli()).execute(*args)
        exitProcess(returnValue)
    }

}

fun main(args: Array<String>) {
    val returnValue = CommandLine(MigratorCli()).execute(*args)
    exitProcess(returnValue)
}
