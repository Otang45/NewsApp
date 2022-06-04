package otang.news.ui.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.mancj.materialsearchbar.MaterialSearchBar;
import java.util.ArrayList;
import java.util.List;
import otang.news.R;
import otang.news.adapter.CategoryAdapter;
import otang.news.adapter.ICVPAdapter;
import otang.news.adapter.NewsAdapter;
import otang.news.api.ApiClient;
import otang.news.api.ApiInterface;
import otang.news.databinding.HomeActivityBinding;
import otang.news.model.Articles;
import otang.news.model.Category;
import otang.news.model.NewsResponse;
import otang.news.preference.WindowPreference;
import retrofit2.Call;
import otang.news.util.AppUtils;
import retrofit2.Response;
import retrofit2.Callback;

public class HomeActivity extends AppCompatActivity
		implements MaterialSearchBar.OnSearchActionListener, CategoryAdapter.OnItemClick {

	private ApiInterface apiInterface;
	private HomeActivityBinding binding;
	private List<Category> categoryList;
	private String apikey;

	boolean searchResult = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = HomeActivityBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		setupWindow();
		setupData();
		setupView();
		setupCategory();
		getNews("all");
	}

	private void setupView() {
		binding.msb.setOnSearchActionListener(this);
		binding.shimmer.getRoot().setVisibility(VISIBLE);
		hideView();
	}

	private void setupData() {
		apiInterface = ApiClient.getApiInterface();
		apikey = getString(R.string.apikey);
	}

	private void setupCategory() {
		categoryList = new ArrayList<>();
		categoryList.add(new Category(R.drawable.all, "all"));
		categoryList.add(new Category(R.drawable.business, "business"));
		categoryList.add(new Category(R.drawable.science, "science"));
		categoryList.add(new Category(R.drawable.technology, "technology"));
		categoryList.add(new Category(R.drawable.health, "health"));
		categoryList.add(new Category(R.drawable.sports, "sports"));
		categoryList.add(new Category(R.drawable.entertainment, "entertainment"));
		LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
		CategoryAdapter adapter = new CategoryAdapter(this, categoryList, binding.rvCategory, this);
		binding.rvCategory.setHasFixedSize(true);
		binding.rvCategory.setLayoutManager(manager);
		binding.rvCategory.setAdapter(adapter);
	}

	private void getNews(String category) {
		searchResult = false;
		if (category.equals("all")) {
			apiInterface.getTopHeadline("id", apikey).enqueue(new Callback<NewsResponse>() {
				@Override
				public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
					NewsResponse newsResponse = response.body();
					binding.shimmer.getRoot().setVisibility(GONE);
					showView();
					if (newsResponse != null) {
						List<Articles> list = new ArrayList<>();
						for (int i = 0; i < 5; i++) {
							list.add(newsResponse.getArticles().get(i));
						}
						ICVPAdapter adapter = new ICVPAdapter(HomeActivity.this, list);
						binding.hicvp.setAdapter(adapter);
						List<Articles> list1 = new ArrayList<>();
						for (int i = 5; i < newsResponse.getArticles().size(); i++) {
							list1.add(newsResponse.getArticles().get(i));
						}
						NewsAdapter adapter1 = new NewsAdapter(HomeActivity.this, list1);
						binding.rvNews.setHasFixedSize(true);
						binding.rvNews.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
						binding.rvNews.setAdapter(adapter1);
					}
				}

				@Override
				public void onFailure(Call<NewsResponse> call, Throwable throwable) {
					binding.shimmer.getRoot().setVisibility(VISIBLE);
					showAlertDialog(throwable.getMessage());
				}
			});
		} else {
			apiInterface.getByCategory(category, "id", apikey).enqueue(new Callback<NewsResponse>() {
				@Override
				public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
					NewsResponse newsResponse = response.body();
					binding.shimmer.getRoot().setVisibility(GONE);
					showView();
					if (newsResponse != null) {
						List<Articles> list = new ArrayList<>();
						for (int i = 0; i < 5; i++) {
							list.add(newsResponse.getArticles().get(i));
						}
						ICVPAdapter adapter = new ICVPAdapter(HomeActivity.this, list);
						binding.hicvp.setAdapter(adapter);
						List<Articles> list1 = new ArrayList<>();
						for (int i = 5; i < newsResponse.getArticles().size(); i++) {
							list1.add(newsResponse.getArticles().get(i));
						}
						NewsAdapter adapter1 = new NewsAdapter(HomeActivity.this, list1);
						binding.rvNews.setHasFixedSize(true);
						binding.rvNews.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
						binding.rvNews.setAdapter(adapter1);
					}
				}

				@Override
				public void onFailure(Call<NewsResponse> call, Throwable throwable) {
					binding.shimmer.getRoot().setVisibility(VISIBLE);
					showAlertDialog(throwable.getMessage());
				}
			});
		}
	}

	private void findNews(String keyword) {
		apiInterface.findNews(keyword, apikey).enqueue(new Callback<NewsResponse>() {
			@Override
			public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
				NewsResponse newsResponse = response.body();
				binding.shimmer.getRoot().setVisibility(GONE);
				binding.rvCategory.setVisibility(GONE);
				binding.hicvp.setVisibility(GONE);
				binding.tvLatest.setVisibility(GONE);
				binding.rvNews.setVisibility(VISIBLE);
				if (newsResponse != null) {
					NewsAdapter adapter = new NewsAdapter(HomeActivity.this, newsResponse.getArticles());
					binding.rvNews.setHasFixedSize(true);
					binding.rvNews.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
					binding.rvNews.setAdapter(adapter);
					searchResult = true;
				}
			}

			@Override
			public void onFailure(Call<NewsResponse> call, Throwable throwable) {
				searchResult = false;
				binding.shimmer.getRoot().setVisibility(GONE);
				showView();
				showAlertDialog(throwable.getMessage());
			}
		});
	}

	private void showAlertDialog(String message) {
		MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage(message);
		builder.setPositiveButton("Oke", (dialog, which) -> {
			dialog.dismiss();
		});
		builder.show();
	}

	private void hideView() {
		binding.rvCategory.setVisibility(GONE);
		binding.hicvp.setVisibility(GONE);
		binding.tvLatest.setVisibility(GONE);
		binding.rvNews.setVisibility(GONE);
	}

	private void showView() {
		binding.rvCategory.setVisibility(VISIBLE);
		binding.hicvp.setVisibility(VISIBLE);
		binding.tvLatest.setVisibility(VISIBLE);
		binding.rvNews.setVisibility(VISIBLE);
	}

	@Override
	public void onItemClick(int position) {
		getNews(categoryList.get(position).getCategory());
	}

	@Override
	protected void onPause() {
		super.onPause();
		binding.shimmer.getRoot().stopShimmer();
	}

	@Override
	protected void onResume() {
		super.onResume();
		binding.shimmer.getRoot().startShimmer();
	}

	@Override
	public void onBackPressed() {
		if (searchResult == true) {
			binding.shimmer.getRoot().setVisibility(GONE);
			showView();
			getNews("all");
		} else {
			super.onBackPressed();
		}
	}

	private void setupWindow() {
		new WindowPreference(this).applyEdgeToEdgePreference(getWindow(), getColor(R.color.colorSurface));
		AppUtils.addSystemWindowInsetToPadding(binding.getRoot(), false, true, false, true);
	}

	@Override
	public void onSearchStateChanged(boolean enabled) {
		if (enabled) {
			binding.shimmer.getRoot().setVisibility(VISIBLE);
			hideView();
		} else {
			if (searchResult == true) {
				binding.shimmer.getRoot().setVisibility(GONE);
				binding.rvCategory.setVisibility(GONE);
				binding.hicvp.setVisibility(GONE);
				binding.tvLatest.setVisibility(GONE);
				binding.rvNews.setVisibility(VISIBLE);
			} else {
				binding.shimmer.getRoot().setVisibility(GONE);
				showView();
			}
		}
	}

	@Override
	public void onSearchConfirmed(CharSequence text) {
		findNews(text.toString());
	}

	@Override
	public void onButtonClicked(int buttonCode) {
		switch (buttonCode) {
		case MaterialSearchBar.BUTTON_NAVIGATION:
			break;
		case MaterialSearchBar.BUTTON_SPEECH:
			break;
		case MaterialSearchBar.BUTTON_BACK:
			binding.msb.closeSearch();
			break;
		}

	}
}
