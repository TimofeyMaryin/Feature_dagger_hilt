package com.example.fifteenfebuaryproject

import androidx.lifecycle.ViewModel
import com.example.fifteenfebuaryproject.domain.repository.MyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject


@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: dagger.Lazy<MyRepository>
): ViewModel() {

    init {
        repository.get()
    }

}