package com.kei.githubappsecondsubmission.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kei.githubappsecondsubmission.domainNetwork.data.model.UsersItem
import com.kei.githubappsecondsubmission.domainNetwork.data.network.ApiResult
import com.kei.githubappsecondsubmission.domainNetwork.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    //pemisahan antara variable yang bisa di consume oleh class sendiri dan lainnya
    private val _listUserLiveData = MutableLiveData<List<UsersItem?>?>()
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