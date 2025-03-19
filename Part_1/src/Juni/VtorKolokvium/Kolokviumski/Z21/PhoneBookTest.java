package Juni.VtorKolokvium.Kolokviumski.Z21;

import java.util.*;
import java.util.stream.Collectors;

class DuplicateNumberException extends Exception {
    public DuplicateNumberException(String number) {
        super("Duplicate number: " + number);
    }
}

class Contact implements Comparable<Contact> {
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
    public int compareTo(Contact o) {
        int result = name.compareTo(o.name);
        if (result == 0) {
            result = number.compareTo(o.number);
        }
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s %s", name, number);
    }
}

class PhoneBook {
    Set<Contact> allContacts;
    Map<String, Set<Contact>> contactsByNumber;
    Map<String, Set<Contact>> contactsByName;

    public PhoneBook() {
        allContacts = new HashSet<>();
        contactsByNumber = new HashMap<>();
        contactsByName = new HashMap<>();
    }

    private List<String> subNumbers(String number) {
        List<String> result = new ArrayList<>();
        for (int len = 3; len <= number.length(); len++) {
            for (int i = 0; i <= number.length() - len; i++) {
                result.add(number.substring(i, i + len));
            }
        }
        return result;
    }

    void addContact(String name, String number) throws DuplicateNumberException {
        Contact c = new Contact(name, number);
        if (allContacts.contains(c)) {
            throw new DuplicateNumberException(number);
        }
        allContacts.add(c);

        List<String> subNumbers = subNumbers(number);
        for (String subNumber : subNumbers) {
            contactsByNumber.putIfAbsent(subNumber, new TreeSet<>());
            contactsByNumber.get(subNumber).add(c);
        }

        contactsByName.putIfAbsent(name, new TreeSet<>());
        contactsByName.get(name).add(c);
    }

    void contactsByNumber(String number) {
        Set<Contact> contacts = contactsByNumber.get(number);
        if (contacts == null) {
            System.out.println("NOT FOUND");
            return;
        }
        contacts.forEach(System.out::println);
    }

    void contactsByName(String name) {
        Set<Contact> contacts = contactsByName.get(name);
        if (contacts == null) {
            System.out.println("NOT FOUND");
            return;
        }
        contacts.forEach(System.out::println);
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
