package com.example.petcomm.network;

import com.example.petcomm.model.Dog;
import com.example.petcomm.model.Res;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

public interface RetrofitInterface {

    @POST("dog")
    Observable<Res> registerDog(@Body Dog dog);

    @GET("dogs/{userId}")
    Observable<Dog[]> getDogsById(@Path("userId") String email);

    @GET("dog/{dogId}")
    Observable<Dog> getDogByDogId(@Path("dogId") String dogId);


}
