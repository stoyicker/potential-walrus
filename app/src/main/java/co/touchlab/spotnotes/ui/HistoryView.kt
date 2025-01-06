package co.touchlab.spotnotes.ui

import android.location.Location
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import co.touchlab.spotnotes.SpotNote

@Composable
fun HistoryView(notes: List<SpotNote>) {
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
    HistoryView(listOf(SpotNote("Test", 0, Location("")), SpotNote("Test 2", 500, Location(""))))
}