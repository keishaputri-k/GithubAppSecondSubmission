package com.kei.githubappsecondsubmission.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kei.githubappsecondsubmission.domain.data.model.ItemsItem
import com.kei.githubappsecondsubmission.domain.data.network.ApiResult
import com.kei.githubappsecondsubmission.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    //pemisahan antara variable yang bisa di consume oleh class sendiri dan lainnya
    private val _listUserLiveData = MutableLiveData<List<ItemsItem?>?>()
    val listUserLiveData get() = _listUserLiveData

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading get() = _loading

    private val _error: MutableLiveData<Throwable?> = MutableLiveData()
    val error get() = _error

    //ini untuk connect ke repository
    fun getAllUserData() {
        viewModelScope.launch {
            userRepository.getAllUser().onStart {
                _loading.value = true
            }.onCompletion {
                _loading.value = false
            }.collect {
                when (it) {
                    is ApiResult.Success -> {
                        _listUserLiveData.postValue(it.data)
                    }
                    is ApiResult.Error -> {
                        _error.postValue(it.throwable)
                    }
                }
            }
        }
    }

    fun getSearchUser(username: String) {
        if (username == "") {
            getAllUserData()
        } else {
            viewModelScope.launch {
                userRepository.getSearchUser(username).onStart {
                    _loading.value = true
                }.onCompletion {
                    _loading.value = false
                }.collect {
                    when(it){
                       is ApiResult.Success -> {
                           _error.postValue(null)
                           _listUserLiveData.postValue(it.data)
                       }
                        is ApiResult.Error -> _error.postValue(it.throwable)
                    }
                }
            }
        }
    }

    init {
        getAllUserData()
    }
}