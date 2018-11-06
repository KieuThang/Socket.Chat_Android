package com.github.kieuthang.login_chat.data.common;


import com.github.kieuthang.login_chat.data.entity.AccessToken;
import com.github.nkzawa.socketio.androidchat.Rooms;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("getRooms/")
    Call<Rooms> getRooms();

    @FormUrlEncoded
    @POST
    Call<AccessToken> login(@Field("email") String email, @Field("password") String password);
}
