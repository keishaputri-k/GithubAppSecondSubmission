package com.kei.githubappsecondsubmission.domainNetwork.data.network
// R -> result keseluruhan | Berhasil
//T -> data | gagal
open class ApiResult<out R> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val throwable: Throwable) : ApiResult<Nothing>()
}
