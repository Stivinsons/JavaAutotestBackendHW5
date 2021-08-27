package Homework5;

import Homework5.Service.ProductService;
import Homework5.Util.RetrofitUtils;
import Homework5.dto.Product;
import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import retrofit2.Response;


import static org.hamcrest.MatcherAssert.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateProductTest {
    static ProductService productService;
    Product product;
    Faker faker = new Faker();

    static int id;
    static int price;
    static String title;
    static String categoryTitle;

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
    }

    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle("Food")
                .withPrice((int) (Math.random() * 10000));
    }

    @SneakyThrows
    @Test
    @Order(1)
    void CreateProductInFoodCategoryPositiveTest() {
        Response<Product> response = productService.createProduct(product)
                .execute();
        id = response.body().getId();
        price = response.body().getPrice();
        title = response.body().getTitle();
        categoryTitle = response.body().getCategoryTitle();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
    }


    @SneakyThrows
    @Test
    @Order(2)
    void GetProductByIdPositiveTest() {
        Response<ResponseBody> response = productService.getProductInfo(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
    }

    @SneakyThrows
    @Test
    @Order(3)
    void UpdateProductPriceByIDPositiveTest() {
        product = new Product()
                .withId(id)
                .withTitle(title)
                .withPrice((int) (Math.random() * 10000))
                .withCategoryTitle(categoryTitle);
        Response<Product> response = productService.updateProduct(product)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
    }

    @SneakyThrows
    @Test
    @Order(4)
    void GetProductByEmptyIdNegativeTest() {
        Response<ResponseBody> response = productService.getProductInfo(0).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.code(), CoreMatchers.is(404));
    }

    @SneakyThrows
    @Test
    @Order(5)
    void GetProductByNegativeIdNegativeTest() {
        Response<ResponseBody> response = productService.getProductInfo(-1).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.code(), CoreMatchers.is(404));
    }

    @SneakyThrows
    @Test
    @Order(6)
    void UpdateProductWithoutIDPositiveTest() {
        product = new Product()
                .withTitle(title)
                .withPrice((int) (Math.random() * 10000))
                .withCategoryTitle(categoryTitle);
        Response<Product> response = productService.updateProduct(product)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.code(), CoreMatchers.is(400));
    }

    @SneakyThrows
    @Test
    @Order(7)
    void UpdateProductWithoutTitlePositiveTest() {
        product = new Product()
                .withId(id)
                .withPrice((int) (Math.random() * 10000))
                .withCategoryTitle(categoryTitle);
        Response<Product> response = productService.updateProduct(product)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.code(), CoreMatchers.is(200));
    }

    @SneakyThrows
    @Test
    @Order(8)
    void UpdateProductWithoutCategoryTitlePositiveTest() {
        product = new Product()
                .withId(id)
                .withTitle(title)
                .withPrice((int) (Math.random() * 10000));
        Response<Product> response = productService.updateProduct(product)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.code(), CoreMatchers.is(500));
    }

    @SneakyThrows
    @AfterAll
    static void tearDown() {
        Response<ResponseBody> response = productService.deleteProduct(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
    }
}

