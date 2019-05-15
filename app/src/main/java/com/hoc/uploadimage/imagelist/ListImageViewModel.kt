package com.hoc.uploadimage.imagelist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.hoc.uploadimage.ApiService
import com.hoc.uploadimage.utils.SingleLiveEvent

class ListImageViewModel(apiService: ApiService) : ViewModel() {
    private val mutableMessage = SingleLiveEvent<String>()
    private val dataSourceFactory: DSF
    private val mutableIsLoading = MutableLiveData<Boolean>()

    val pagedList: LiveData<PagedList<String>>
    val message: LiveData<String> = mutableMessage
    val isLoading: LiveData<Boolean> = mutableIsLoading

    init {
        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(PAGE_SIZE * 2)
                .setPageSize(PAGE_SIZE)
                .build()
        dataSourceFactory = DSF(apiService, ::handleError)
        pagedList = LivePagedListBuilder(dataSourceFactory, config).build()
    }

    fun onRefresh() {
        println("onRefresh")
        dataSourceFactory.mutableDS.value?.addInvalidatedCallback {
            mutableIsLoading.value = false
        }
        mutableIsLoading.value = true
        dataSourceFactory.mutableDS.value?.invalidate()
    }

    private fun handleError(string: String) {
        mutableMessage.postValue(string)
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}