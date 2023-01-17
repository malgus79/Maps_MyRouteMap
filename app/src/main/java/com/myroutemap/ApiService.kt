package com.myroutemap

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

// https://api.openrouteservice.org/v2/
// directions/driving-car
// ?
// api_key=5b3ce3597851110001cf6248b568bcde1a2445dc93decd70c099178b
// &
// start=8.681495,49.41461
// &
// end=8.687872,49.420318

    @GET("directions/driving-car")
    fun getRoute(
        @Query("api_key") key: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): Response<*>
}