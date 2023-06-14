package com.example.agiles_3;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Receipe.class}, version = 1, exportSchema = false)
public abstract class ReceipeRoomDatabase extends RoomDatabase {
    public abstract ReceipeDao receipeDao();

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);

            //Cada que se reinicia la app se resetea
            databaseWriteExecutor.execute(() -> {
                ReceipeDao dao = INSTANCE.receipeDao();

                Receipe receipe = new Receipe(
                        "ajiji",
                        2.3,
                        233,
                        23.3,
                        232.32,
                        4343.423,
                        2342.234,
                        423.423,
                        2342.4312,
                        423.4321,
                        4234.4321,
                        2342.4321,
                        423.3241);
                dao.insert(receipe);
            });
        }
    };

    private static volatile ReceipeRoomDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ReceipeRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (ReceipeRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ReceipeRoomDatabase.class, "recetas_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
