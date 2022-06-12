package com.sankar.myapplication.data.models

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface CurrencyApi {

    @Headers("apikey: 6fwQqeuuJ4pmAFQsQOFyITN4ra1Q8M7S")
    @GET("currency_data/convert")
    suspend fun getRates(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: String
    ): Response<ConvertResponse>
}