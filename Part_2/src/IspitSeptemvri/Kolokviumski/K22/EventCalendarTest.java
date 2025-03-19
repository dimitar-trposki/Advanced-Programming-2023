package IspitSeptemvri.Kolokviumski.K22;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

class WrongDateException extends Exception {

    public WrongDateException(Date date) {
        super(String.format("Wrong date: %s", date));
    }

}

class Event implements Comparable<Event> {

    String name;
    String location;
    Date date;
    LocalDateTime localDateTime;

    public Event(String name, String location, Date date) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Date getDate() {
        return date;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    @Override
    public String toString() {
        String month = String.valueOf(localDateTime.getMonth());
        month = month.substring(0, 3);
        month = month.toLowerCase();
        month = String.valueOf(Character.toUpperCase(month.charAt(0))).concat(month.substring(1, 3));
        return String.format("%d %s, %02d %02d:%02d at %s, %s",
                localDateTime.getDayOfMonth(),
                month,
                localDateTime.getYear(),
                localDateTime.getHour(),
                localDateTime.getMinute(),
                location, name);
    }

    @Override
    public int compareTo(Event o) {
        Comparator<Event> comparator = Comparator.comparing(Event::getDate).thenComparing(Event::getName);
        return comparator.compare(this, o);
    }
}

class EventCalendar {

    int year;
    List<Event> events;

    public EventCalendar(int year) {
        this.year = year;
        this.events = new ArrayList<>();
    }

    public void addEvent(String name, String location, Date date) throws WrongDateException, ParseException {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date isBefore = df.parse(String.format("01.01.%d 00:00", year));
        Date isAfter = df.parse(String.format("31.12.%d 23:59", year));
        if (date.before(isBefore) || date.after(isAfter)) {
            throw new WrongDateException(date);
        }
        events.add(new Event(name, location, date));
    }

    public void listEvents(Date date) {
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        List<Event> listOfEvents = events.stream()
                .filter(e -> e.localDateTime.getDayOfMonth() == localDateTime.getDayOfMonth() && e.localDateTime.getMonth() == localDateTime.getMonth())
                .sorted()
                .collect(Collectors.toList());
        if (listOfEvents.isEmpty()) {
            System.out.println("No events on this day!");
        } else {
            listOfEvents.stream().forEach(System.out::println);
        }
    }

    public void listByMonth() {
        Map<Integer, Long> listByMonth = new TreeMap<>();
        for (int i = 1; i <= 12; i++) {
            listByMonth.putIfAbsent(i, 0l);
        }
        Map<Integer, Long> listByMonthWithEvents = events.stream()
                .collect(Collectors.groupingBy(
                        e -> e.localDateTime.getMonth().getValue(),
                        Collectors.counting()
                ));
        for (Map.Entry<Integer, Long> entry : listByMonthWithEvents.entrySet()) {
            listByMonth.put(entry.getKey(), entry.getValue());
        }
        listByMonth.entrySet().forEach(entry -> {
            System.out.println(String.format("%d : %d", entry.getKey(), entry.getValue()));
        });
    }

}

public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            Date date = df.parse(parts[2]);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        Date date = df.parse(scanner.nextLine());
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}