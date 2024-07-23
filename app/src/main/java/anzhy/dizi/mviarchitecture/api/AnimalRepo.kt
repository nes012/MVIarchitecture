package anzhy.dizi.mviarchitecture.api

class AnimalRepo(private val api: AnimalApi) {

    suspend fun getAnimals() = api.getAnimals()

}