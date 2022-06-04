package otang.news.api;

public class ApiClient {
	public static final String BASE_URL = "https://newsapi.org";

	public static ApiInterface getApiInterface() {
		return RetrofitInstance.getClient(BASE_URL).create(ApiInterface.class);
	}
}