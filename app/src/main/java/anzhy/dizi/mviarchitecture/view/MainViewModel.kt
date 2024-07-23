package anzhy.dizi.mviarchitecture.view

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import anzhy.dizi.mviarchitecture.api.AnimalRepo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repo: AnimalRepo
) : ViewModel() {

    val userIntent = Channel<MainIntent>(Channel.UNLIMITED)
    var state = mutableStateOf<MainState>(MainState.Idle)
        private set // This is to prevent setting state from outside the ViewModel

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect { collector ->
                when (collector) {
                    is MainIntent.FetchAnimals -> {
                        fetchAnimals()
                    }
                }
            }
        }
    }

    private suspend fun fetchAnimals() {
        viewModelScope.launch {
            state.value = MainState.Loading
            state.value = try {
                MainState.Success(repo.getAnimals())
            } catch (e: Exception) {
                MainState.Error(e.localizedMessage)
            }
        }
    }
}