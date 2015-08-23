package com.vetcon.sendnow.interfaces;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Michael Yoon Huh on 8/23/2015.
 */
public interface SimplifyInterface {

    @POST("/api/fund")
    void sendToken(@Body String token, double amount, String receiverEmail, Callback<String> callback);
}
