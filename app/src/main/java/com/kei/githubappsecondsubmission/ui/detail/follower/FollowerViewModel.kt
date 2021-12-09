package com.kei.githubappsecondsubmission.ui.detail.follower

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
class FollowerViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private var strUserName: String = ""

    private val _followersLiveData = MutableLiveData<List<UsersItem?>?>()
    val followerLiveData get() = _followersLiveData

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading get() = _loading

    private val _error: MutableLiveData<Throwable?> = MutableLiveData()
    val error get() = _error

    fun getFollowers(username: String) {
        if (strUserName != username) {
            viewModelScope.launch {
                strUserName = username
                userRepository.getFollower(username).onStart {
                    _loading.value = true
                }.onCompletion {
                    _loading.value = false
                }.collect {
                    when (it) {
                        is ApiResult.Success -> {
                            _error.postValue(null)
                            _followersLiveData.postValue(it.data)
                        }
                        is ApiResult.Error -> {
                            _error.postValue(it.throwable)
                        }
                    }
                }
            }
        }
    }
}