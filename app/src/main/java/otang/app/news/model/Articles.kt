package otang.app.news.model

import com.google.gson.annotations.SerializedName
import otang.app.news.model.articles.Source

class Articles {
    @SerializedName("source")
    var source: Source = Source()

    @SerializedName("author")
    var author: String = ""

    @SerializedName("title")
    var title: String = ""

    @SerializedName("description")
    var description: String = ""

    @SerializedName("url")
    var url: String = ""

    @SerializedName("urlToImage")
    var urlToImage: String = ""

    @SerializedName("publishedAt")
    var publishedAt: String = ""

    @SerializedName("content")
    var content: String = ""
}
