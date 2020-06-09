Database
========

# Defining models and taples
This project relies on [Exposed](https://github.com/JetBrains/Exposed) to define PostgreSQL tables, and access to tables in the database.

The main way to access the database is through the [Exposed DAO](https://github.com/JetBrains/Exposed/wiki/DAO). The models for this project can be found in 
`com.kotlindiscord.database.DataModels`

# Migrations
In order to add new models, three things need to happen. Firstly, a Data Access Object and DSL table will need to be 
defined in `com.kotlindiscord.database.DataModels` and `com.kotlindiscord.database.TableDefinitions`. Secondly, a 
migration will need to be created and applied with the migrations CLI tool.

## Command Line Tool
### Connecting to the database
Connecting to the database requires the following environment variables to be set:
* `DB_URL` 
    * Where the database can be found.
    * Example: `localhost:5432/docker`
* `DB_USER`
    * The user to authenticate as when connecting to the database.
    * Example: `docker`
* `DB_PASSWORD`
    * The password to use when connecting to the database.
    * Example: `docker`

### Configuring the migrations directory
### Commands
* `create`
    * Requires the `MIGRATIONS_DIR` environment variable to be set, and point at the directory where the 
    `com.kotlindiscord.database.migrations` package can be found.
        * Example: `/users/{username}/IdeaProjects/database/src/main/kotlin/com/kotlindiscord/database/migrations`
    * Creates a new blank migration in `com.kotlindiscord.database.migrations`.
    * You are required to fill in the implementations of the `up()` and `down()` methods of the created migration.
        * Migrations can include their own transaction (which will be called as a nested transaction), or they can rely on
        the parent transaction.
    * The tool will need to be recompiled after any new migrations have been added.
* `up`
   * Moves the database forwards a single migration. 
* `down` 
    * Moves the database down a single migration.
* `all`
    * Applies all migrations in order.

