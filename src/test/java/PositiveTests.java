import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsIterableContaining.hasItem;
import static org.hamcrest.core.IsNull.notNullValue;

public class PositiveTests {

    private static final String BASE_URL = "http://localhost:5000";

    @DataProvider(name = "bookIdProvider")
    public Object[][] bookIdProvider() {
        return new Object[][]{
                {1},
                {2},
                {100}, // non-exciting
                {-5} // invalid value
        };
    }

    @DataProvider(name = "newBookProvider")
    public Object[][] newBookProvider() {
        return new Object[][]{
                {"{ \"name\": \"New Book 1\" }", "New Book 1"}, //valid
                {"{ \"name\": \"New Book 2\", \"author\": \"New Author\", \"year\": 2023, \"isElectronicBook\": true }","New Book 2"}, // valid
                {"{ \"author\": \"New Author\", \"year\": 2023 }", ""}, // invalid
        };
    }

    @DataProvider(name = "bookIdAndRequestBodyProvider")
    public Object[][] bookIdAndRequestBodyProvider() {
        return new Object[][]{
                {1, "{ \"name\": \"Updated Book 1\", \"author\": \"Updated Author\", \"year\": 2022, \"isElectronicBook\": false }"}, // valid
                {2, "{ \"name\": \"Updated Book 2\", \"author\": \"Updated Author\", \"year\": 2021, \"isElectronicBook\": true }"}, // valid
                {100, "{ \"name\": \"Updated Book 100\", \"author\": \"Updated Author\", \"year\": 2023, \"isElectronicBook\": true }"} // invalid
        };
    }

    @Test
    //получение информации о всех доступных книгах
    public void getAllBooks() {
        Response response = RestAssured.get(BASE_URL + "/api/books");
        Assert.assertEquals(response.getStatusCode(), 200);
    }
    //Добавление новой книги в библиотеку
    @Test(dataProvider = "newBookProvider")
    public void testAddNewBook(String requestBody, String expectedBookName) {
        // todo: создать книгу и проверить, что она находится в системе
        Response response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post(BASE_URL + "/api/books");
        Assert.assertEquals(response.getStatusCode(), 201);
        response.then().body("id", notNullValue());
        response.then().body("name", notNullValue());
        response.then().body("author", notNullValue());
        response.then().body("year", notNullValue());
        response.then().body("isElectronicBook", notNullValue());

        Response allBooksResponse = RestAssured.get(BASE_URL + "/api/books");
        if (!expectedBookName.isEmpty()) {
            allBooksResponse.then().body("name", hasItem(expectedBookName));
        }
    }
    //изменение книги по её id
    @Test(dataProvider = "bookIdAndRequestBodyProvider")
    public void testUpdateBookById(int bookId, String requestBody) {
        Response response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .put(BASE_URL + "/api/books/" + bookId);
        Assert.assertEquals(response.getStatusCode(), 200);
        response.then().body("id", equalTo(bookId));
        response.then().body("name", notNullValue());
        response.then().body("author", notNullValue());
        response.then().body("year", notNullValue());
        response.then().body("isElectronicBook", notNullValue());
    }
    //получение информации о книге по ее ID
    @Test(dataProvider = "bookIdProvider")
    public void getBookById(int bookId) {
        Response response = RestAssured.get(BASE_URL + "/api/books/" + bookId);
        Assert.assertEquals(response.getStatusCode(), 200);

    }
    //удаление книги по ее ID
    @Test(dataProvider = "bookIdProvider")
    public void testDeleteBookById(int bookId) {
        Response response = RestAssured.delete(BASE_URL + "/api/books/" + bookId);
        Assert.assertEquals(response.getStatusCode(), 200);
        //проверка, что книга действительно удалена
        response = RestAssured.get(BASE_URL + "/api/books/" + bookId);
        Assert.assertEquals(response.getStatusCode(), 404);
    }
}