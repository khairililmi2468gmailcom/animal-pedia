package com.example.animalpedia

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

@Suppress("DEPRECATION")
class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val list = ArrayList<Article>()

    private lateinit var toolbar: MaterialToolbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        toolbar = view.findViewById(R.id.topAppBar)

        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

        setHasOptionsMenu(true)

        recyclerView = view.findViewById(R.id.rv_article)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        list.addAll(getArticle())
        showRecyclerList()

        return view
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_appbar_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showRecyclerList() {
        val listAdapter = ListArticleAdapter(list)
        recyclerView.adapter = listAdapter
        recyclerView.setHasFixedSize(true)

        listAdapter.setOnItemClickListener(object : ListArticleAdapter.OnItemClickListener{
            override fun onItemClick(article: Article) {
                val intentToDetail = Intent(requireContext(), ArticleDetail::class.java)
                intentToDetail.putExtra(ArticleDetail.ARTICLE, article)
                startActivity(intentToDetail)
            }
        })
    }

    @SuppressLint("Recycle")
    private fun getArticle(): ArrayList<Article> {
        val title = resources.getStringArray(R.array.data_title)
        val img = resources.obtainTypedArray(R.array.data_img)
        val description = resources.getStringArray(R.array.data_description)
        val backgroundColor = resources.obtainTypedArray(R.array.data_color)
        val listArticles = ArrayList<Article>()
        for (i in title.indices) {
            val article =
                Article(title[i], description[i], img.getResourceId(i, -1), backgroundColor.getColor(i, -1))
            listArticles.add(article)
        }
        return listArticles
    }

}