package com.amalip.exam1

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.amalip.exam1.CRUD.*

class ArticleFragment : BaseFragment(R.layout.article_fragment) {

    //Reader View
    private lateinit var imgLike: ImageView
    private lateinit var txvLikes: TextView

    //Edition View
    private lateinit var edtTitle: EditText
    private lateinit var edtContent: EditText
    private lateinit var btnFunction: Button

    //Carousel View
    private lateinit var imgLeft: ImageView
    private lateinit var imgCenter: ImageView
    private lateinit var imgRight: ImageView
    private val photos = listOf(
        R.drawable.ic_money,
        R.drawable.ic_stars,
        R.drawable.ic_happy,
        R.drawable.ic_weather,
        R.drawable.ic_flights
    )

    private var actualPoint = 0
    private var actualArticle: Article? = null
    private var function: CRUD? = null
    private var actualPointUpdated = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actualArticle = requireArguments().getParcelable("article")
        function = requireArguments().getParcelable("function")

        initCarouselView()
        initReadViews()
        initEditionView()

        if (loggedUser.userType == UserType.WRITER && function != READ)
            initWriterView()
        else
            initReaderView()

    }

    private fun initReadViews() {
        imgLike = getView(R.id.imgLike)
        txvLikes = getView(R.id.txvLikes)

        actualArticle?.let {
            txvLikes.text = it.likes.size.toString()
        }

        imgLike.setOnClickListener {
            setLike()
        }

    }

    private fun initEditionView() {
        edtTitle = getView(R.id.edtTitle)
        edtContent = getView(R.id.edtContent)
        btnFunction = getView(R.id.btnFunction)

        btnFunction.setText(function?.text ?: 0)

        actualArticle?.let {
            imgCenter.setImageResource(it.img)
            edtTitle.setText(it.title)
            edtContent.setText(it.content)
        }
    }

    override fun initReaderView() {
        imgLike.isVisible = true
        txvLikes.isVisible = true

        imgLeft.isGone = true
        imgRight.isGone = true

        edtTitle.isEnabled = false
        edtContent.isEnabled = false
        btnFunction.isGone = true
    }

    override fun initWriterView() {
        imgLike.isGone = true
        txvLikes.isGone = true

        imgLeft.isVisible = true
        imgRight.isVisible = true

        edtTitle.isEnabled = true
        edtContent.isEnabled = true
        btnFunction.isVisible = true

        btnFunction.setOnClickListener {
            when (function) {
                CREATE -> create()
                READ -> {
                }
                UPDATE -> update()
                DELETE -> {
                }
                null -> {
                }
            }
        }
    }

    private fun validateForm() = edtTitle.text.isNotEmpty() && edtContent.text.isNotEmpty()

    private fun create() {
        if (validateForm()) {
            val article = Article(
                1,
                loggedUser.id,
                photos[actualPoint],
                edtTitle.text.toString(),
                edtContent.text.toString(),
                mutableListOf()
            )
            savedArticles = savedArticles?.apply {
                add(article.apply { id = size })
            } ?: mutableListOf(article)

            requireActivity().onBackPressed()

        } else showToast(R.string.text_complete_form)
    }

    private fun update() {
        actualArticle?.let {
            if (validateForm()) {
                val article =
                    savedArticles?.find { articleFound -> articleFound.id == it.id }?.apply {
                        if (actualPointUpdated) img = photos[actualPoint]
                        title = edtTitle.text.toString()
                        content = edtContent.text.toString()
                    }

                finishUpdate(article)

                requireActivity().onBackPressed()

            } else showToast(R.string.text_complete_form)
        }
    }

    private fun setLike() {
        actualArticle?.apply {
            val index = likes.indexOfFirst { index -> index.userId == loggedUser.id }
            if (index != -1)
                likes.removeAt(index)
            else likes.add(Like(likes.size, loggedUser.id))
        }

        finishUpdate(actualArticle)

        initReadViews()
    }

    private fun finishUpdate(article: Article?) {
        savedArticles = savedArticles?.apply {
            set(
                this.indexOfFirst { indexedArticle -> indexedArticle.id == article?.id },
                article!!
            )
        }
    }

    private fun initCarouselView() {
        imgLeft = getView(R.id.imgLeft)
        imgCenter = getView(R.id.imgCenter)
        imgRight = getView(R.id.imgRight)

        imgCenter.setImageResource(photos[actualPoint])

        imgLeft.setOnClickListener { carouselNavigateTo(CarouselWay.Left) }
        imgRight.setOnClickListener { carouselNavigateTo(CarouselWay.Right) }
    }

    private fun carouselNavigateTo(way: CarouselWay) {
        actualPointUpdated = true

        actualPoint = if (way == CarouselWay.Left) actualPoint - 1 else actualPoint + 1
        actualPoint =
            if (actualPoint < 0) photos.size - 1 else if (actualPoint == photos.size) 0 else actualPoint

        imgCenter.setImageResource(photos[actualPoint])
    }

}