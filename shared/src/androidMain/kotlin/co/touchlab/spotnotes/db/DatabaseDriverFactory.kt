package co.touchlab.spotnotes.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual class DatabaseDriverFactory(context: Context) {
    /**
     * Avoid memory leaks
     */
    private val context = context.applicationContext

    internal actual fun create(): SqlDriver =
        AndroidSqliteDriver(Database.Schema, context, DATABASE_FILE_NAME)
}
