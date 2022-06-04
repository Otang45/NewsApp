package otang.news.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NewsResponse {

	@SerializedName("status")
	private String status;

	@SerializedName("totalResults")
	private Integer totalResults;

	@SerializedName("articles")
	private List<Articles> articles;

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}

	public Integer getTotalResults() {
		return totalResults;
	}

	public void setArticles(List<Articles> articles) {
		this.articles = articles;
	}

	public List<Articles> getArticles() {
		return articles;
	}

}
