package otang.news.api;

import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.Retrofit;

public class RetrofitInstance {
	private static Retrofit retrofit = null;

	public static Retrofit getClient(String baseUrl) {
		if (retrofit == null) {
			retrofit = new Retrofit.Builder()
					.baseUrl(baseUrl)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
		}
		return retrofit;
	}
}