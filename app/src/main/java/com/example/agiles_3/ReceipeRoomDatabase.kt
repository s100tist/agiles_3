package com.example.agiles_3

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Receipe::class], version = 1, exportSchema = false)

public abstract class ReceipeRoomDatabase : RoomDatabase() {
    abstract fun receipeDao(): ReceipeDao

    companion object{
        @Volatile
        private var INSTANCE : ReceipeRoomDatabase? = null

        fun getDatabase(context: Context): ReceipeRoomDatabase{
            //Si la instancia no es nula regresara la misma instancia si no va a crear la base de datos
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ReceipeRoomDatabase::class.java,
                    "receipe_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}