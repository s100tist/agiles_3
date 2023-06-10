package com.example.agiles_3

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "receta_tabla")
data class Receipe(
    @PrimaryKey(autoGenerate = true) val id: Int,
    //Poner los demas vvalores del json
)