package com.amalip.exam1

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 * Created by Amalip on 9/19/2021.
 */

@JsonClass(generateAdapter = true)
@Parcelize
class User(
    val id: Int = 0,
    val username: String = "",
    val password: String = "",
    val img: Int = 0,
    val userType: UserType = UserType.READER
) : Parcelable {

    companion object {
        val users = listOf(
            User(1, "Writer1", "123", R.drawable.ic_android_green, UserType.WRITER),
            User(2, "Writer2", "123", R.drawable.ic_android_black, UserType.WRITER),
            User(3, "Writer3", "123", R.drawable.ic_android_blue, UserType.WRITER),
            User(4, "Reader1", "123", R.drawable.ic_android_red, UserType.READER),
            User(5, "Reader2", "123", R.drawable.ic_android_yellow, UserType.READER),
            User(6, "Reader3", "123", R.drawable.ic_android_pink, UserType.READER)
        )
    }

    fun validateLogin() = users.firstOrNull { it.username == username && it.password == password }

    fun getPhoto() = users.firstOrNull { it.username == username }?.img ?: 0

}