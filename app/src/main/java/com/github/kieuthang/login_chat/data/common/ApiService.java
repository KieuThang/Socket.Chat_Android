package com.github.kieuthang.login_chat.data.common;


import com.github.kieuthang.login_chat.data.entity.AccessToken;
import com.github.kieuthang.login_chat.data.entity.BaseResponseModel;
import com.github.kieuthang.login_chat.data.entity.UserModel;
import com.github.nkzawa.socketio.androidchat.Rooms;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("getRooms/")
    Call<Rooms> getRooms();

    @FormUrlEncoded
    @POST("users/login")
    Call<AccessToken> login(@Field("email") String email, @Field("password") String password);

    @POST("users/register")
    Call<AccessToken> register(@Body UserModel userModel);
}
