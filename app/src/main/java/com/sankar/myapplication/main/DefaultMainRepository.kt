package com.sankar.myapplication.main

import com.sankar.myapplication.data.models.CurrencyApi
import com.sankar.myapplication.util.Resource
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val api: CurrencyApi
): MainRepository {
    override suspend fun getRates(
        from: String,
        to: String,
        amount: String): Resource<ConvertResponse> {
        return try {
            val response = api.getRates(from, to, amount)
            val result = response.body()

            if(response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occured!")
        }
    }
}