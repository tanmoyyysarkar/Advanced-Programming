import java.util.ArrayList;

abstract class LibraryItem {
    protected String title;
    protected int year;
    private static int count = 0;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            this.title = "Unknown";
        } else {
            this.title = title;
        }
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        if (year > 0) {
            this.year = year;
        } else {
            this.year = 0;
        }
    }

    public LibraryItem(String title, int year) {
        setTitle(title);
        setYear(year);
        count++;
    }

    public static int getCount() {
        return count;
    }

    public abstract void displayInfo();
}

class Book extends LibraryItem {
    private String author;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        if (author == null || author.isBlank()) {
            this.author = "Unknown";
        } else {
            this.author = author;
        }
    }

    public Book(String title, int year, String author) {
        super(title, year);
        setAuthor(author);
    }

    @Override
    public void displayInfo() {
        System.out.println(getTitle() + " (" + getYear() + ") by " + getAuthor());
    }
}

class DVD extends LibraryItem {
    private int duration;
    private String genre;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        if (duration > 0) {
            this.duration = duration;
        } else {
            this.duration = 0;
        }
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        if (genre == null || genre.isBlank()) {
            this.genre = "Unknown";
        } else {
            this.genre = genre;
        }
    }

    public DVD(String title, int year, int duration, String genre) {
        super(title, year);
        setDuration(duration);
        setGenre(genre);
    }

    @Override
    public void displayInfo() {
        System.out.println(getTitle() + " (" + getYear() + "), " + getGenre() + ", " + getDuration() + " mins");
    }
}

class Main {
    public static void main(String[] args) {
        ArrayList<LibraryItem> items = new ArrayList<>();

        items.add(new Book("The Great Gatsby", 1925, "F. Scott Fitzgerald"));
        items.add(new Book("To Kill a Mockingbird", 1960, "Harper Lee"));
        items.add(new Book("1984", 1949, "George Orwell"));

        items.add(new DVD("Inception", 2010, 148, "Science Fiction"));
        items.add(new DVD("The Shawshank Redemption", 1994, 142, "Drama"));
        items.add(new DVD("Pulp Fiction", 1994, 154, "Crime"));

        System.out.println("------ LIBRARY ITEMS ------\n");
        for (LibraryItem item : items) {
            item.displayInfo();
        }

        System.out.println("\nTotal items in library: " + LibraryItem.getCount());
    }
}
