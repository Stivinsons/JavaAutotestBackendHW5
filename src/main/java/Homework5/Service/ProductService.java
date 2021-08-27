package Homework5.Service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import Homework5.dto.Product;

public interface ProductService {
    @POST("products")
    Call<Product> createProduct(@Body Product createProductRequest);

    @PUT("products")
    Call<Product> updateProduct(@Body Product createProductRequest);

    @GET("products/{id}")
    Call<ResponseBody> getProductInfo(@Path("id") int id);

    @DELETE("products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") int id);

}
