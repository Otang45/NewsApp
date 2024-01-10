package otang.app.news.model

import com.google.gson.annotations.SerializedName

class NewsResponse {
    @SerializedName("status")
    var status: String = ""

    @SerializedName("totalResults")
    var totalResults: Int = 0

    @SerializedName("articles")
    var articles: List<Articles> = ArrayList()
}
