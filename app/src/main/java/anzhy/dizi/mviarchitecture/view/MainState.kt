package anzhy.dizi.mviarchitecture.view

import anzhy.dizi.mviarchitecture.model.Animal

sealed class MainState {

    data object Idle : MainState()

    data object Loading : MainState()

    data class Success(val animalList: List<Animal>) : MainState()

    data class Error(val error: String?) : MainState()
}