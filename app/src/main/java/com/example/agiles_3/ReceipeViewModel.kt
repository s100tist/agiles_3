package com.example.agiles_3

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class ReceipeViewModel(private val repository: ReceipeRepository):ViewModel() {
    val allReceipes: LiveData<List<Receipe>> = repository.allRecipes.asLiveData()

    fun insert(receipe: Receipe) = viewModelScope.launch{
        //repository.insert(receipe)
    }

}

//class ReceipeViewModelFactory(private val repository: ReceipeRepository) : ViewModelProvider.Factory{
//    override fun <T : ViewModel> create(modelClass: Class<T>) :T{
//        if(modelClass.isAssignableFrom(ReceipeViewModel::class.java)){
//            @Suppress("UNCHECKED_CAST")
//            return ReceipeViewModel(repository) as T
//        }
//        throw IllegalArgumentException("Casteo del ViewModel desconocido")

//    }
//}