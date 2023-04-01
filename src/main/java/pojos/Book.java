package pojos;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Book {
    @SerializedName("name")
    private String name;
    @SerializedName("author")
    private String author;
    @SerializedName("year")
    private Integer year;
    @SerializedName("isElectronicBook")
    private boolean isElectronicBook;

    public void setElectronicBook(boolean electronicBook) {
        isElectronicBook = electronicBook;
    }

    public boolean getIsElectronicBook() {
        return isElectronicBook;
    }

    public Book(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return isElectronicBook == book.isElectronicBook && Objects.equals(name, book.name) && Objects.equals(author, book.author) && Objects.equals(year, book.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, author, year, isElectronicBook);
    }
}