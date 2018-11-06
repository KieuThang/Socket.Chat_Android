package com.github.kieuthang.login_chat.data

import android.content.Context
import com.github.kieuthang.login_chat.common.AppConstants
import com.github.kieuthang.login_chat.common.log.AppLog
import com.github.kieuthang.login_chat.common.utils.ApplicationUtils
import com.github.kieuthang.login_chat.data.common.ApiService
import com.github.kieuthang.login_chat.data.common.BaseRepositoryImpl
import com.github.kieuthang.login_chat.data.common.RestApiClient
import com.github.kieuthang.login_chat.data.common.cache.DataCacheApiImpl
import com.github.kieuthang.login_chat.data.entity.AccessToken
import com.github.kieuthang.login_chat.data.entity.LoginRequest
import com.github.kieuthang.login_chat.data.entity.UserModel
import com.github.kieuthang.login_chat.views.common.IDataRepository
import com.google.gson.Gson
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DataRepositoryImpl(context: Context) : BaseRepositoryImpl(context), IDataRepository {

    override fun register(firstName: String, lastName: String, email: String, password: String): Observable<AccessToken> {
        return Observable.create { subscriber ->
            val apiService = RestApiClient.getClient().create(ApiService::class.java)

            val request = UserModel()
            request.email = email
            request.password = password
            request.firstName = firstName
            request.lastName = lastName
            AppLog.d(AppConstants.TAG, "register START=> email:$email,password:$password")
            val call = apiService.register(request)
            call.enqueue(object : Callback<AccessToken> {
                override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                    val result = response.body()
                    if (result == null) {
                        subscriber.onError(Throwable())
                        return
                    }
                    val resultJson = ApplicationUtils.makeJsonObject(result)
                    AppLog.d(AppConstants.TAG, "register success=>: $resultJson")

                    val iDataCacheApi = DataCacheApiImpl(mContext)

                    val newResultJson = ApplicationUtils.makeJsonObject(resultJson)
                    iDataCacheApi.saveDataToCache(AppConstants.Cache.ACCESS_TOKEN, newResultJson)
                    subscriber.onNext(result)
                    subscriber.onComplete()
                }

                override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                    AppLog.d(AppConstants.TAG, "register onFailure: " + t.message)
                    subscriber.onError(Throwable())
                }
            })
        }
    }

    override fun getMyProfile(pullToRefresh: Boolean): Observable<UserModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun login(email: String, password: String): Observable<AccessToken> {

        return Observable.create { subscriber ->
            val apiService = RestApiClient.getClient().create(ApiService::class.java)
            AppLog.d(AppConstants.TAG, "login START=> email:$email,password:$password")
            val request = LoginRequest()
            request.email = email
            request.password = password
            val call = apiService.login(email, password)
            call.enqueue(object : Callback<AccessToken> {
                override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                    val result = response.body()
                    if (result == null) {
                        subscriber.onError(Throwable())
                        return
                    }
                    val resultJson = ApplicationUtils.makeJsonObject(result)
                    AppLog.d(AppConstants.TAG, "login success=>: $resultJson")

                    val newAccessToken = Gson().fromJson<AccessToken>(resultJson, AccessToken::class.java)
                    val iDataCacheApi = DataCacheApiImpl(mContext)

                    val newResultJson = ApplicationUtils.makeJsonObject(newAccessToken)
                    iDataCacheApi.saveDataToCache(AppConstants.Cache.ACCESS_TOKEN, newResultJson)

                    subscriber.onNext(newAccessToken)
                    subscriber.onComplete()
                }

                override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                    AppLog.d(AppConstants.TAG, "login onFailure: " + t.message)
                    subscriber.onError(Throwable())
                }
            })
        }

    }

}