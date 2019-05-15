package com.hoc.uploadimage.upload

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import android.provider.OpenableColumns
import com.hoc.uploadimage.ApiService
import com.hoc.uploadimage.MyApp
import com.hoc.uploadimage.UploadResponse
import com.hoc.uploadimage.utils.SingleLiveEvent
import com.hoc.uploadimage.utils.parse
import io.reactivex.disposables.CompositeDisposable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class UploadViewModel(
        application: Application,
        val apiService: ApiService
) : AndroidViewModel(application) {
    private val app = getApplication<MyApp>()
    private val compositeDisposable = CompositeDisposable()

    private val mutableIsLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableMessage = SingleLiveEvent<String>().apply { value = null }
    private val mutableUri = MutableLiveData<Uri>().apply { value = null }

    val message: LiveData<String> = mutableMessage
    val isLoading: LiveData<Boolean> = mutableIsLoading
    val uri: LiveData<Uri> = mutableUri

    fun uploadImage(imageName: String) {
        val imageUri = mutableUri.value
        if (imageUri == null) {
            showMessage("Please select an image ")
            return
        }

        mutableIsLoading.value = true

        val contentResolver = app.contentResolver
        val type = contentResolver.getType(imageUri)
        val inputStream = contentResolver.openInputStream(imageUri)
        val fileName = contentResolver.query(imageUri, null, null, null, null).use {
            it.moveToFirst()
            it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
        val bytes = ByteArrayOutputStream().use {
            inputStream.copyTo(it)
            it.toByteArray()
        }

        val requestFile = RequestBody.create(MediaType.parse(type), bytes)
        val body = MultipartBody.Part.createFormData("image", fileName, requestFile)
        val uploadImage = apiService.uploadImage(body, imageName)
        uploadImage.enqueue(object : Callback<UploadResponse> {
            override fun onFailure(call: Call<UploadResponse>?, t: Throwable?) {
                showMessage(t?.message ?: "Unknown error")
                mutableIsLoading.value = false
            }

            override fun onResponse(call: Call<UploadResponse>?, response: Response<UploadResponse>) {
                if (response.isSuccessful) showMessage(response.body()!!.message)
                else {
                    parse<UploadResponse>(response.errorBody()?.use { it.string() } ?: "").let {
                        showMessage(it?.message ?: "Unknown error")
                    }
                }
                mutableIsLoading.value = false
            }
        })
    }

    private fun showMessage(it: String?) {
        mutableMessage.value = it
    }

    fun setImageUri(data: Uri?) {
        mutableUri.value = data
    }

    override fun onCleared() {
    }
}

