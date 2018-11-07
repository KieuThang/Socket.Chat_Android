package com.github.kieuthang.login_chat.data

import android.content.Context
import com.github.kieuthang.login_chat.common.AppConstants
import com.github.kieuthang.login_chat.common.log.AppLog
import com.github.kieuthang.login_chat.common.utils.ApplicationUtils
import com.github.kieuthang.login_chat.data.common.ApiService
import com.github.kieuthang.login_chat.data.common.BaseRepositoryImpl
import com.github.kieuthang.login_chat.data.common.RestApiClient
import com.github.kieuthang.login_chat.data.common.cache.DataCacheApiImpl
import com.github.kieuthang.login_chat.data.entity.*
import com.github.kieuthang.login_chat.views.common.IDataRepository
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DataRepositoryImpl(context: Context) : BaseRepositoryImpl(context), IDataRepository {
    override fun getRooms(): Observable<RoomsResponseModel> {
        return Observable.create { subscriber ->
            val apiService = RestApiClient.getClient().create(ApiService::class.java)

            val iDataCacheApi = DataCacheApiImpl(mContext)
            val accessToken = iDataCacheApi.getAccessToken()
            if (accessToken == null) {
                subscriber.onError(Throwable())
                return@create
            }
            AppLog.d(AppConstants.TAG, "getRooms START=> token:${accessToken.token}")
            val call = apiService.getRooms(accessToken.token)
            call.enqueue(object : Callback<RoomsResponseModel> {
                override fun onResponse(call: Call<RoomsResponseModel>, response: Response<RoomsResponseModel>) {
                    val result = response.body()
                    if (result == null) {
                        subscriber.onError(Throwable())
                        return
                    }
                    val resultJson = ApplicationUtils.makeJsonObject(result)
                    AppLog.d(AppConstants.TAG, "getRooms success=>: $resultJson")

                    subscriber.onNext(result)
                    subscriber.onComplete()
                }

                override fun onFailure(call: Call<RoomsResponseModel>, t: Throwable) {
                    AppLog.d(AppConstants.TAG, "getRooms onFailure: " + t.message)
                    subscriber.onError(Throwable())
                }
            })
        }

    }

    override fun addRoom(name: String): Observable<RoomResponseModel> {
        return Observable.create { subscriber ->
            val apiService = RestApiClient.getClient().create(ApiService::class.java)
            val iDataCacheApi = DataCacheApiImpl(mContext)
            val accessToken = iDataCacheApi.getAccessToken()
            if (accessToken == null) {
                subscriber.onError(Throwable())
                return@create
            }
            AppLog.d(AppConstants.TAG, "addRoom START=> name:$name")

            val call = apiService.addRoom(accessToken.token, name)
            call.enqueue(object : Callback<RoomResponseModel> {
                override fun onResponse(call: Call<RoomResponseModel>, response: Response<RoomResponseModel>) {
                    val result = response.body()
                    if (result == null) {
                        subscriber.onError(Throwable())
                        return
                    }
                    val resultJson = ApplicationUtils.makeJsonObject(result)
                    AppLog.d(AppConstants.TAG, "addRoom success=>: $resultJson")

                    subscriber.onNext(result)
                    subscriber.onComplete()
                }

                override fun onFailure(call: Call<RoomResponseModel>, t: Throwable) {
                    AppLog.d(AppConstants.TAG, "addRoom onFailure: " + t.message)
                    subscriber.onError(Throwable())
                }
            })
        }

    }

    override fun register(firstName: String, lastName: String, email: String, password: String): Observable<AccessTokenResponseModel> {
        return Observable.create { subscriber ->
            val apiService = RestApiClient.getClient().create(ApiService::class.java)

            val request = UserModel()
            request.email = email
            request.password = password
            request.firstName = firstName
            request.lastName = lastName
            AppLog.d(AppConstants.TAG, "register START=> email:$email,password:$password")
            val call = apiService.register(request)
            call.enqueue(object : Callback<AccessTokenResponseModel> {
                override fun onResponse(call: Call<AccessTokenResponseModel>, response: Response<AccessTokenResponseModel>) {
                    val result = response.body()
                    if (result?.accessToken == null) {
                        subscriber.onError(Throwable())
                        return
                    }
                    val resultJson = ApplicationUtils.makeJsonObject(result.accessToken!!)
                    AppLog.d(AppConstants.TAG, "register success=>: $resultJson")

                    val iDataCacheApi = DataCacheApiImpl(mContext)

                    val newResultJson = ApplicationUtils.makeJsonObject(resultJson)
                    iDataCacheApi.saveDataToCache(AppConstants.Cache.ACCESS_TOKEN, newResultJson)
                    subscriber.onNext(result)
                    subscriber.onComplete()
                }

                override fun onFailure(call: Call<AccessTokenResponseModel>, t: Throwable) {
                    AppLog.d(AppConstants.TAG, "register onFailure: " + t.message)
                    subscriber.onError(Throwable())
                }
            })
        }
    }

    override fun getMyProfile(pullToRefresh: Boolean): Observable<UserResponseModel> {
        return Observable.create { subscriber ->
            val apiService = RestApiClient.getClient().create(ApiService::class.java)
            val iDataCacheApi = DataCacheApiImpl(mContext)
            val accessToken = iDataCacheApi.getAccessToken()
            if (accessToken == null) {
                subscriber.onError(Throwable())
                return@create
            }
            AppLog.d(AppConstants.TAG, "getMyProfile START=> token:${accessToken.token}")
            val userModel = iDataCacheApi.getUserModel()
            if (!pullToRefresh || userModel != null) {
                val userResponseModel = UserResponseModel()
                userResponseModel.userModel = userModel
                userResponseModel.code = AppConstants.APICodeResponse.SUCCESS
                subscriber.onNext(userResponseModel)
                subscriber.onComplete()
                return@create
            }
            val call = apiService.getProfile(accessToken.token)
            call.enqueue(object : Callback<UserResponseModel> {
                override fun onResponse(call: Call<UserResponseModel>, response: Response<UserResponseModel>) {
                    val result = response.body()
                    if (result?.userModel == null) {
                        subscriber.onError(Throwable())
                        return
                    }
                    val resultJson = ApplicationUtils.makeJsonObject(result.userModel!!)
                    AppLog.d(AppConstants.TAG, "getMyProfile success=>: $resultJson")
                    iDataCacheApi.saveDataToCache(AppConstants.Cache.USER_MODEL, resultJson)

                    subscriber.onNext(result)
                    subscriber.onComplete()
                }

                override fun onFailure(call: Call<UserResponseModel>, t: Throwable) {
                    AppLog.d(AppConstants.TAG, "getMyProfile onFailure: " + t.message)
                    subscriber.onError(Throwable())
                }
            })
        }
    }

    override fun login(email: String, password: String): Observable<AccessTokenResponseModel> {

        return Observable.create { subscriber ->
            val apiService = RestApiClient.getClient().create(ApiService::class.java)
            AppLog.d(AppConstants.TAG, "login START=> email:$email,password:$password")
            val request = LoginRequest()
            request.email = email
            request.password = password
            val call = apiService.login(email, password)
            call.enqueue(object : Callback<AccessTokenResponseModel> {
                override fun onResponse(call: Call<AccessTokenResponseModel>, response: Response<AccessTokenResponseModel>) {
                    val result = response.body()
                    if (result?.accessToken == null) {
                        subscriber.onError(Throwable())
                        return
                    }
                    val resultJson = ApplicationUtils.makeJsonObject(result.accessToken!!)
                    AppLog.d(AppConstants.TAG, "login success=>: $resultJson")
                    val iDataCacheApi = DataCacheApiImpl(mContext)
                    iDataCacheApi.saveDataToCache(AppConstants.Cache.ACCESS_TOKEN, resultJson)

                    subscriber.onNext(result)
                    subscriber.onComplete()
                }

                override fun onFailure(call: Call<AccessTokenResponseModel>, t: Throwable) {
                    AppLog.d(AppConstants.TAG, "login onFailure: " + t.message)
                    subscriber.onError(Throwable())
                }
            })
        }

    }

}