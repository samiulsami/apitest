package db;

public class book {
    public String title, authorName;
    public int bookID, pages,authorID;
    public book(){

    }
    public book(String title, String authorName, int bookID, int authorID, int pages){
        this.title = title;
        this.authorName = authorName;
        this.bookID  = bookID;
        this.authorID = authorID;
        this.pages = pages;
    }
}
