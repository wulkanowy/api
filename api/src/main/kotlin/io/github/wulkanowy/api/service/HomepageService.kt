package io.github.wulkanowy.api.service

import io.github.wulkanowy.api.ApiResponse
import io.github.wulkanowy.api.home.HomepageTileResponse
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface HomepageService {

    @GET("Start.mvc/Index")
    fun getStart(): Single<String>

    @FormUrlEncoded
    @POST("Start.mvc/GetSelfGovernments")
    fun getSelfGovernments(@Field("permissions") token: String): Single<ApiResponse<List<HomepageTileResponse>>>

    @FormUrlEncoded
    @POST("Start.mvc/GetStudentTrips")
    fun getStudentsTrips(@Field("permissions") token: String): Single<ApiResponse<List<HomepageTileResponse>>>

    @FormUrlEncoded
    @POST("Start.mvc/GetLastNotes")
    fun getLastGrades(@Field("permissions") token: String): Single<ApiResponse<List<HomepageTileResponse>>>

    @FormUrlEncoded
    @POST("Start.mvc/GetFreeDays")
    fun getFreeDays(@Field("permissions") token: String): Single<ApiResponse<List<HomepageTileResponse>>>

    @FormUrlEncoded
    @POST("Start.mvc/GetKidsLuckyNumbers")
    fun getKidsLuckyNumbers(@Field("permissions") token: String): Single<ApiResponse<List<HomepageTileResponse>>>

    @FormUrlEncoded
    @POST("Start.mvc/GetKidsLessonPlan")
    fun getKidsLessonPlan(@Field("permissions") token: String): Single<ApiResponse<List<HomepageTileResponse>>>

    @FormUrlEncoded
    @POST("Start.mvc/GetLastHomeworks")
    fun getLastHomework(@Field("permissions") token: String): Single<ApiResponse<List<HomepageTileResponse>>>

    @FormUrlEncoded
    @POST("Start.mvc/GetLastTests")
    fun getLastTests(@Field("permissions") token: String): Single<ApiResponse<List<HomepageTileResponse>>>

    @FormUrlEncoded
    @POST("Start.mvc/GetLastStudentLessons")
    fun getLastStudentLessons(@Field("permissions") token: String): Single<ApiResponse<List<HomepageTileResponse>>>
}
