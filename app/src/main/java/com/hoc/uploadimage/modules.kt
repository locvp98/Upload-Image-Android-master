package com.hoc.uploadimage

import com.hoc.uploadimage.imagelist.ListImageViewModel
import com.hoc.uploadimage.upload.UploadViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

val viewModelModule = applicationContext {
    viewModel { UploadViewModel(get(), get()) }
    viewModel { ListImageViewModel(get()) }
}

val appModule = applicationContext {
    bean { MyApp.application }
}