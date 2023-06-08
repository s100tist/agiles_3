package com.example.agiles_3

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

//Declaramos el Dao como propiedad en el constructor ya que no necesitamos toda la base de datos, solo el DAO
class ReceipeRepository(private val receipeDao: ReceipeDao) {
    //El room hace las peticiones a la db en hilos separados
    //Se ocupa un observador que necesita avisar que se cambiaron los datos

    val allRecipes: Flow<List<Receipe>> = receipeDao.getSortedId()

    //Por default el room suspende las peticiiones del hilo principal a menos que no se necesite implementar nada, para asegurar que no se esta
    //haciendo nada en segundo plano

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    //Modificar la de insertar
    suspend fun insert(receipe : Receipe){
        receipeDao.insert(receipe)
    }
}