package ZimskiSemestar.PrvKolokvium.Laboratoriski.LV2;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

abstract class Contact {
    private String date;

    public Contact(String date) {
        this.date = date;
    }

    public boolean isNewerThan(Contact c) {
        return this.date.compareTo(c.date) > 0;
    }

    public abstract String getType();
}

class EmailContact extends Contact {

    private String email;

    public EmailContact(String date, String email) {
        super(date);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getType() {
        return "Email";
    }

    @Override
    public String toString() {
        return email;
    }
}

enum Operator {
    VIP,
    ONE,
    TMOBILE
}

class PhoneContact extends Contact {

    private String phone;
    private Operator operator;

    public PhoneContact(String date, String phone) {
        super(date);
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public Operator getOperator() {
        if (phone.charAt(2) == '0' || phone.charAt(2) == '1' || phone.charAt(2) == '2') {
            return Operator.TMOBILE;
        } else if (phone.charAt(2) == '5' || phone.charAt(2) == '6') {
            return Operator.ONE;
        } else {
            return Operator.VIP;
        }
    }

    @Override
    public String getType() {
        return "Phone";
    }

    @Override
    public String toString() {
        return phone;
    }
}

class Student {

    private String firstName;
    private String lastName;
    private String city;
    private int age;
    private long index;
    private Contact[] contacts;
    private int position;
    private int emailContacts;
    private int phoneContacts;

    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
        this.contacts = new Contact[100];
        position = 0;
    }

    public void addEmailContact(String date, String email) {
        contacts[position++] = new EmailContact(date, email);
        emailContacts++;
    }

    public void addPhoneContact(String date, String phone) {
        contacts[position++] = new PhoneContact(date, phone);
        phoneContacts++;
    }

    public Contact[] getEmailContacts() {
        return Arrays.stream(contacts, 0, position)
                .filter(c -> c != null && c.getType().equals("Email"))
                .toArray(Contact[]::new);
//        Contact[] temp = new Contact[100];
//        int pos = 0;
//        for (int i = 0; i < contacts.length; i++) {
//            if (contacts[i] == null) {
//                break;
//            } else if (contacts[i].getType().equals("Email") && contacts[i] != null) {
//                temp[pos++] = contacts[i];
//            }
//        }
//        return temp;
    }

    public Contact[] getPhoneContacts() {
        return Arrays.stream(contacts, 0, position)
                .filter(c -> c != null && c.getType().equals("Phone"))
                .toArray(Contact[]::new);
//        Contact[] temp = new Contact[100];
//        int pos = 0;
//        for (int i = 0; i < contacts.length; i++) {
//            if (contacts[i] == null) {
//                break;
//            } else if (contacts[i].getType().equals("Phone")) {
//                temp[pos++] = contacts[i];
//            }
//        }
//        return temp;
    }

    public int getPosition() {
        return position;
    }

    public String getCity() {
        return city;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public long getIndex() {
        return index;
    }

    public Contact getLatestContact() {
        Contact toReturn = contacts[0];
        for (int i = 0; i < position; i++) {
            if (contacts[i].isNewerThan(toReturn) && contacts[i] != null) {
                toReturn = contacts[i];
            }
        }
        return toReturn;
    }

//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("{\"ime\":" + "\"").append(firstName).append("\",");
//        sb.append("\"prezime\":" + "\"").append(lastName).append("\",");
//        sb.append("\"vozrast\":" + "\"").append(age).append("\",");
//        sb.append("\"grad\":" + "\"").append(city).append("\",");
//        sb.append("\"indeks\":" + "\"").append(index).append("\",");
//        sb.append("\"telefonskiKontakti\":");
//        sb.append("[");
//        Contact[] phoneContacts = getPhoneContacts();
//        sb.append("\"" + phoneContacts[0].toString() + "\"");
//        for (int i = 1; i < phoneContacts.length; i++) {
//            sb.append(", " + "\"").append(phoneContacts[i].toString()).append("\"");
//        }
//        sb.append("]");
//        sb.append("\"emailKontakti\":");
//        sb.append("[");
//        Contact[] emailContacts = getEmailContacts();
//        sb.append("\"" + emailContacts[0].toString() + "\"");
//        for (int i = 1; i < emailContacts.length; i++) {
//            sb.append(", " + "\"").append(emailContacts[i].toString()).append("\"");
//        }
//        sb.append("]");
//        sb.append("}");
//        return sb.toString();
//    }

    @Override
    public String toString() {
        String phoneContacts = Arrays.stream(getPhoneContacts())
                .map(Contact::toString)
                .collect(Collectors.joining("\", \"", "\"", "\""));
        String emailContacts = Arrays.stream(getEmailContacts())
                .map(Contact::toString)
                .collect(Collectors.joining("\", \"", "\"", "\""));
        if (phoneContacts.length() > 11 && emailContacts.length() > 11) {
            return String.format("{\"ime\":\"%s\", \"prezime\":\"%s\", \"vozrast\":%d, \"grad\":\"%s\", \"indeks\":%d, \"telefonskiKontakti\":[%s], \"emailKontakti\":[%s]}",
                    firstName, lastName, age, city, index, phoneContacts, emailContacts);
        } else if (phoneContacts.length() > 11 && emailContacts.length() < 11) {
            return String.format("{\"ime\":\"%s\", \"prezime\":\"%s\", \"vozrast\":%d, \"grad\":\"%s\", \"indeks\":%d, \"telefonskiKontakti\":[%s], \"emailKontakti\":[]}",
                    firstName, lastName, age, city, index, phoneContacts);
        } else {
            return String.format("{\"ime\":\"%s\", \"prezime\":\"%s\", \"vozrast\":%d, \"grad\":\"%s\", \"indeks\":%d, \"telefonskiKontakti\":[], \"emailKontakti\":[%s]}",
                    firstName, lastName, age, city, index, emailContacts);
        }
    }
}

class Faculty {

    private String name;
    private Student[] students;
    private int position;

    public Faculty(String name, Student[] students) {
        this.name = name;
        this.students = new Student[100];
        position = 0;
        for (Student student : students) {
            if (student == null) {
                break;
            }
            this.students[position++] = student;
        }
    }

    public int countStudentsFromCity(String cityName) {
        //return (int) Arrays.stream(students).filter(s -> s.getCity().equals(cityName)).count();
        int counter = 0;
        for (int i = 0; i < position; i++) {
            if (students[i].getCity().equals(cityName)) {
                counter++;
            }
        }
        return counter;
    }

    public Student getStudent(long index) {
        //return Arrays.stream(students).filter(s -> s.getIndex() == index).findFirst().get();
        for (int i = 0; i < position; i++) {
            if (students[i].getIndex() == index) {
                return students[i];
            }
        }
        return null;
    }

    public Student getStudentWithMostContacts() {
        Student student = students[0];
        for (int i = 1; i < position; i++) {
            if (students[i].getPosition() > student.getPosition()) {
                student = students[i];
            } else if (students[i].getPosition() == student.getPosition()) {
                if (students[i].getIndex() > student.getIndex()) {
                    student = students[i];
                }
            }
        }
        return student;
    }

    public double getAverageNumberOfContacts() {
        DoubleSummaryStatistics stats = Arrays.stream(students, 0, position)
                .mapToDouble(Student::getPosition)
                .summaryStatistics();
        return stats.getAverage();
//        int sum = 0;
//        for (int i = 0; i < position; i++) {
//            sum += students[i].getPosition();
//        }
//        return (double) sum / students.length;
    }

//    @Override
//    public String toString() {
//        return "{\"fakultet\":\"" + name + "\", "
//                + "\"studenti\":[" + Arrays.toString(students) +
//                "]}";
//    }

    @Override
    public String toString() {
        String studentsJson = Arrays.stream(students, 0, position)
                .map(Student::toString)
                .collect(Collectors.joining(", "));
        return String.format("{\"fakultet\":\"%s\", \"studenti\":[%s]}", name, studentsJson);
    }
}

public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
