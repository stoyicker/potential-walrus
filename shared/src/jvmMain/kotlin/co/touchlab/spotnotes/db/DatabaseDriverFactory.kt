package co.touchlab.spotnotes.db

import app.cash.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory {
    internal actual fun create(): SqlDriver = throw UnsupportedOperationException("Stub")
}
