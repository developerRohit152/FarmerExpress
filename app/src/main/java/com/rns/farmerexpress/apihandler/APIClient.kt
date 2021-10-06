package com.rns.farmerexpress.apihandler

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder

import com.google.gson.Gson





class APIClient {
    companion object {
        private const val BASE_URL = "http://farmerexpress.rnsitsolutions.com/api/"
        private const val BASE_URL_NEWS = "http://khabarexpo.in/admin/"
        private const val BASE_URL_MANDI  = "https://api.data.gov.in/resource/"
        private const val BASE_URL_PAYMENT = "https://www.kwikapi.com/api/v2/"
        private var retrofit: Retrofit? = null
        private var retrofit_pay: Retrofit? = null
        private var retrofit_Mandi: Retrofit? = null
        fun getClient() : Retrofit? {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
        fun getPayment() : Retrofit? {
            if (retrofit_pay == null) {
                retrofit_pay = Retrofit.Builder()
                    .baseUrl(BASE_URL_PAYMENT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit_pay
        }
        fun getMandi() : Retrofit? {
            if (retrofit_Mandi == null) {
                retrofit_Mandi = Retrofit.Builder()
                    .baseUrl(BASE_URL_MANDI)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit_Mandi
        }

        var gson: Gson = GsonBuilder()
            .setLenient()
            .create()
        private var retrofitNews: Retrofit? = null
        fun getNewsClient() : Retrofit? {
            if (retrofitNews == null) {
                retrofitNews = Retrofit.Builder()
                    .baseUrl(BASE_URL_NEWS)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return retrofitNews
        }
    }
}