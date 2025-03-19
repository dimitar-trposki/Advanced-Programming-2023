package Septemvri.Kolokviumski.K15;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class Flight {

    String airportFrom;
    String airportTo;
    int time;
    int duration;

    public Flight(String airportFrom, String airportTo, int time, int duration) {
        this.airportFrom = airportFrom;
        this.airportTo = airportTo;
        this.time = time;
        this.duration = duration;
    }

    public String getAirportFrom() {
        return airportFrom;
    }

    public String getAirportTo() {
        return airportTo;
    }

    public int getTime() {
        return time;
    }

    public int getDuration() {
        return duration;
    }

    public String timeFromTo() {
        StringBuilder stringBuilder = new StringBuilder();
        int minutes = time % 60;
        int hours = time / 60;
        String timeFrom = String.format("%02d:%02d", hours, minutes);
        stringBuilder.append(timeFrom).append("-");
        minutes = (time + duration) % 60;
        hours = (time + duration) / 60;
        String timeTo = String.format("%02d:%02d", hours, minutes);
        if (hours >= 24) {
            int daysPlus = hours / 24;
            hours -= 24;
            timeTo = String.format("%02d:%02d +%dd", hours, minutes, daysPlus);
        }
        stringBuilder.append(timeTo);
        return stringBuilder.toString();
    }

    public String totalDuration() {
        int minutes = duration % 60;
        int hours = duration / 60;
        return String.format("%dh%02dm", hours, minutes);
    }

    @Override
    public String toString() {
        return String.format("%s %s", timeFromTo(), totalDuration());
    }

}

class Airport {

    String name;
    String country;
    String code;
    int passengers;

    Set<Flight> flights;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;

        this.flights = new TreeSet<>(Comparator.comparing(Flight::getAirportTo).thenComparing(Flight::getTime));
    }

    public void addFlightTo(String from, String to, int time, int duration) {
        flights.add(new Flight(from, to, time, duration));
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name).append(" (").append(code).append(")").append("\n");
        stringBuilder.append(country).append("\n");
        stringBuilder.append(passengers).append("\n");
        AtomicInteger i = new AtomicInteger(1);
        stringBuilder.append(flights.stream()
                .map(a -> String.format("%d. %s-%s %s", i.getAndIncrement(), code, a.getAirportTo(), a.toString()))
                .collect(Collectors.joining("\n")));
        return stringBuilder.toString();
    }

}

class Airports {

    Map<String, Airport> airports;
    //Map<String, Set<Flight>> airportsTo;
    List<Flight> flights;

    public Airports() {
        this.airports = new TreeMap<>();
        //this.airportsTo = new TreeMap<>();
        this.flights = new ArrayList<>();
    }

    public void addAirport(String name, String country, String code, int passengers) {
        airports.put(code, new Airport(name, country, code, passengers));
    }

    public void addFlights(String from, String to, int time, int duration) {
        airports.get(from).addFlightTo(from, to, time, duration);
//        airportsTo.putIfAbsent(to, new TreeSet<>(Comparator.comparing(Flight::getTime)));
//        airportsTo.get(to).add(new Flight(to, from, time, duration));
        flights.add(new Flight(from, to, time, duration));
    }

    public void showFlightsFromAirport(String code) {
        System.out.println(airports.get(code));
    }

    public void showDirectFlightsFromTo(String from, String to) {
        List<Flight> flightsFromTo = airports.get(from).flights.stream()
                .filter(f -> f.airportTo.equals(to))
                .collect(Collectors.toList());

        if (flightsFromTo.isEmpty()) {
            System.out.printf("No flights from %s to %s%n", from, to);
        } else {
            flightsFromTo.stream().forEach(f -> System.out.println(String.format("%s-%s %s", from, to, f)));
        }
    }

    public void showDirectFlightsTo(String to) {
//        airportsTo.get(to).stream()
//                .map(f -> String.format("%s-%s %s", f.getAirportTo(), to, f.toString()))
//                .forEach(System.out::println);
        flights.stream()
                .filter(f -> f.getAirportTo().equals(to))
                .sorted(Comparator.comparing(Flight::getTime).thenComparing(Flight::getAirportFrom))
                .forEach(f -> System.out.println(String.format("%s-%s %s", f.getAirportFrom(), to, f.toString())));
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