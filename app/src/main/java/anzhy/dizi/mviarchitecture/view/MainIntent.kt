package anzhy.dizi.mviarchitecture.view

sealed class MainIntent {

    data object FetchAnimals : MainIntent()

}