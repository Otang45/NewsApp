package otang.app.news.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import otang.app.news.databinding.TopHeadlineItemBinding
import otang.app.news.model.Articles
import otang.app.news.ui.activity.ReadActivity

class ICVPAdapter(private val context: Context, private val list: List<Articles>) :
    PagerAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(viewGroup: ViewGroup, pos: Int): Any {
        val articles = list[pos]
        val binding = TopHeadlineItemBinding.inflate(
            LayoutInflater.from(
                context
            )
        )
        Glide.with(context).load(articles.urlToImage).into(binding.iv)
        binding.tvSource.text = articles.source.name
        binding.tvTitle.text = articles.title
        binding.tvDescription.text = articles.description
        val time = articles.publishedAt.replace("T", " ").replace("Z", " ")
        binding.tvTime.text = time
        binding.root.setOnClickListener {
            val intent = Intent(context, ReadActivity::class.java)
            intent.putExtra("url", list[pos].url)
            context.startActivity(intent)
        }
        // Add view
        viewGroup.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(viewGroup: ViewGroup, pos: Int, `object`: Any) {
        viewGroup.removeView(`object` as View)
    }
}