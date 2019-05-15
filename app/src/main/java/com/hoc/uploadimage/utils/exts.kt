package com.hoc.uploadimage.utils

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import com.squareup.moshi.Moshi

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: (T) -> Unit) {
    observe(owner, Observer { it?.let { observer(it) } })
}

inline fun <reified T> parse(string: String?): T? {
    return if (string != null) {
        Moshi.Builder()
                .build()
                .adapter(T::class.java)
                .nullSafe()
                .fromJson(string)
    } else null
}