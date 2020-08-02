package by.hackathon.drinder.api

import by.hackathon.drinder.data.LocationInfo
import by.hackathon.drinder.data.LoginInfo
import by.hackathon.drinder.data.UserInfo
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DrinderApi {
    @GET(LOGIN_ENDPOINT)
    suspend fun login(
        @Query(value = LOGIN) login: String,
        @Query(value = PASSWORD) password: String
    ): Response<LoginInfo>

    @POST(REGISTER_ENDPOINT)
    suspend fun register(
        @Query(value = LOGIN) login: String,
        @Query(value = PASSWORD) password: String
    ): Response<LoginInfo>

    @GET(LOCATION_ENDPOINT)
    suspend fun getLocation(@Query(value = ID) id: String): Response<LocationInfo>

    @POST(LOCATION_ENDPOINT)
    suspend fun sendLocation(
        @Query(value = LONGITUDE) lon: String,
        @Query(value = ID) id: String,
        @Query(value = LATITUDE) lat: String
    ): Response<LocationInfo>

    @GET(FIND_ENDPOINT)
    suspend fun findDrinkers(@Query(value = ID) id: String): Response<List<LocationInfo>>

    @GET(DETAILS_ENDPOINT)
    suspend fun getUserDetail(@Query(value = ID) id: String): Response<UserInfo>

    @POST(DETAILS_ENDPOINT)
    suspend fun postUserDetail(
        @Query(value = LOGIN) login: String?,
        @Query(value = PASSWORD) password: String?,
        @Query(value = ALCOHOL) alcohol: String? = null,
        @Query(value = GENDER) gender: String? = null,
        @Query(value = AGE) age: Int? = null,
        @Query(value = USERNAME) username: String? = null
    ): Response<ResponseBody>
}