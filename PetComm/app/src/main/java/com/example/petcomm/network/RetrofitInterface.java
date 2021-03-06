package com.example.petcomm.network;

import com.example.petcomm.model.Dog;
import com.example.petcomm.model.FeedSchedule;
import com.example.petcomm.model.HealthEat;
import com.example.petcomm.model.HealthPoop;
import com.example.petcomm.model.HealthWeight;
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

    @GET("health/eat/{feederId}")
    Observable<HealthEat[]> getHealthEatByFeederId(@Path("feederId") String feederId);
    @GET("health/weight/{toiletId}")
    Observable<HealthWeight[]> getHealthWeightByToiletId(@Path("toiletId") String toiletId);
    @GET("health/poop/{toiletId}")
    Observable<HealthPoop[]> getHealthPoopByToiletId(@Path("toiletId") String toiletId);

    @PUT("register/feeder/{dogId}")
    Observable<Res> registerFeeder(@Path("dogId") String dogId, @Body Dog dog);

    @PUT("register/toilet/{dogId}")
    Observable<Res> registerToilet(@Path("dogId") String dogId, @Body Dog dog);

    @PUT("unregister/feeder/{dogId}")
    Observable<Res> unregisterFeeder(@Path("dogId") String dogId, @Body Dog dog);

    @PUT("unregister/toilet/{dogId}")
    Observable<Res> unregisterToilet(@Path("dogId") String dogId, @Body Dog dog);


    @POST("schedule/delete")
    Observable<Res> removeSchedule(@Body Dog dog);

    @POST("schedule")
    Observable<Res> registerSchedule(@Body FeedSchedule feedSchedule);

    @GET("schedule/{feederId}")
    Observable<FeedSchedule[]> getFeedSchedule(@Path("feederId") String feederId);

    @POST("feed")
    Observable<Res> feedManually(@Body FeedSchedule feedSchedule);

    @POST("voice")
    Observable<Res> playVoice(@Body Dog dog);

    @POST("video")
    Observable<Res> cctvCamera();

    @GET("shutdown")
    Observable<Res> cctvCameraShutDown();

    @GET("video")
    Observable<String> cctvCameraGet();




}
