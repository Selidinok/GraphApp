package com.example.model.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Point(
    val x: String,
    val y: String
) : Parcelable