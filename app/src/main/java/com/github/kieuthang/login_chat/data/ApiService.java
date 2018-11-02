package com.github.kieuthang.login_chat.data;


import com.github.nkzawa.socketio.androidchat.Rooms;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("getRooms/")
    Call<Rooms> getRooms();
}
