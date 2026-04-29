import java.util.ArrayList;
import java.util.Scanner;

public class BookSearch {
    public static void main(String[] args) {
        ArrayList<String> books = new ArrayList<>();
        books.add("The Alchemist");
        books.add("Atomic Habits");
        books.add("Clean Code");
        books.add("The Pragmatic Programmer");
        books.add("Harry Potter and the Philosopher's Stone");

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter word to search in titles: ");
        String keyword = sc.nextLine().toLowerCase();

        System.out.println("\nBooks containing \"" + keyword + "\":");
        boolean found = false;

        for(String book : books){
            if(book.toLowerCase().contains(keyword)) {
                System.out.println(book);
                found = true;
            }
        }

        if(!found){
            System.out.println("No matching books found.");
        }

        sc.close();
    }
}
