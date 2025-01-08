package co.touchlab.spotnotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UIState(val notes: List<Note>)

class MainViewModel(
    private val locationManager: LocationManager,
    private val distanceCalculator: DistanceCalculator
) : ViewModel() {
    private val _uiState = MutableStateFlow(UIState(emptyList()))
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    fun saveNote(text: String) {
        viewModelScope.launch {
            _uiState.update {
                val latLong = locationManager.getLocation()?.run { latitude to longitude }
                val distance = distanceCalculator.between(latLong, it.notes.lastOrNull()?.latLong)
                val list = it.notes.toMutableList()
                list.add(buildNote(text, distance, latLong))
                it.copy(notes = list)
            }
        }
    }
}