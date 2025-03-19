package Septemvri.Kolokviumski.K21;

import java.util.*;

class DuplicateNumberException extends Exception {

    public DuplicateNumberException(String number) {
        super(String.format("Duplicate number: [%s]", number));
    }
}

class Contact {

    String name;
    String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return name + " " + number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(name, contact.name) && Objects.equals(number, contact.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, number);
    }


}

class PhoneBook {

    Map<String, List<String>> names;
    Map<String, List<Contact>> numbers;

    public PhoneBook() {
        this.names = new TreeMap<>();
        this.numbers = new HashMap<>();
    }

    public void addContact(String name, String number) throws DuplicateNumberException {
        names.putIfAbsent(name, new ArrayList<>());
        if (names.get(name).contains(number)) {
            throw new DuplicateNumberException(number);
        }
        names.get(name).add(number);

        for (int i = 0; i < number.length(); i++) {
            for (int j = i + 3; j <= number.length(); j++) {
                numbers.putIfAbsent(number.substring(i, j), new ArrayList<>());
                numbers.get(number.substring(i, j)).add(new Contact(name, number));
            }
        }
    }

    public void contactsByNumber(String number) {
        if (!numbers.containsKey(number)) {
            System.out.println("NOT FOUND");
        } else {
            numbers.get(number).stream()
                    .sorted(Comparator.comparing(Contact::getName).thenComparing(Contact::getNumber))
                    .forEach(System.out::println);

        }
    }

    public void contactsByName(String name) {
        if (!names.containsKey(name)) {
            System.out.println("NOT FOUND");
        } else {
            names.get(name).stream()
                    .sorted()
                    .forEach(num -> System.out.println(String.format("%s %s", name, num)));
        }
    }

}

public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}