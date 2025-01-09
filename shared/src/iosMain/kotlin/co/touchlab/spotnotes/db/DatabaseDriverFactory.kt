package co.touchlab.spotnotes.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    internal actual fun create(): SqlDriver =
        NativeSqliteDriver(Database.Schema, DATABASE_FILE_NAME)
}
