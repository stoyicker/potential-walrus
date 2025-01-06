package co.touchlab.spotnotes

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToLong

data class UIState(val notes: List<SpotNote>)

data class SpotNote( val note: String, val distance: Long, val location: Location?)

class MainViewModel(private val locationManager: LocationManager) : ViewModel() {
    private val _uiState = MutableStateFlow(UIState(emptyList()))
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    fun saveNote(text: String) {
        viewModelScope.launch {
            _uiState.update {
                val location = locationManager.getLocation()
                val distance = locationManager.calculateDistance(location, it.notes.lastOrNull()?.location)
                val list = it.notes.toMutableList()
                list.add(SpotNote(text, distance.roundToLong(), location))
                it.copy(notes = list)
            }
        }
    }
}