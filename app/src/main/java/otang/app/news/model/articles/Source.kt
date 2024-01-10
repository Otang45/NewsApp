package otang.app.news.model.articles

import com.google.gson.annotations.SerializedName

class Source {
    @SerializedName("id")
    var id: Any = 0

    @SerializedName("name")
    var name: String = ""
}
