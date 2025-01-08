package co.touchlab.spotnotes

interface Note {
    val id: String
    val note: String
    val distance: Long
    val latLong: Pair<Double, Double>?
}

/**
 * Workaround for data classes exposing the constructor regardless of its visibility via copy() - if
 * I don't do the interface, I can't prevent callers from instantiating the class by using copy()
 * instead of the intended way
 */
internal data class NoteImpl(
    override val id: String,
    override val note: String,
    override val distance: Long,
    override val latLong: Pair<Double, Double>?
) : Note

expect fun buildNote(note: String, distance: Long, latLong: Pair<Double, Double>?): Note
