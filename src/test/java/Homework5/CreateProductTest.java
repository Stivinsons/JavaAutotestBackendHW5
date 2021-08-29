package Homework5;

import Homework5.Service.CategoryService;
import Homework5.Service.ProductService;
import Homework5.Util.ConnectDB;
import Homework5.Util.RetrofitUtils;
import Homework5.dto.Product;
import com.github.javafaker.Faker;
import db.model.Products;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import retrofit2.Retrofit;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateProductTest {
    static ProductService productService;
    static CategoryService categoryService;
    Product product;
    Faker faker = new Faker();
    static db.dao.ProductsMapper productsMapper;
    static Retrofit client;
    static Products dbProduct;

    static int id;
    static int price;
    static String title;
    static String categoryTitle;

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
        client = RetrofitUtils.getRetrofit();
        productsMapper = ConnectDB.getProductsMapper();
        productService = client.create(ProductService.class);
        categoryService = client.create(CategoryService.class);
        dbProduct = ConnectDB.selectProductById(productsMapper, id);
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
        Integer countProductsBefore = ConnectDB.countProducts(productsMapper);

        Response<Product> response = productService.createProduct(product)
                .execute();

        //Получение переменны для тестирвоания
        id = response.body().getId();
        price = response.body().getPrice();
        title = response.body().getTitle();
        categoryTitle = response.body().getCategoryTitle();

        //Тестирование ответа сервера
        assertThat(response.isSuccessful(), CoreMatchers.is(true));

        //Тестиование записи в БД
        assertThat(title, equalTo(dbProduct.getTitle()));
        assertThat(price, equalTo(dbProduct.getPrice()));
        Integer countProductsAfter = ConnectDB.countProducts(productsMapper);
        assertThat(countProductsBefore, equalTo(countProductsAfter));
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

        //Тестирование ответа сервера
        assertThat(response.isSuccessful(), CoreMatchers.is(true));

        //Тестирование обновление записи в БД
        assertThat(price, equalTo(dbProduct.getPrice()));
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

        //Тестирвние ответа сервера
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.code(), CoreMatchers.is(200));

        //Тестирвоание записи в БД
        assertThat(price, equalTo(dbProduct.getPrice()));
    }

    @SneakyThrows
    @Test
    @Order(8)
    void UpdateProductWithoutCategoryTitleNegativeTest() {
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

        //тестирование ответа сервера
        assertThat(response.isSuccessful(), CoreMatchers.is(true));

        //Тестирвоание записи в БД
        assertThat(dbProduct, equalTo(null));
    }
}

