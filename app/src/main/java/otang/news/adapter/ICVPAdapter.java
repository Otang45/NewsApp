package otang.news.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import java.util.List;
import otang.news.databinding.TopHeadlineItemBinding;
import otang.news.ui.activity.ReadActivity;
import otang.news.model.Articles;

public class ICVPAdapter extends PagerAdapter {

	private Context context;
	private List<Articles> list;

	public ICVPAdapter(Context context, List<Articles> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public Object instantiateItem(ViewGroup viewGroup, int pos) {
		final Articles articles = list.get(pos);
		TopHeadlineItemBinding binding = TopHeadlineItemBinding.inflate(LayoutInflater.from(context));
		Glide.with(context).load(articles.getUrlToImage()).into(binding.iv);
		binding.tvSource.setText(articles.getSource().getName());
		binding.tvTitle.setText(articles.getTitle());
		binding.tvDescription.setText(articles.getDescription());
		String time = articles.getPublishedAt().replace("T", " ").replace("Z", " ");
		binding.tvTime.setText(time);
		binding.getRoot().setOnClickListener(v -> {
			Intent intent = new Intent(context, ReadActivity.class);
			intent.putExtra("url", list.get(pos).getUrl());
			context.startActivity(intent);
		});
		// Add view
		viewGroup.addView(binding.getRoot());
		return binding.getRoot();
	}

	@Override
	public void destroyItem(ViewGroup viewGroup, int pos, Object object) {
		viewGroup.removeView((View) object);
	}

}