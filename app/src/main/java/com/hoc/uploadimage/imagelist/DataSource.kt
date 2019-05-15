package com.hoc.uploadimage.imagelist

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import android.arch.paging.PositionalDataSource
import com.hoc.uploadimage.ApiService
import com.hoc.uploadimage.GetAllImageNameResponse
import com.hoc.uploadimage.UploadResponse
import com.hoc.uploadimage.utils.parse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DS(private val apiService: ApiService, private val handleError: (String) -> Unit) : PositionalDataSource<String>() {
    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<String>) {
        println("[DS] loadRange params.startPosition = [${params.startPosition}], params.loadSize = [${params.loadSize}]")
        apiService.getAllImageName(start = params.startPosition, limit = params.loadSize)
                .enqueue(object : Callback<GetAllImageNameResponse> {
                    override fun onFailure(call: Call<GetAllImageNameResponse>?, t: Throwable?) {
                        handleError(t?.message ?: "Unknown error")
                    }

                    override fun onResponse(call: Call<GetAllImageNameResponse>?, response: Response<GetAllImageNameResponse>) {
                        if (response.isSuccessful) {
                            callback.onResult(response.body()?.data
                                    ?: throw NullPointerException("GetAllImageNameResponse::data is null"))
                        } else {
                            parse<UploadResponse>(response.errorBody()?.use { it.string() }).let {
                                handleError(it?.message ?: "Unknown error")
                            }
                        }
                    }
                })
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<String>) {
        println("[DS] loadInitial params.requestedLoadSize = [${params.requestedLoadSize}]")
        apiService.getAllImageName(start = 0, limit = params.requestedLoadSize)
                .enqueue(object : Callback<GetAllImageNameResponse> {
                    override fun onFailure(call: Call<GetAllImageNameResponse>?, t: Throwable?) {
                        handleError(t?.message ?: "Unknown error")
                    }

                    override fun onResponse(call: Call<GetAllImageNameResponse>?, response: Response<GetAllImageNameResponse>) {
                        if (response.isSuccessful) {
                            val data = (response.body()?.data
                                    ?: throw NullPointerException("GetAllImageNameResponse::data is null"))
                            callback.onResult(data, 0, data.size)
                        } else {
                            parse<UploadResponse>(response.errorBody()?.use { it.string() }).let {
                                handleError(it?.message ?: "Unknown error")
                            }
                        }
                    }
                })
    }

}

class DSF(private val apiService: ApiService, private val handleError: (String) -> Unit) : DataSource.Factory<Int, String>() {
    val mutableDS = MutableLiveData<DS>()
    override fun create(): DataSource<Int, String> {
        return DS(apiService, handleError).also(mutableDS::postValue)
    }
}
