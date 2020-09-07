package com.em_projects.testapp.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GeoPoint(val lat: Double, val lng: Double): Parcelable

@Parcelize
data class FarmData(
    val id: Int,
    val name: String,
    val lat: Double,
    val lng: Double,
    var points: List<GeoPoint>? = null
): Parcelable