package com.amalip.exam1

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged

class LoginFragment : BaseFragment(R.layout.login_fragment) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (loggedUser.id != 0)
            goToUserFragment()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getView<Button>(R.id.btnLogin).setOnClickListener {
            User(
                username = getView<EditText>(R.id.edtUsername).text.toString(),
                password = getView<EditText>(R.id.edtPassword).text.toString()
            ).validateLogin()?.let {
                loggedUser = it
                goToUserFragment()
            } ?: showToast(R.string.userNotFound)
        }

        getView<EditText>(R.id.edtUsername).doAfterTextChanged {
            getView<ImageView>(R.id.imgUser)
                .setImageResource(User(username = (it ?: "").toString()).getPhoto())
        }

    }

    private fun goToUserFragment() = navigateTo(UserFragment(), false)

}