package pojos;

public class Book {
    private String name;
    private String author;
    private Integer year;
    private boolean isElectronicBook;

    public Book(){
    }

    public Book(String name, String author, Integer year, boolean isElectronicBook) {
        this.name = name;
        this.author = author;
        this.year = year;
        this.isElectronicBook = isElectronicBook;
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

    public boolean isElectronicBook() {
        return isElectronicBook;
    }

    public void setElectronicBook(boolean electronicBook) {
        isElectronicBook = electronicBook;
    }
}