package co.touchlab.spotnotes.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import co.touchlab.spotnotes.note.Note
import co.touchlab.spotnotes.note.NoteImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class NoteTable internal constructor(private val database: Database) {
    /**
     * Keeping persisting notes separate from creating them allows callers to rapidly update their
     * UIs without having to write for a disk write. Then, if the write fails, they can react by
     * removing the note that they added on the supposition that the write would work.
     */
    suspend fun insert(note: Note) = withContext(Dispatchers.IO) {
        with(note) {
            database.noteQueries
                .insert(id, this.note, distance, latLong?.first, latLong?.second)
        }
    }

    suspend fun selectAll(): List<Note> =
        database.noteQueries
            .selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .first()
            .map {
                it.run {
                    NoteImpl(
                        id,
                        text,
                        distance,
                        if (latitude == null || longitude == null) {
                            null
                        } else {
                            latitude to longitude
                        }
                    )
                }
            }
}
