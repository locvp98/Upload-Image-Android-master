package com.hoc.uploadimage

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

const val BASE_URL = "http://10.10.42.157:3001"

data class UploadResponse(val message: String)

data class GetAllImageNameResponse(
        val data: List<String>? = null,
        val message: String? = null
)

interface ApiService {
    @Multipart
    @POST("/images/upload/{image_name}")
    fun uploadImage(
            @Part body: MultipartBody.Part,
            @Path("image_name") imageName: String
    ): Call<UploadResponse>

    @GET("/images")
    fun getAllImageName(
            @Query("start") start: Int? = null,
            @Query("limit") limit: Int? = null
    ): Call<GetAllImageNameResponse>
}
