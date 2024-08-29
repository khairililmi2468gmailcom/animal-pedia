package com.example.animalpedia

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Article(
    val title: String,
    val description: String,
    val photo: Int,
    val backgroundColor: Int
) : Parcelable