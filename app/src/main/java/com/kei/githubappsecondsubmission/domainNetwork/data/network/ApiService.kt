package com.kei.githubappsecondsubmission.domainNetwork.data.network

import com.kei.githubappsecondsubmission.BuildConfig
import com.kei.githubappsecondsubmission.domainNetwork.data.model.DetailUserResponse
import com.kei.githubappsecondsubmission.domainNetwork.data.model.ResponseUser
import com.kei.githubappsecondsubmission.domainNetwork.data.model.UsersItem
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users?q=followers%3A>%3D1000&ref=searchresults&s=followers&type=Users")
    @Headers("Authorization: token ${BuildConfig.KEY}")
    suspend fun getListUser(): ResponseUser

    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.KEY}")
    suspend fun getSearchUser(@Query("q") username : String) : ResponseUser

    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.KEY}")
    suspend fun getDetailUser(@Path("username")username: String) : DetailUserResponse

    @GET("users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.KEY}")
    suspend fun getFollowers(@Path("username")username: String) : List<UsersItem>

    @GET("users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.KEY}")
    suspend fun getFollowing(@Path("username")username: String) : List<UsersItem>
}