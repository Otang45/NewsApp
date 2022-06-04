package otang.news.model.articles;

import com.google.gson.annotations.SerializedName;

public class Source {

	@SerializedName("id")
	private Object id;

	@SerializedName("name")
	private String name;

	public void setId(Object id) {
		this.id = id;
	}

	public Object getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
