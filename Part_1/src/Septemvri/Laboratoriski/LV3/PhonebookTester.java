package Septemvri.Laboratoriski.LV3;

import java.util.*;
import java.util.stream.Collectors;

class InvalidNameException extends Exception {

    public String name;

    public InvalidNameException(String name) {
        super(name);
        this.name = name;
    }

}

class InvalidNumberException extends Exception {

    public InvalidNumberException() {
        super("InvalidNumberException");
    }
}

class MaximumSizeExceddedException extends Exception {

    public MaximumSizeExceddedException() {
        super("MaximumSizeExceddedException");
    }
}

class Contact implements Comparable<Contact> {

    private String name;
    private String[] phoneNumber;
    private int numOfContacts;

    public Contact(String name, String... phoneNumber) throws InvalidNameException, MaximumSizeExceddedException, InvalidNumberException {
        if (name.length() <= 4 || name.length() > 10 || !name.matches("[a-zA-Z0-9]+")) {
            throw new InvalidNameException(name);
        }
        for (String s : phoneNumber) {
            if (!checkNumber(s)) {
                throw new InvalidNumberException();
            }
        }
        if (phoneNumber.length > 5) {
            throw new MaximumSizeExceddedException();
        }

        this.name = name;
        this.phoneNumber = new String[phoneNumber.length];
        System.arraycopy(phoneNumber, 0, this.phoneNumber, 0, phoneNumber.length);
        this.numOfContacts = phoneNumber.length;
    }

    private boolean checkNumber(String number) {
        String[] validPrefixes = {"070", "071", "072", "075", "076", "077", "078"};
        if (number.length() != 9) {
            return false;
        }
        if (!number.matches("[0-9]+")) {
            return false;
        }
        for (String prefix : validPrefixes) {
            if (number.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public String[] getNumbers() {
        Arrays.sort(phoneNumber);
        return Arrays.copyOf(phoneNumber, phoneNumber.length);
    }

    public void addNumber(String phoneNumber) throws InvalidNumberException, MaximumSizeExceddedException {
        if (!checkNumber(phoneNumber)) {
            throw new InvalidNumberException();
        }

        if (numOfContacts >= 5) {
            throw new MaximumSizeExceddedException();
        }

        String[] phoneNumbers = new String[numOfContacts + 1];
        for (int i = 0; i < this.phoneNumber.length; i++) {
            phoneNumbers[i] = this.phoneNumber[i];
        }
        phoneNumbers[numOfContacts++] = phoneNumber;
        this.phoneNumber = phoneNumbers;
    }

    public static Contact valueOf(String s) throws InvalidNameException, InvalidNumberException, MaximumSizeExceddedException {
        return new Contact("Random", "random");
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Contact contact = (Contact) o;
//        return numOfContacts == contact.numOfContacts && Objects.equals(name, contact.name) && Objects.deepEquals(phoneNumber, contact.phoneNumber);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(name, Arrays.hashCode(phoneNumber), numOfContacts);
//    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        Arrays.sort(phoneNumber);

        stringBuilder.append(name).append("\n");
        stringBuilder.append(numOfContacts).append("\n");

        for (String s : phoneNumber) {
            stringBuilder.append(s).append("\n");
        }

        return stringBuilder.toString();
    }

    @Override
    public int compareTo(Contact o) {
        return this.name.compareToIgnoreCase(o.name);
    }

}

class PhoneBook {

    private Contact[] contacts;
    private int numOfContacts;

    public PhoneBook() {
        this.contacts = new Contact[0];
        this.numOfContacts = 0;
    }

    public void addContact(Contact contact) throws MaximumSizeExceddedException, InvalidNameException {
        if (numOfContacts > 250) {
            throw new MaximumSizeExceddedException();
        }

        for (int i = 0; i < numOfContacts; i++) {
            if (contacts[i].getName().equals(contact.getName())) {
                throw new InvalidNameException(contact.getName());
            }
        }

        Contact[] newArray = new Contact[numOfContacts + 1];
        for (int i = 0; i < numOfContacts; i++) {
            newArray[i] = contacts[i];
        }
        newArray[numOfContacts++] = contact;
        contacts = newArray;
        Arrays.sort(contacts);
    }

    public Contact getContactForName(String name) {
        for (Contact contact : contacts) {
            if (contact.getName().equals(name)) {
                return contact;
            }
        }
        return null;
    }

    public int numberOfContacts() {
        return numOfContacts;
    }

    public Contact[] getContacts() {
        return contacts;
    }

    public boolean removeContact(String name) {
        for (int i = 0; i < numOfContacts; i++) {
            if (contacts[i].getName().equals(name)) {

                for (int j = i; j < numOfContacts - 1; j++) {
                    contacts[j] = contacts[j + 1];
                }

                //contacts[numOfContacts - 1] = null;
                contacts = Arrays.copyOf(contacts, numOfContacts - 1);
                numOfContacts--;
                return true;
            }
        }
        return false;
    }

    public static boolean saveAsTextFile(PhoneBook phonebook, String path) {
        return false;
    }

    public static PhoneBook loadFromTextFile(String path) {
        return new PhoneBook();
    }

    public Contact[] getContactsForNumber(String number_prefix) {
        List<Contact> contactsWithPrefixList = new ArrayList<>();

        for (Contact contact : contacts) {
            for (String number : contact.getNumbers()) {
                if (number.startsWith(number_prefix)) {
                    contactsWithPrefixList.add(contact);
                    break;
                }
            }
        }

        Contact[] contactsWithPrefixArray = new Contact[contactsWithPrefixList.size()];
        contactsWithPrefixList.toArray(contactsWithPrefixArray);

        Arrays.sort(contactsWithPrefixArray);
        return contactsWithPrefixArray;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        PhoneBook phoneBook = (PhoneBook) o;
//        return numOfContacts == phoneBook.numOfContacts && Objects.deepEquals(contacts, phoneBook.contacts);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(Arrays.hashCode(contacts), numOfContacts);
//    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        Arrays.sort(contacts);

        for (Contact contact : contacts) {
            stringBuilder.append(contact).append("\n");
        }

        return stringBuilder.toString();
    }

}

public class PhonebookTester {

    public static void main(String[] args) throws Exception {
        Scanner jin = new Scanner(System.in);
        String line = jin.nextLine();
        switch (line) {
            case "test_contact":
                testContact(jin);
                break;
            case "test_phonebook_exceptions":
                testPhonebookExceptions(jin);
                break;
            case "test_usage":
                testUsage(jin);
                break;
        }
    }

    private static void testFile(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while (jin.hasNextLine())
            phonebook.addContact(new Contact(jin.nextLine(), jin.nextLine().split("\\s++")));
        String text_file = "phonebook.txt";
        PhoneBook.saveAsTextFile(phonebook, text_file);
        PhoneBook pb = PhoneBook.loadFromTextFile(text_file);
        if (!pb.equals(phonebook)) System.out.println("Your file saving and loading doesn't seem to work right");
        else System.out.println("Your file saving and loading works great. Good job!");
    }

    private static void testUsage(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while (jin.hasNextLine()) {
            String command = jin.nextLine();
            switch (command) {
                case "add":
                    phonebook.addContact(new Contact(jin.nextLine(), jin.nextLine().split("\\s++")));
                    break;
                case "remove":
                    phonebook.removeContact(jin.nextLine());
                    break;
                case "print":
                    System.out.println(phonebook.numberOfContacts());
                    System.out.println(Arrays.toString(phonebook.getContacts()));
                    System.out.println(phonebook.toString());
                    break;
                case "get_name":
                    System.out.println(phonebook.getContactForName(jin.nextLine()));
                    break;
                case "get_number":
                    System.out.println(Arrays.toString(phonebook.getContactsForNumber(jin.nextLine())));
                    break;
            }
        }
    }

    private static void testPhonebookExceptions(Scanner jin) {
        PhoneBook phonebook = new PhoneBook();
        boolean exception_thrown = false;
        try {
            while (jin.hasNextLine()) {
                phonebook.addContact(new Contact(jin.nextLine()));
            }
        } catch (InvalidNameException e) {
            System.out.println(e.name);
            exception_thrown = true;
        } catch (Exception e) {
        }
        if (!exception_thrown) System.out.println("Your addContact method doesn't throw InvalidNameException");
        /*
		exception_thrown = false;
		try {
		phonebook.addContact(new Contact(jin.nextLine()));
		} catch ( MaximumSizeExceddedException e ) {
			exception_thrown = true;
		}
		catch ( Exception e ) {}
		if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw MaximumSizeExcededException");
        */
    }

    private static void testContact(Scanner jin) throws Exception {
        boolean exception_thrown = true;
        String names_to_test[] = {"And\nrej", "asd", "AAAAAAAAAAAAAAAAAAAAAA", "Ð�Ð½Ð´Ñ€ÐµÑ˜A123213", "Andrej#", "Andrej<3"};
        for (String name : names_to_test) {
            try {
                new Contact(name);
                exception_thrown = false;
            } catch (InvalidNameException e) {
                exception_thrown = true;
            }
            if (!exception_thrown) System.out.println("Your Contact constructor doesn't throw an InvalidNameException");
        }
        String numbers_to_test[] = {"+071718028", "number", "078asdasdasd", "070asdqwe", "070a56798", "07045678a", "123456789", "074456798", "073456798", "079456798"};
        for (String number : numbers_to_test) {
            try {
                new Contact("Andrej", number);
                exception_thrown = false;
            } catch (InvalidNumberException e) {
                exception_thrown = true;
            }
            if (!exception_thrown)
                System.out.println("Your Contact constructor doesn't throw an InvalidNumberException");
        }
        String nums[] = new String[10];
        for (int i = 0; i < nums.length; ++i) nums[i] = getRandomLegitNumber();
        try {
            new Contact("Andrej", nums);
            exception_thrown = false;
        } catch (MaximumSizeExceddedException e) {
            exception_thrown = true;
        }
        if (!exception_thrown)
            System.out.println("Your Contact constructor doesn't throw a MaximumSizeExceddedException");
        Random rnd = new Random(5);
        Contact contact = new Contact("Andrej", getRandomLegitNumber(rnd), getRandomLegitNumber(rnd), getRandomLegitNumber(rnd));
        System.out.println(contact.getName());
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
    }

    static String[] legit_prefixes = {"070", "071", "072", "075", "076", "077", "078"};
    static Random rnd = new Random();

    private static String getRandomLegitNumber() {
        return getRandomLegitNumber(rnd);
    }

    private static String getRandomLegitNumber(Random rnd) {
        StringBuilder sb = new StringBuilder(legit_prefixes[rnd.nextInt(legit_prefixes.length)]);
        for (int i = 3; i < 9; ++i)
            sb.append(rnd.nextInt(10));
        return sb.toString();
    }


}
