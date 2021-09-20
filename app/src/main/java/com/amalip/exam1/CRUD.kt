package com.amalip.exam1

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Amalip on 9/20/2021.
 */

@Parcelize
enum class CRUD(val text: Int): Parcelable {
    CREATE(R.string.text_create), READ(R.string.text_read), UPDATE(R.string.text_update), DELETE(R.string.text_delete)
}