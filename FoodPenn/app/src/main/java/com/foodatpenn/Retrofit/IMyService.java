package com.foodatpenn.Retrofit;

import io.reactivex.Observable;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IMyService {
    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("email") String email,
                                    @Field("name") String name,
                                    @Field("password") String password,
                                    @Field("year") String year,
                                    @Field("phone") String phone);

    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email") String email,
                                    @Field("password") String name);

    @POST("contains")
    @FormUrlEncoded
    Observable<String> contains(@Field("email") String email);

    @POST("getUser")
    @FormUrlEncoded
    Observable<String> getUser(@Field("email") String email);

    @POST("modify")
    @FormUrlEncoded
    Observable<String> modifyUser(@Field("email") String email,
                                    @Field("name") String name,
                                    @Field("year") String year,
                                    @Field("phone") String phone);

    @POST("users")
    @FormUrlEncoded
    Observable<String> allUsers(@Field("email") String email);

    @POST("addRating")
    @FormUrlEncoded
    Observable<String> addRating(@Field("email") String email,
                                 @Field("rating") int rating);

    @POST("createPost")
    @FormUrlEncoded
    Observable<String> createPost(
                                    @Field("date") String date,
                                    @Field("food") String food,
                                    @Field("description") String description,
                                    @Field("id") String id,
                                    @Field("location") String location);

    @POST("modifyPost")
    @FormUrlEncoded
    Observable<String> modifyPost(@Field("id") String id,
                                  @Field("food") String food,
                                  @Field("description") String description,
                                  @Field("location") String location);

    @POST("deletePost")
    @FormUrlEncoded
    Observable<String> deletePost(@Field("id") String id);


}
