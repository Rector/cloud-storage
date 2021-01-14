import java.io.Serializable;

public class Book implements Serializable {
    private String titleBook;
    private String author;
    private int yearOfPublishing;

    public Book(String titleBook, String author, int yearOfPublishing){
        this.titleBook = titleBook;
        this.author = author;
        this.yearOfPublishing = yearOfPublishing;
    }

    public void info(){
        System.out.printf("Title of the book: %s || Author: %s || The year of publishing: %d", titleBook, author, yearOfPublishing);
    }
}
