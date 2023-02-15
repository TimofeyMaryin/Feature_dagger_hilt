package com.example.fifteenfebuaryproject.data.repository

import android.app.Application
import com.example.fifteenfebuaryproject.R
import com.example.fifteenfebuaryproject.data.remote.MyApi
import com.example.fifteenfebuaryproject.domain.repository.MyRepository
import javax.inject.Inject

class MyRepositoryImpl @Inject constructor(
    private val api: MyApi,
    private val application: Application
): MyRepository {

    init {
        val appName = application.getString(R.string.app_name)
    }

    override suspend fun doNetworkCall() {
        api.doNetworkCall()
    }


}