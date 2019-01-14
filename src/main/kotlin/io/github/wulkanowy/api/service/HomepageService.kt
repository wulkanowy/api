package io.github.wulkanowy.api.service

import io.github.wulkanowy.api.lucky_number.LuckyNumberResponse
import io.reactivex.Single
import retrofit2.http.GET

interface HomepageService {

    @GET("Start.mvc/Index")
    fun getLuckyNumber(): Single<LuckyNumberResponse>

}