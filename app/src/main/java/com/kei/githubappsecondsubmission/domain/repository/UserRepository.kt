package com.kei.githubappsecondsubmission.domain.repository

import android.util.Log
import com.kei.githubappsecondsubmission.domain.data.model.DetailUserResponse
import com.kei.githubappsecondsubmission.domain.data.model.UsersItem
import com.kei.githubappsecondsubmission.domain.data.network.ApiResult
import com.kei.githubappsecondsubmission.domain.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val apiService: ApiService){
    suspend fun getAllUser(): Flow<ApiResult<List<UsersItem?>?>>{
        return flow {
            try {
                val data = apiService.getListUser().items
                emit(ApiResult.Success(data))
            }catch (e: Throwable){
                emit(ApiResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getSearchUser(username : String) : Flow<ApiResult<List<UsersItem?>?>>{
        return flow {
            try {
                val data = apiService.getSearchUser(username)
                emit(ApiResult.Success(data.items))
            }catch (e: Throwable){
                emit(ApiResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getDetailUser(username: String): Flow<ApiResult<DetailUserResponse?>>{
        return flow {
            try {
                val data = apiService.getDetailUser(username)
                emit(ApiResult.Success(data))
            }catch (e: Throwable){
                emit(ApiResult.Error(e))
                Log.d("DEBUG", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getFollower(username: String): Flow<ApiResult<List<UsersItem?>?>>{
        return flow {
            try {
                val data = apiService.getFollowers(username)
                emit(ApiResult.Success(data))
            }catch (e: Throwable){
                emit(ApiResult.Error(e))
            }
        }
    }

    suspend fun getFollowing(username: String): Flow<ApiResult<List<UsersItem?>?>>{
        return flow {
            try {
                val data = apiService.getFollowing(username)
                emit(ApiResult.Success(data))
            }catch (e: Throwable){
                emit(ApiResult.Error(e))
            }
        }
    }
}