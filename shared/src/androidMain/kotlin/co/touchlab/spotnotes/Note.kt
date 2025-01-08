package co.touchlab.spotnotes

import java.util.UUID

actual fun buildNote(note: String, distance: Long, latLong: Pair<Double, Double>?): Note =
    NoteImpl(UUID.randomUUID().toString(), note, distance, latLong)
