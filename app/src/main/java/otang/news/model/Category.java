package otang.news.model;

public class Category {
	private int image;
	private String category;

	public Category(int image, String category) {
		this.image = image;
		this.category = category;
	}

	public int getImage() {
		return image;
	}

	public String getCategory() {
		return category;
	}
}