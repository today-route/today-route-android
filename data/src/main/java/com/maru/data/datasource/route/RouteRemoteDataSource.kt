package com.maru.data.datasource.route

import com.maru.data.model.Route
import com.maru.data.model.SimpleRoute
import com.maru.data.network.response.SaveNewRouteResponse
import com.maru.data.network.server.RetrofitService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class RouteRemoteDataSource @Inject constructor(
    private val retrofitService: RetrofitService
) : RouteDataSource {

    override suspend fun getRouteOfMonth(year: Int, month: Int): Result<List<SimpleRoute>> =
        runCatching {
            retrofitService.getRouteOfMonth(year.toString(), month.toString())
        }

    override suspend fun getRouteById(routeId: Int): Result<Route> = runCatching {
        retrofitService.getRouteById(routeId)
    }

    override suspend fun saveNewRoute(
        date: String,
        zoomLevel: Double,
        title: String,
        contents: String,
        location: String,
        fileList: List<File>,
        geoCoordList: List<List<Double>>
    ): Result<SaveNewRouteResponse> = runCatching {
        val dateBody = stringToPlainTextRequestBody(date)
        val zoomLevelBody = stringToPlainTextRequestBody(zoomLevel.toString())
        val titleBody = stringToPlainTextRequestBody(title)
        val contentsBody = stringToPlainTextRequestBody(contents)
        val locationBody = stringToPlainTextRequestBody(location)

        val requestMap = mapOf(
            "date" to dateBody,
            "zoomLevel" to zoomLevelBody,
            "title" to titleBody,
            "content" to contentsBody,
            "location" to locationBody
        )

        val photoFiles = mutableListOf<MultipartBody.Part>()
        for (file in fileList) {
            val fileBody: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val fileToUpload = MultipartBody.Part.createFormData("photos",file.name, fileBody)
            photoFiles.add(fileToUpload)
        }

        val geoCoords = mutableListOf<MultipartBody.Part>()
        for (i in geoCoordList.indices) {
            geoCoords.add(MultipartBody.Part.createFormData("geoCoord[$i][0]", geoCoordList[i][0].toString()))
            geoCoords.add(MultipartBody.Part.createFormData("geoCoord[$i][1]", geoCoordList[i][1].toString()))
        }

        retrofitService.saveNewRoute(requestMap, photoFiles, geoCoords)
    }

    companion object {
        val stringToPlainTextRequestBody: (String) -> RequestBody = { s: String ->
            s.toRequestBody("text/plain".toMediaTypeOrNull())
        }
    }
}