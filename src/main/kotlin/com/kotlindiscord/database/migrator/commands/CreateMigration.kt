package com.kotlindiscord.database.migrator.commands

import picocli.CommandLine.Command
import picocli.CommandLine.Parameters
import java.nio.file.Paths
import java.util.concurrent.Callable

@Command(name = "create", description = ["Create a new migration folder"])
class CreateMigration : Callable<Int> {

    @Parameters(
        paramLabel = "NAME",
        description = ["The name with which to start the migration file"],
        defaultValue = "Migration"
    )
    lateinit var migrationClassName: String


    override fun call(): Int {
        val currentTime = System.currentTimeMillis()
        val migrationFile =
            Paths.get(System.getenv("MIGRATIONS_DIR") + "/$migrationClassName$currentTime.kt").toFile()

        migrationFile.parentFile.mkdirs()
        migrationFile.createNewFile()
        migrationFile.writeText(
            """
                package com.kotlindiscord.database.migrations
                import com.kotlindiscord.database.migrator.AbstractMigration
                class $migrationClassName$currentTime : AbstractMigration {
                    override fun migrateUp() {
                        TODO()
                    }
                
                    override fun migrateDown() {
                        TODO()
                    }
                }
""".trimIndent()
        )

        println("New migration created at $migrationFile")
        return 0
    }
}

