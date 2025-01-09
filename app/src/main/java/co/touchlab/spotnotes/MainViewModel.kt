package co.touchlab.spotnotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.spotnotes.note.Note
import co.touchlab.spotnotes.note.NoteFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UIState(val notes: List<Note>)

class MainViewModel(private val noteFactory: NoteFactory) : ViewModel() {
    private val _uiState = MutableStateFlow(UIState(emptyList()))
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    fun saveNote(text: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(it.notes + noteFactory.create(text, it.notes.lastOrNull()?.latLong))
            }
        }
    }
}