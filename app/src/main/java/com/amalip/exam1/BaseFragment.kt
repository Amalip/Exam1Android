package com.amalip.exam1

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * Created by Amalip on 9/19/2021.
 */

open class BaseFragment(layout: Int) : Fragment(layout) {

    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferencesManager = PreferencesManager(requireContext())
    }

    fun navigateTo(fragment: Fragment, saveInStack: Boolean = true) =
        (requireActivity() as MainActivity).replaceFragment(fragment, saveInStack)

    fun showToast(message: Int) =
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    fun <V : View> getView(id: Int): V = requireView().findViewById(id)

    var loggedUser: User
        set(value) {
            preferencesManager.user = value
        }
        get() = preferencesManager.user

    var savedArticles: MutableList<Article>?
        set(value) {
            preferencesManager.articles = value
        }
        get() = preferencesManager.articles

    val filteredArticlesByAuthor: MutableList<Article>
        get() = savedArticles?.let {
            return@let it.filter { it.authorID == loggedUser.id }.toMutableList()
        } ?: mutableListOf()

    open fun initReaderView() {}

    open fun initWriterView() {}

}