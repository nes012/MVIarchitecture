package anzhy.dizi.mviarchitecture.api

import anzhy.dizi.mviarchitecture.model.Animal
import retrofit2.http.GET

interface AnimalApi {

    @GET("animals.json")
    suspend fun getAnimals(): List<Animal>

}