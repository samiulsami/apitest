package db;

public class book {
    private final String title, authorName;
    private final int bookID, pages,authorID;
    public static class Builder{
        private String title, authorName;
        private int bookID=-1, authorID=-1,pages=-1;

        public Builder title(String s){
            this.title = s;
            return this;
        }
        public Builder bookID(int s){
            this.bookID = s;
            return this;
        }
        public Builder authorName(String s){
            this.authorName = s;
            return this;
        }
        public Builder authorID(int s){
            this.authorID = s;
            return this;
        }
        public Builder pages(int s){
            this.pages= s;
            return this;
        }

        public book build()throws bookErrorException{
            if(title == null || authorName == null || bookID == -1 || pages == -1 || authorID == -1){
                throw new bookErrorException("Book requires the following fields: title, authorName, bookID, authorID, pages");
            }
            return new book(this);
        }
    }

    private book(Builder builder){
        title = builder.title;
        authorName = builder.authorName;
        bookID  = builder.bookID;
        authorID = builder.authorID;
        pages = builder.pages;
    }
}
