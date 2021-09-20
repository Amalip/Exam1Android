package com.amalip.exam1

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible


class UserFragment : BaseFragment(R.layout.user_fragment) {

    //Carousel View
    private lateinit var imgLeft: ImageView
    private lateinit var imgCenter: ImageView
    private lateinit var imgRight: ImageView
    private lateinit var txvTitle: TextView
    private lateinit var imgLike: ImageView

    //CRUD View
    private lateinit var btnCreate: Button
    private lateinit var btnRead: Button
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button

    var actualArticle: Article? = null
    var actualPoint = 0

    var finalList: MutableList<Article>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        finalList = if (loggedUser.userType == UserType.WRITER) filteredArticlesByAuthor else savedArticles

        initUserView()

        initCreate()
        initRead()
        initUpdate()
        initDelete()

        initCarouselView()

        if (loggedUser.userType == UserType.WRITER)
            initWriterView()
        else
            initReaderView()

        getView<Button>(R.id.btnLogout).setOnClickListener {
            loggedUser = User()
            navigateTo(LoginFragment(), false)
        }
    }

    private fun initUserView() {
        getView<ImageView>(R.id.imgUser).setImageResource(loggedUser.img)
        getView<TextView>(R.id.txvUsername).text = loggedUser.username
        getView<TextView>(R.id.txvUserType).text = loggedUser.userType.name

        initExtraView()
    }

    private fun initExtraView() {
        getView<TextView>(R.id.txvExtraInfo).text = if (loggedUser.userType == UserType.WRITER) {
            getString(R.string.text_articles, finalList?.size ?: 0)
        } else {
            var totalLikes = 0

            finalList?.let {
                it.forEach { article ->
                    totalLikes =
                        if (article.likes.find { like -> like.userId == loggedUser.id } != null) totalLikes + 1 else totalLikes
                }
            }

            getString(R.string.text_likes, totalLikes)
        }
    }

    private fun initCarouselView() {
        imgLeft = getView(R.id.imgLeft)
        imgCenter = getView(R.id.imgCenter)
        imgRight = getView(R.id.imgRight)
        txvTitle = getView(R.id.txvTitle)
        imgLike = getView(R.id.imgLike)

        imgLeft.setOnClickListener { carouselNavigateTo(CarouselWay.Left) }
        imgRight.setOnClickListener { carouselNavigateTo(CarouselWay.Right) }

        finalList?.let {
            if (it.size > 0) actualArticle = it[0]
            setActualArticle()
        }
    }

    private fun carouselNavigateTo(way: CarouselWay) {
        finalList?.let {
            if (it.size > 0) {
                actualPoint = if (way == CarouselWay.Left) actualPoint - 1 else actualPoint + 1
                actualPoint =
                    if (actualPoint < 0) it.size - 1 else if (actualPoint == it.size) 0 else actualPoint

                actualArticle = it[actualPoint]
            }
            setActualArticle()
        }
    }

    private fun setActualArticle() = actualArticle?.let {
        imgCenter.setImageResource(it.img)
        txvTitle.text = it.title

        if (loggedUser.userType == UserType.READER)
            imgLike.setImageResource(if (it.likes.firstOrNull { like -> like.userId == loggedUser.id } != null) R.drawable.ic_liked else R.drawable.ic_not_liked)

    } ?: run {
        imgCenter.setImageResource(R.drawable.ic_404)
        txvTitle.text = ""

        btnRead.isEnabled = false
        btnUpdate.isEnabled = false
        btnDelete.isEnabled = false
    }

    override fun initWriterView() {
        imgLike.isGone = true
    }

    override fun initReaderView() {
        imgLike.isVisible = true

        btnCreate.isInvisible = true
        btnUpdate.isInvisible = true
        btnDelete.isInvisible = true

        finalList?.let {
            btnRead.isEnabled = it.size > 0
        }?: run {
            btnRead.isEnabled = false
        }

        initRead()
    }

    private fun initCreate() {
        btnCreate = getView<Button>(R.id.btnCreate).apply {
            setOnClickListener {
                navigateTo(ArticleFragment().apply {
                    arguments = bundleOf("function" to CRUD.CREATE)
                })
            }
        }
    }

    private fun initRead() {
        btnRead = getView<Button>(R.id.btnRead).apply {
            setOnClickListener {
                navigateTo(ArticleFragment().apply {
                    arguments = bundleOf("function" to CRUD.READ, "article" to actualArticle)
                })
            }
        }
    }

    private fun initUpdate() {
        btnUpdate = getView<Button>(R.id.btnUpdate).apply {
            setOnClickListener {
                navigateTo(ArticleFragment().apply {
                    arguments = bundleOf("function" to CRUD.UPDATE, "article" to actualArticle)
                })
            }
        }
    }

    private fun initDelete() {
        btnDelete = getView<Button>(R.id.btnDelete).apply {
            setOnClickListener {
                savedArticles = savedArticles?.apply {
                    removeIf { articleToRemove -> articleToRemove.id == actualArticle?.id }
                }

                finalList = filteredArticlesByAuthor
                actualArticle = null
                carouselNavigateTo(CarouselWay.Right)
                initExtraView()
            }
        }
    }
}