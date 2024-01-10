package otang.app.news.ui.activity

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.internal.EdgeToEdgeUtils
import com.mancj.materialsearchbar.MaterialSearchBar
import com.mancj.materialsearchbar.MaterialSearchBar.OnSearchActionListener
import otang.app.news.R
import otang.app.news.adapter.CategoryAdapter
import otang.app.news.adapter.CategoryAdapter.OnItemClick
import otang.app.news.adapter.ICVPAdapter
import otang.app.news.adapter.NewsAdapter
import otang.app.news.api.ApiClient
import otang.app.news.api.ApiInterface
import otang.app.news.databinding.HomeActivityBinding
import otang.app.news.model.Articles
import otang.app.news.model.Category
import otang.app.news.model.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity(), OnSearchActionListener, OnItemClick {
    private lateinit var apiInterface: ApiInterface
    private lateinit var binding: HomeActivityBinding
    private lateinit var categoryList: MutableList<Category>
    private lateinit var apikey: String
    var searchResult = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindow()
        setupData()
        setupView()
        setupCategory()
        getNews("all")
    }

    private fun setupView() {
        binding.msb.setOnSearchActionListener(this)
        binding.shimmer.root.visibility = View.VISIBLE
        hideView()
    }

    private fun setupData() {
        apiInterface = ApiClient.apiInterface
        apikey = getString(R.string.apikey)
    }

    private fun setupCategory() {
        categoryList = ArrayList()
        categoryList.add(Category(R.drawable.all, "all"))
        categoryList.add(Category(R.drawable.business, "business"))
        categoryList.add(Category(R.drawable.science, "science"))
        categoryList.add(Category(R.drawable.technology, "technology"))
        categoryList.add(Category(R.drawable.health, "health"))
        categoryList.add(Category(R.drawable.sports, "sports"))
        categoryList.add(Category(R.drawable.entertainment, "entertainment"))
        val manager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = CategoryAdapter(this, categoryList, this)
        binding.rvCategory.setHasFixedSize(true)
        binding.rvCategory.layoutManager = manager
        binding.rvCategory.adapter = adapter
    }

    private fun getNews(category: String?) {
        searchResult = false
        if (category == "all") {
            apiInterface.getTopHeadline("id", apikey).enqueue(object : Callback<NewsResponse?> {
                override fun onResponse(
                    call: Call<NewsResponse?>,
                    response: Response<NewsResponse?>
                ) {
                    val newsResponse = response.body()
                    if (newsResponse != null) {
                        binding.shimmer.root.visibility = View.GONE
                        showView()
                        val list: ArrayList<Articles> = ArrayList()
                        for (i in 0..4) {
                            list.add(newsResponse.articles[i])
                        }
                        val adapter = ICVPAdapter(this@HomeActivity, list)
                        binding.hicvp.adapter = adapter
                        val list1: ArrayList<Articles> = ArrayList()
                        for (i in 5 until newsResponse.articles.size) {
                            list1.add(newsResponse.articles[i])
                        }
                        val adapter1 = NewsAdapter(this@HomeActivity, list1)
                        binding.rvNews.setHasFixedSize(true)
                        binding.rvNews.layoutManager = LinearLayoutManager(this@HomeActivity)
                        binding.rvNews.adapter = adapter1
                    } else {
                        binding.shimmer.root.visibility = View.VISIBLE
                        hideView()
                    }
                }

                override fun onFailure(call: Call<NewsResponse?>, throwable: Throwable) {
                    binding.shimmer.root.visibility = View.VISIBLE
                    hideView()
                    showAlertDialog(throwable.message)
                }
            })
        } else {
            apiInterface.getByCategory(category, "id", apikey)
                .enqueue(object : Callback<NewsResponse?> {
                    override fun onResponse(
                        call: Call<NewsResponse?>,
                        response: Response<NewsResponse?>
                    ) {
                        val newsResponse = response.body()
                        binding.shimmer.root.visibility = View.GONE
                        showView()
                        if (newsResponse != null) {
                            val list: ArrayList<Articles> = ArrayList()
                            for (i in 0..4) {
                                list.add(newsResponse.articles[i])
                            }
                            val adapter = ICVPAdapter(this@HomeActivity, list)
                            binding.hicvp.adapter = adapter
                            val list1: ArrayList<Articles> = ArrayList()
                            for (i in 5 until newsResponse.articles.size) {
                                list1.add(newsResponse.articles[i])
                            }
                            val adapter1 = NewsAdapter(this@HomeActivity, list1)
                            binding.rvNews.setHasFixedSize(true)
                            binding.rvNews.layoutManager = LinearLayoutManager(this@HomeActivity)
                            binding.rvNews.adapter = adapter1
                        }
                    }

                    override fun onFailure(call: Call<NewsResponse?>, throwable: Throwable) {
                        binding.shimmer.root.visibility = View.VISIBLE
                        hideView()
                        showAlertDialog(throwable.message)
                    }
                })
        }
    }

    private fun findNews(keyword: String) {
        apiInterface.findNews(keyword, apikey).enqueue(object : Callback<NewsResponse?> {
            override fun onResponse(call: Call<NewsResponse?>, response: Response<NewsResponse?>) {
                val newsResponse = response.body()
                binding.shimmer.root.visibility = View.GONE
                binding.rvCategory.visibility = View.GONE
                binding.hicvp.visibility = View.GONE
                binding.tvLatest.visibility = View.GONE
                binding.rvNews.visibility = View.VISIBLE
                if (newsResponse != null) {
                    val adapter = NewsAdapter(this@HomeActivity, newsResponse.articles)
                    binding.rvNews.setHasFixedSize(true)
                    binding.rvNews.layoutManager = LinearLayoutManager(this@HomeActivity)
                    binding.rvNews.adapter = adapter
                    searchResult = true
                }
            }

            override fun onFailure(call: Call<NewsResponse?>, throwable: Throwable) {
                searchResult = false
                binding.shimmer.root.visibility = View.GONE
                showView()
                showAlertDialog(throwable.message)
            }
        })
    }

    private fun showAlertDialog(message: String?) {
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle(getString(R.string.app_name))
        builder.setMessage(message)
        builder.setPositiveButton("Oke") { dialog: DialogInterface, _: Int -> dialog.dismiss() }
        builder.show()
    }

    private fun hideView() {
        binding.rvCategory.visibility = View.GONE
        binding.hicvp.visibility = View.GONE
        binding.tvLatest.visibility = View.GONE
        binding.rvNews.visibility = View.GONE
    }

    private fun showView() {
        binding.rvCategory.visibility = View.VISIBLE
        binding.hicvp.visibility = View.VISIBLE
        binding.tvLatest.visibility = View.VISIBLE
        binding.rvNews.visibility = View.VISIBLE
    }

    override fun onItemClick(position: Int) {
        getNews(categoryList[position].category)
    }

    override fun onPause() {
        super.onPause()
        binding.shimmer.root.stopShimmer()
    }

    override fun onResume() {
        super.onResume()
        binding.shimmer.root.startShimmer()
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (searchResult) {
            binding.shimmer.root.visibility = View.GONE
            showView()
            getNews("all")
        } else {
            super.onBackPressed()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun setupWindow() {
        EdgeToEdgeUtils.applyEdgeToEdge(window, true)
    }

    override fun onSearchStateChanged(enabled: Boolean) {
        if (enabled) {
            binding.shimmer.root.visibility = View.VISIBLE
            hideView()
        } else {
            if (searchResult) {
                binding.shimmer.root.visibility = View.GONE
                binding.rvCategory.visibility = View.GONE
                binding.hicvp.visibility = View.GONE
                binding.tvLatest.visibility = View.GONE
                binding.rvNews.visibility = View.VISIBLE
            } else {
                binding.shimmer.root.visibility = View.GONE
                showView()
            }
        }
    }

    override fun onSearchConfirmed(text: CharSequence) {
        findNews(text.toString())
    }

    override fun onButtonClicked(buttonCode: Int) {
        when (buttonCode) {
            MaterialSearchBar.BUTTON_NAVIGATION, MaterialSearchBar.BUTTON_SPEECH -> {}
            MaterialSearchBar.BUTTON_BACK -> binding.msb.closeSearch()
        }
    }
}
