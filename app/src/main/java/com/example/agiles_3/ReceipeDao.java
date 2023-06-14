package com.example.agiles_3;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ReceipeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Receipe receipe);

    @Query("SELECT * FROM recetas_tabla ORDER BY calories ASC")
    LiveData<List<Receipe>> getAllReceipes();
}
