package com.example.agiles_3;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ReceipeViewModel extends AndroidViewModel {
    private ReceipeRepository receipeRepository;

    private final LiveData<List<Receipe>> allReceipes;

    public ReceipeViewModel (Application application){
        super(application);
        receipeRepository = new ReceipeRepository(application);
        allReceipes = receipeRepository.getAllReceipes();
    }

    LiveData<List<Receipe>> getAllReceipes( ){
        return allReceipes;
    }

    public void insert(Receipe receipe){
        receipeRepository.insert(receipe);
    }
}
