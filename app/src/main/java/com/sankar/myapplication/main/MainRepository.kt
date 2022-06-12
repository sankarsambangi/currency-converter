package com.sankar.myapplication.main

import com.sankar.myapplication.util.Resource

interface MainRepository {

    suspend fun getRates(
        from: String,
        to: String,
        amount: String): Resource<ConvertResponse>
}