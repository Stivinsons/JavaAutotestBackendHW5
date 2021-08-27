package Homework5.Service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import Homework5.dto.GetCategoryResponse;

public interface CategoryService {

    @GET("categories/{id}")
    Call<GetCategoryResponse> getCategory(@Path("id") int id);
}