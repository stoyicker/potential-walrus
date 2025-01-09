package co.touchlab.spotnotes.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import co.touchlab.spotnotes.note.Note

@Composable
fun HistoryView(notes: List<Note>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(notes) {
            Row {
                ListItem(headlineContent = { Text(text = it.note) },
                    trailingContent = { Text(text = "${it.distance.toString()} m") })
            }
        }
    }
}

@Composable
@Preview
fun Preview_HistoryView() {
    HistoryView(
        listOf(
            object : Note {
                override val id = ""
                override val note = "Test"
                override val distance = 9L
                override val latLong = null
            },
            object : Note {
                override val id = ""
                override val note = "Test 2"
                override val distance = 500L
                override val latLong = null
            }
        )
    )
}