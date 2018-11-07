package com.github.kieuthang.login_chat.data.common;


import com.github.kieuthang.login_chat.data.entity.AccessTokenResponseModel;
import com.github.kieuthang.login_chat.data.entity.RoomResponseModel;
import com.github.kieuthang.login_chat.data.entity.RoomsResponseModel;
import com.github.kieuthang.login_chat.data.entity.UserModel;
import com.github.kieuthang.login_chat.data.entity.UserResponseModel;
import com.github.nkzawa.socketio.androidchat.Rooms;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @GET("getRooms/")
    Call<Rooms> getRooms();

    @FormUrlEncoded
    @POST("users/login")
    Call<AccessTokenResponseModel> login(@Field("email") String email, @Field("password") String password);

    @POST("users/register")
    Call<AccessTokenResponseModel> register(@Body UserModel userModel);

    @GET("users/getProfile")
    Call<UserResponseModel> getProfile(@Header("token") String token);

    @GET("rooms/getRooms")
    Call<RoomsResponseModel> getRooms(@Header("token") String token);

    @POST("rooms/addRoom")
    Call<RoomResponseModel> addRoom(@Header("token") String token, @Field("name") String name);
}
