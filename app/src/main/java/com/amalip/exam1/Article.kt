package com.amalip.exam1

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * Created by Amalip on 9/19/2021.
 */

@JsonClass(generateAdapter = true)
@Parcelize
class Article(
    var id: Int,
    val authorID: Int,
    var img: Int,
    var title: String,
    var content: String,
    val likes: @RawValue MutableList<Like>
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
class Like(val id: Int, val userId: Int) : Parcelable