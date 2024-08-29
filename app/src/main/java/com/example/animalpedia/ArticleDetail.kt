package com.example.animalpedia

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ArticleDetail : AppCompatActivity() {

    companion object{
        const val ARTICLE ="data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)

        val data = intent.getParcelableExtra<Article>("data")
        Log.d("Detail data", data?.title.toString())

        val tvTitle: TextView = findViewById(R.id.tv_title)
        val imgPhoto: ImageView = findViewById(R.id.img_article)
        val tvDescription: TextView = findViewById(R.id.tv_description)

        if (data != null){
            val dataName = data.title
            val dataSummary = data.description
            val dataPhoto = data.photo

            tvTitle.text = dataName
            imgPhoto.setImageResource(dataPhoto)
            tvDescription.text = dataSummary
        }
    }
}