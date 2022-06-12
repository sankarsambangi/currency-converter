package com.sankar.myapplication.data.models

data class ConvertResponse(
    val info: Info,
    val query: Query,
    val result: Double,
    val success: Boolean
)