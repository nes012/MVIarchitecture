package anzhy.dizi.mviarchitecture.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import anzhy.dizi.mviarchitecture.api.AnimalApi
import anzhy.dizi.mviarchitecture.api.AnimalRepo

class MainViewModelFactory(
    private val api: AnimalApi
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(AnimalRepo(api)) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}