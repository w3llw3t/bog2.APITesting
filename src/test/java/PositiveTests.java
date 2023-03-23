import io.restassured.RestAssured;
import org.testng.annotations.Test;
import pojos.Book;

public class PositiveTests extends BaseTest {

    @Test
        //получение информации о всех доступных книгах
    void getAllBooks() {
        RestAssured
                .given()
                .when()
                .get("/api/books")
                .then()
                .statusCode(200);
    }
    @Test
        //Добавление новой книги в библиотеку
    void createBook() {
        Book book = new Book();
        book.setName("NAME");
        RestAssured
                .given()
                .when()
                .body(book)
                .post("/api/books")
                .then()
                .statusCode(201);
    }

    @Test
        //изменение информации о книге по ее ID
    void changeBookInfo() {
        Book changedBook = new Book("name","author",2000, false);
        RestAssured
                .given()
                .when()
                .body(changedBook)
                .put("/api/books/3")
                .then()
                .statusCode(200);
    }
    @Test
        //получение информации о книге по ее ID
    void getBookId() {
        RestAssured
                .given()
                .when()
                .get("/api/books/3")
                .then()
                .statusCode(200);
    }
    @Test
        //удаление книги по ее ID
    void deleteBook() {
        RestAssured
                .given()
                .when()
                .delete("/api/books/3")
                .then()
                .statusCode(200);
    }
}