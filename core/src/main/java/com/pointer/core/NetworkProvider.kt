package com.pointer.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.core.content.getSystemService

class NetworkProvider(
    private val context: Context
) {

    private val networkInfo: NetworkInfo?
        get() = context.getSystemService<ConnectivityManager>()?.activeNetworkInfo

    val isConnected: Boolean
        get() = networkInfo?.isConnected ?: false
}