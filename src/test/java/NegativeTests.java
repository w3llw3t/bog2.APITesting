import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pojos.Book;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class NegativeTests extends BaseTest{
    @DataProvider
    public Object[][] bookData() {
        return new Object[][]{
                {"", "New Book Author", 2000, true},
                {"New Book Name",22 ,22, false }
        };
    }

    @Test(dataProvider = "bookData")
    //Добавление новой книги в библиотеку (негативный сценарий)
    public void createBookTestNegative(String name, String author, int year, boolean t) {
        // todo: создать книгу и проверить, что она находится в системе
        Book book = createBook(buildNewBook(name, author, year, t));
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
    @DataProvider(name = "invalidBookIds")
    public Object[][] getInvalidBookIds() {
        return new Object[][]{
                {"100"},
                {"abc123"},
                {"-1"}
        };
    }
    //получение информации о несуществующей книге по ее ID
    @Test(dataProvider = "invalidBookIds")
    public void getBookIdNegative(String bookId) {
        given()
                .pathParam("bookId", bookId)
                .when()
                .get("/api/books/{bookId}")
                .then()
                .statusCode(404)
                .assertThat()
                .body(equalTo(""));
    }
}