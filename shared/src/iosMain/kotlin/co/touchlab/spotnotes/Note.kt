package co.touchlab.spotnotes

import platform.Foundation.NSUUID

actual fun buildNote(note: String, distance: Long, latLong: Pair<Double, Double>?): Note =
    NoteImpl(NSUUID().UUIDString(), note, distance, latLong)
