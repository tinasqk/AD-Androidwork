package com.example.nus.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    // 可能的服务器地址列表
    private val POSSIBLE_BASE_URLS = listOf(
        "http://10.0.2.2:8080/",      // Android模拟器访问本地服务器
        "http://192.168.1.100:8080/", // 替换为您的实际IP地址
        "http://localhost:8080/",     // 直接localhost（可能不工作）
    )

    // 当前使用的BASE_URL - 请替换为您的实际IP地址
    // 如果10.0.2.2不工作，请尝试您的实际IP地址，例如：
    // private const val BASE_URL = "http://192.168.1.100:8080/"
    private const val BASE_URL = "http://10.0.2.2:8080/"
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(10, TimeUnit.SECONDS)  // 减少连接超时时间
        .readTimeout(15, TimeUnit.SECONDS)     // 减少读取超时时间
        .writeTimeout(15, TimeUnit.SECONDS)    // 减少写入超时时间
        .retryOnConnectionFailure(true)        // 启用连接失败重试
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val userApiService: UserApiService = retrofit.create(UserApiService::class.java)
}
