import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pojos.Book;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class PositiveTests extends BaseTest {


    @DataProvider
    public Object[][] books() {
        return new Object[][]{
                {"New Book Name", "New Book Author", 2000, true},
                {"BokBook", "Bok BookBook", 2005, false}
        };
    }

    @Test
    //получение информации о всех доступных книгах
    public void getAllBooks() {
        given()
                .when()
                .get("/api/books")
                .then()
                .statusCode(200);
    }
    //Добавление новой книги в библиотеку
    @Test(dataProvider = "books")
    public void createBookTest(String name, String author, int year, boolean b) {
        // todo: создать книгу и проверить, что она находится в системе
        Book book = createBook(buildNewBook(name, author, year, b));
        Book actual = getBookById(book.getId());

        //Проверка, что книга действительно была добавлена
        Assert.assertEquals(actual.getName(), book.getName());
    }
    //на получение кгиги по её id
    protected Book getBookById(int id) {
        return given()
                .when()
                .get("/api/books/" + id)
                .then()
                .statusCode(200)
                .extract().body().as(Book.class);
    }
    //изменение книги по её id
    @Test(dataProvider = "books")
    public void updateBookTest(String name, String author, int year, boolean w) {
        // todo: создать книгу с именем, затем обновить
        Book book = createBook(buildNewBook(name, author, year, w));
        updateBook(book);
    }
    private void updateBook(Book book) {
        Book book1 = new Book();
        book1.setName("SATTATA");
        given()
                .pathParam("id", book.getId())
                .body(book1.getName())
                .when()
                .put("/api/books/{id}")
                .then()
                .statusCode(200);
    }
    //получение информации о книге по ее ID
    @Test(dataProvider = "books")
    public void getBookId(String name, String author, int year, boolean r) {
        Book book = createBook(buildNewBook(name, author, year, r));
        given()
                .when()
                .get("/api/books/" + book.getId())
                .then()
                .statusCode(200)
                .body("name", equalTo(name))
                .body("author", equalTo(author))
                .body("year",equalTo(year));
    }
    //удаление книги по ее ID
    @Test(dataProvider = "books")
    public void deleteBookId(String name, String author, int year, boolean q) {
        Book book = createBook(buildNewBook(name, author, year, q));
        given()
                .when()
                .body(book)
                .delete("/api/books/" + book.getId())
                .then()
                .statusCode(200);
        //проверка, что книга действительно удалена
        Assert.assertEquals(book, equalTo(book));
    }
}