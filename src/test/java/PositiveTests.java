import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pojos.Book;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

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
                {"{ \"name\": \"New Book 1\" }"}, //valid
                {"{ \"name\": \"New Book 2\", \"author\": \"New Author\", \"year\": 2023, \"isElectronicBook\": true }"}, // valid
                {"{ \"author\": \"New Author\", \"year\": 2023 }"}, // invalid
        };
    }

    @DataProvider(name = "bookIdAndRequestBodyProvider")
    public Object[][] bookIdAndRequestBodyProvider() {
        return new Object[][]{
                {1, "{ \"name\": \"Updated Book 1\", \"author\": \"Updated Author\", \"year\": 2022, \"isElectronicBook\": false }","Updated Book 1"}, // valid
                {2, "{ \"name\": \"Updated Book 2\", \"author\": \"Updated Author\", \"year\": 2021, \"isElectronicBook\": true }","Updated Book 2"}, // valid
                {100, "{ \"name\": \"Updated Book 100\", \"author\": \"Updated Author\", \"year\": 2023, \"isElectronicBook\": true }",""} // invalid
        };
    }

    @Test
    //получение информации о всех доступных книгах
    public void getAllBooks() {
        Response response = RestAssured.given()
                .get(BASE_URL + "/api/books");
        response.then()
                .log().all() // логирование ответа
                .assertThat()
                .statusCode(200);
    }
    //Добавление новой книги в библиотеку
    @Test(dataProvider = "newBookProvider")
    public void testAddNewBook(String requestBody) {
        // todo: создать книгу и проверить, что она находится в системе
        Response response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .log().all()
                .post(BASE_URL + "/api/books");
        response.then().statusCode(201);

        // Получаем информацию о добавленной книге из ответа на POST-запрос
        Book newBook = response.as(Book.class);

        // Проверяем, что книга действительно добавлена
        response.then().assertThat().body("name", equalTo(newBook.getName()));
        response.then().assertThat().body("author", equalTo(newBook.getAuthor()));
        response.then().assertThat().body("year", equalTo(newBook.getYear()));
        response.then().assertThat().body("isElectronicBook", equalTo(newBook.getIsElectronicBook()));
    }

    //изменение книги по её id
    @Test(dataProvider = "bookIdAndRequestBodyProvider")
    public void testUpdateBookById(int bookId, String requestBody, String updatedBookName) {
        Response response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .log().all()
                .put(BASE_URL + "/api/books/" + bookId);
        response.then().statusCode(200);
        // Получаем информацию о добавленной книге из ответа на POST-запрос
        Book updatedBook = response.as(Book.class);

        //проверка, что книга действительно изменена
        response.then().assertThat().body("name", equalTo(updatedBook.getName()));
        response.then().assertThat().body("author", equalTo(updatedBook.getAuthor()));
        response.then().assertThat().body("year", equalTo(updatedBook.getYear()));
        response.then().assertThat().body("isElectronicBook", equalTo(updatedBook.getIsElectronicBook()));
    }
    //получение информации о книге по ее ID
    @Test(dataProvider = "bookIdProvider")
    public void getBookById(int bookId) {
        Response response = given()
                .get(BASE_URL + "/api/books" + bookId);
        response.then()
                .log().all() // логирование ответа
                .assertThat()
                .statusCode(200);
        // Получаем информацию о добавленной книге из ответа на POST-запрос
        Book bookIds = response.as(Book.class);

        //проверка, что книга актуальности информации о книге
        response.then().body("id", equalTo(bookId));
        response.then().body("name", equalTo(bookIds.getName()));
        response.then().body("author", equalTo(bookIds.getAuthor()));
        response.then().body("year", equalTo(bookIds.getYear()));
        response.then().body("isElectronicBook", equalTo(bookIds.getIsElectronicBook()));

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