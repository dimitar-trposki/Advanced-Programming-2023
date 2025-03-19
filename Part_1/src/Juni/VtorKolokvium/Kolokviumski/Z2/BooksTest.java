package Juni.VtorKolokvium.Kolokviumski.Z2;

import java.util.*;
import java.util.stream.Collectors;

class Book {
    String title;
    String category;
    float price;

    public Book(String title, String category, float price) {
        this.title = title;
        this.category = category;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public float getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) %.2f", title, category, price);
    }
}

class BookCollection {
    private Map<String, List<Book>> books;

    public BookCollection() {
        books = new HashMap<>();
    }

    public void addBook(Book book) {
        books.putIfAbsent(book.getCategory(), new ArrayList<>());
        books.get(book.getCategory()).add(book);
    }

    public void printByCategory(String category) {
        books.get(category)
                .stream()
                .sorted(Comparator.comparing(Book::getTitle).thenComparing(Book::getPrice))
                .forEach(System.out::println);
    }

    public List<Book> getCheapestN(int n) {
        List<Book> bookList = books.values().stream()
                .flatMap(b -> b.stream().sorted(Comparator.comparing(Book::getPrice)))
                .collect(Collectors.toList());
        return bookList.stream()
                .limit(n)
                .sorted(Comparator.comparing(Book::getPrice).thenComparing(Book::getTitle))
                .collect(Collectors.toList());
    }

}

public class BooksTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        BookCollection booksCollection = new BookCollection();
        Set<String> categories = fillCollection(scanner, booksCollection);
        System.out.println("=== PRINT BY CATEGORY ===");
        for (String category : categories) {
            System.out.println("CATEGORY: " + category);
            booksCollection.printByCategory(category);
        }
        System.out.println("=== TOP N BY PRICE ===");
        print(booksCollection.getCheapestN(n));
    }

    static void print(List<Book> books) {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    static TreeSet<String> fillCollection(Scanner scanner,
                                          BookCollection collection) {
        TreeSet<String> categories = new TreeSet<String>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            Book book = new Book(parts[0], parts[1], Float.parseFloat(parts[2]));
            collection.addBook(book);
            categories.add(parts[1]);
        }
        return categories;
    }
}