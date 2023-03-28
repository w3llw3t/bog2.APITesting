import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojos.Book;

import static io.restassured.RestAssured.given;

public abstract class BaseTest {
    @BeforeClass
    public void prepare(){
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri("http://localhost:5000")
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
        RestAssured.filters(new ResponseLoggingFilter());
    }
    protected Book buildNewBook(String name, String author, int year, boolean b) {
        // todo: создать объект с тестовыми данными
        Book book = new Book();
        book.setName(name);
        book.setAuthor(author);
        book.setYear(year);
        book.isElectronicBook(false);
        return book;
    }
    @Test
    protected Book createBook(Book book) {
        // todo: отправить HTTP запрос для создания книги
        return given()
                .body(book)
                .when()
                .post("/api/books")
                .then()
                .statusCode(201)
                .extract()
                .body()
                .as(Book.class);
    }
}