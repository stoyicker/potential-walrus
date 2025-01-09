package co.touchlab.spotnotes.db

class DatabaseTableProxy private constructor(database: Database) {
    val note = NoteTable(database)

    class Factory {
        fun create(databaseDriverFactory: DatabaseDriverFactory) =
            DatabaseTableProxy(Database(databaseDriverFactory.create()))
    }
}
