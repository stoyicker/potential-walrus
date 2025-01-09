package co.touchlab.spotnotes.db

import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory {
    internal fun create(): SqlDriver
}

internal const val DATABASE_FILE_NAME = "global.db"
