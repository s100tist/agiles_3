package com.example.agiles_3

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceipeDao {
    @Query("SELECT * FROM receta_tabla ORDER BY id ASC")
    fun getSortedId(): Flow<List<Receipe>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(receipe: Receipe)
}