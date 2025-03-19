package Juni.VtorKolokvium.Kolokviumski.Z15;

import com.sun.source.tree.Tree;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class Flight {
    String from;
    String to;
    int time;
    int duration;

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
    }

    public String departureTime() {
        int hour = time / 60;
        int minute = time % 60;
        return String.format("%02d:%02d", hour, minute);
    }

    private String arrivalTime() {
        int totalMinutes = time + duration;
        int hour = totalMinutes / 60;
        int minute = totalMinutes % 60;
        if (hour >= 24) {
            return String.format("%02d:%02d +%dd", hour - 24, minute, hour / 24);
        }
        return String.format("%02d:%02d", hour, minute);
    }

    private String durationTime() {
        int hour = duration / 60;
        int minute = duration % 60;
        return String.format("%dh%02dm", hour, minute);
    }

    public int getTime() {
        return time;
    }

    public int getDuration() {
        return duration;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    @Override
    public String toString() {
        return String.format("%s-%s %s-%s %s", from, to, departureTime(), arrivalTime(), durationTime());
    }
}

class Airport {
    String name;
    String country;
    String code;
    int passengers;
    List<Flight> flights;

    public List<Flight> getFlights() {
        return flights;
    }

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
        flights = new ArrayList<>();
        //flights = new TreeSet<>(Comparator.comparing(Flight::getTo).thenComparing(Flight::departureTime));
    }

    public void addFlight(Flight flight) {
        flights.add(flight);
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getCode() {
        return code;
    }

    public int getPassengers() {
        return passengers;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)\n%s\n%d\n", name, code, country, passengers);
    }
}

class Airports {
    List<Airport> airports;

    public Airports() {
        airports = new ArrayList<>();
    }

    public void addAirport(String name, String country, String code, int passengers) {
        airports.add(new Airport(name, country, code, passengers));
    }

    public void addFlights(String from, String to, int time, int duration) {
        Airport airport = airports.stream()
                .filter(a -> a.getCode().equals(from))
                .findAny().get();
        airport.addFlight(new Flight(from, to, time, duration));
    }

    private Airport getAirport(String code) {
        return airports.stream()
                .filter(a -> a.getCode().equals(code))
                .findAny().get();
    }

    public void showFlightsFromAirport(String code) {
        Airport airport = getAirport(code);
        System.out.printf(airport.toString());
        AtomicInteger counter = new AtomicInteger(1);
        airport.getFlights().stream()
                .sorted(Comparator.comparing(Flight::getTo).thenComparing(Flight::getTime))
                .forEach(a -> System.out.println(counter.getAndIncrement() + ". " + a.toString()));
    }

    public void showDirectFlightsFromTo(String from, String to) {
        Airport airport = getAirport(from);

        List<Flight> ap = airport.getFlights()
                .stream()
                .filter(a -> a.getTo().equals(to))
                .sorted(Comparator.comparing(Flight::getTo).thenComparing(Flight::getTime))
                .collect(Collectors.toList());

        if (ap.isEmpty()) {
            System.out.println("No flights from " + from + " to " + to);
        } else {
            ap.forEach(System.out::println);
        }
    }

    public void showDirectFlightsTo(String to) {

//        Set<Flight> directFlights = airports.stream()
//                .flatMap(a -> a.getFlights().stream()
//                        .filter(airport -> airport.getTo().equals(to)))
//                .filter(flight -> flight.getTo().equals(to))
//                .collect(Collectors.toCollection(() -> new TreeSet<>(comparator)));

        List<Flight> directFlights = airports.stream()
                .flatMap(a -> a.getFlights().stream())
                .filter(flight -> flight.getTo().equals(to))
                .collect(Collectors.toList());

        directFlights = directFlights.stream().sorted(Comparator.comparing(Flight::getTo).thenComparing(Flight::getTime)).collect(Collectors.toList());
        directFlights.forEach(System.out::println);
    }

}

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}