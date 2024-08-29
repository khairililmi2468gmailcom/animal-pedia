package com.example.animalpedia

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class ListArticleAdapter(
    private val listArticle: ArrayList<Article>,
    private var onItemClickListener: OnItemClickListener? = null
) : RecyclerView.Adapter<ListArticleAdapter.ListViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(article: Article)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        onItemClickListener = listener
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.img_article)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        val container: ConstraintLayout = itemView.findViewById(R.id.container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listArticle.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val article = listArticle[position]
        holder.imgPhoto.setImageResource(article.photo)
        holder.tvTitle.text = article.title
        holder.tvDescription.text = article.description
        holder.container.setBackgroundColor(article.backgroundColor)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, ArticleDetail::class.java)
            intentDetail.putExtra("data", listArticle[holder.adapterPosition])
            holder.itemView.context.startActivity(intentDetail)
        }
    }
}
