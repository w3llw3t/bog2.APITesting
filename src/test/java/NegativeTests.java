import io.restassured.RestAssured;
import org.testng.annotations.Test;
import pojos.Book;

public class NegativeTests extends BaseTest{

    //VALID VALUES ----------
    @Test
    //Добавление новой книги в библиотеку (негативный сценарий)
    void createBook() {
        Book book2 = new Book();
        book2.setAuthor("author");
        book2.setYear(2000);
        RestAssured
                .given()
                .when()
                .body(book2)
                .post("/api/books")
                .then()
                .statusCode(400);
    }
    @Test
        //изменение информации о книге (негативный сценарий)
    void changeBookInfo() {
        Book book2 = new Book();
        book2.setAuthor("Author");
        book2.setYear(2001);
        RestAssured
                .given()
                .when()
                .body(book2)
                .put("/api/books/2")
                .then()
                .statusCode(400);
    }

    //INVALID VALUES ----------
    @Test
    void createBookInvalid() {
        Book book = new Book();
        book.setName("BLA");
        book.setYear(2142);
        RestAssured
                .given()
                .when()
                .body(book)
                .post("/api/books")
                .then()
                .statusCode(201);
    }
}