package com.amalip.exam1

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types


/**
 * Created by Amalip on 9/19/2021.
 */
class PreferencesManager(private val context: Context) {

    private val PREFS_KEY = "EXAM"
    private val USER_KEY = "USER"
    private val ARTICLES_KEY = "ARTICLES"

    private var preferences: SharedPreferences =
        context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
    private val moshi = Moshi.Builder().build()

    private var jsonAdapter = moshi.adapter<MutableList<Article>>(
        Types.newParameterizedType(
            MutableList::class.java,
            Article::class.java
        )
    )

    var user: User
        set(value) = preferences.edit()
            .putString(USER_KEY, moshi.adapter(User::class.java).toJson(value)).apply()
        get() = preferences.getString(USER_KEY, null)?.let {
            return@let try {
                moshi.adapter(User::class.java).fromJson(it)
            } catch (e: Exception) {
                User()
            }
        } ?: User()

    var articles: MutableList<Article>?
        set(value) = preferences.edit()
            .putString(ARTICLES_KEY, jsonAdapter.toJson(value)).apply()
        get() = preferences.getString(ARTICLES_KEY, null)?.let {
            return@let try {
                jsonAdapter.fromJson(it)
            } catch (e: Exception) {
                null
            }
        }

}