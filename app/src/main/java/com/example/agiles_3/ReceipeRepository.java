package com.example.agiles_3;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class ReceipeRepository {
    private ReceipeDao receipeDao;
    private LiveData<List<Receipe>> allReceipes;

    ReceipeRepository(Application application){
        ReceipeRoomDatabase db = ReceipeRoomDatabase.getDatabase(application);
        receipeDao = db.receipeDao();
        allReceipes = receipeDao.getAllReceipes();
    }

    LiveData<List<Receipe>> getAllReceipes (){
        return allReceipes;
    }

    void insert(Receipe receipe){
        ReceipeRoomDatabase.databaseWriteExecutor.execute(() -> {
            receipeDao.insert(receipe);
        });
    }
}
