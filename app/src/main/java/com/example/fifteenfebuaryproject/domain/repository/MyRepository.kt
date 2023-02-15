package com.example.fifteenfebuaryproject.domain.repository

import retrofit2.http.GET

interface MyRepository {

    suspend fun doNetworkCall()

}