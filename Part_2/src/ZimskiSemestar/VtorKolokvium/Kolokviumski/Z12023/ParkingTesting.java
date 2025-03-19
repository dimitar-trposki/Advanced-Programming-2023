package ZimskiSemestar.VtorKolokvium.Kolokviumski.Z12023;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class DateUtil {
    public static long durationBetween(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toMinutes();
    }
}

class Vehicle {
    String registration;
    String spot;
    LocalDateTime timestampEntered;
    LocalDateTime timestampLeft;
    boolean entry;

    public Vehicle(String registration, String spot, LocalDateTime timestamp, boolean entry) {
        this.registration = registration;
        this.spot = spot;
        this.entry = entry;
        //if (entry) {
        this.timestampEntered = timestamp;
        //} else {
        // this.timestampLeft = timestamp;
        //}
    }

    public void updateVehicle(LocalDateTime timestamp) {
        this.timestampLeft = timestamp;
    }

    public int timeParked() {
        return (int) DateUtil.durationBetween(timestampEntered, timestampLeft);
    }

    public String getRegistration() {
        return registration;
    }

    public String getSpot() {
        return spot;
    }

    public LocalDateTime getTimestampEntered() {
        return timestampEntered;
    }

    public LocalDateTime getTimestampLeft() {
        return timestampLeft;
    }

    public boolean isEntry() {
        return entry;
    }

    @Override
    public String toString() {
        if (timestampLeft == null) {
            return String.format("Registration number: %s Spot: %s Start timestamp: %s",
                    registration, spot, timestampEntered.toString());
        }
        return String.format("Registration number: %s Spot: %s Start timestamp: %s End timestamp: %s Duration in minutes: %d",
                registration, spot, timestampEntered.toString(), timestampLeft.toString(), timeParked());
    }
}

class Parking {
    int capacity;
    List<Vehicle> vehiclesInParking;
    Set<Vehicle> historyOfParkedVehicles;

    public Parking(int capacity) {
        this.capacity = capacity;
        vehiclesInParking = new ArrayList<>();
        historyOfParkedVehicles = new HashSet<>();
    }

    public void update(String registration, String spot, LocalDateTime timestamp, boolean entry) {
        if (entry) {
            vehiclesInParking.add(new Vehicle(registration, spot, timestamp, entry));
        } else {
            for (Vehicle vehicle : vehiclesInParking) {
                if (vehicle.getRegistration().equals(registration)) {
                    historyOfParkedVehicles.add(vehicle);
                    vehicle.updateVehicle(timestamp);
                    vehiclesInParking.remove(vehicle);
                    return;
                }
            }
        }
    }

    public void currentState() {
        System.out.printf("Capacity filled: %.2f%%%n", (vehiclesInParking.size() / (double) capacity) * 100);
        vehiclesInParking.stream()
                .sorted(Comparator.comparing(Vehicle::getTimestampEntered).reversed().thenComparing(Vehicle::getRegistration))
                .forEach(System.out::println);
    }

    public void history() {
        historyOfParkedVehicles.stream()
                .sorted(Comparator.comparing(Vehicle::timeParked).reversed())
                .forEach(System.out::println);
    }

    public Map<String, Integer> carStatistics() {
        return Stream.concat(historyOfParkedVehicles.stream(), vehiclesInParking.stream())
                .collect(Collectors.groupingBy(
                        Vehicle::getRegistration,
                        TreeMap::new,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)));

//        Map<String, Integer> toReturn = new TreeMap<>();
//
//        historyOfParkedVehicles
//                .forEach(v -> {
//                    toReturn.putIfAbsent(v.getRegistration(), 0);
//                    toReturn.computeIfPresent(v.getRegistration(), (k, val) -> val + 1);
//                });
//
//        vehiclesInParking
//                .forEach(v -> {
//                    toReturn.putIfAbsent(v.getRegistration(), 0);
//                    toReturn.computeIfPresent(v.getRegistration(), (k, val) -> val + 1);
//                });
//
//        return toReturn;
    }

    public Map<String, Double> spotOccupancy(LocalDateTime start, LocalDateTime end) {
//        return historyOfParkedVehicles.stream()
//                .filter(v -> v.getTimestampEntered().isAfter(start) && v.getTimestampLeft().isBefore(end))
//                .collect(Collectors.groupingBy(
//                        Vehicle::getSpot,
//                        Collectors.collectingAndThen()
//                ));

        long totalMinutes = Duration.between(start, end).toMinutes();

        Map<String, Long> spotOccupancyTime = new HashMap<>();

        for (Vehicle vehicle : historyOfParkedVehicles) {
            if (vehicle.getTimestampEntered().isBefore(end) && vehicle.getTimestampLeft().isAfter(start)) {
                LocalDateTime occupancyStart = vehicle.getTimestampEntered().isBefore(start) ? start : vehicle.getTimestampEntered();
                LocalDateTime occupancyEnd = vehicle.getTimestampLeft().isAfter(end) ? end : vehicle.getTimestampLeft();
                long minutesOccupied = Duration.between(occupancyStart, occupancyEnd).toMinutes();
                spotOccupancyTime.merge(vehicle.getSpot(), minutesOccupied, Long::sum);
            }
        }

//        for (Vehicle vehicle : vehiclesInParking) {
//            if (vehicle.getTimestampEntered().isBefore(end)) {
//                LocalDateTime occupancyStart = vehicle.getTimestampEntered().isBefore(start) ? start : vehicle.getTimestampEntered();
//                LocalDateTime occupancyEnd = end;
//                long minutesOccupied = Duration.between(occupancyStart, occupancyEnd).toMinutes();
//                spotOccupancyTime.merge(vehicle.getSpot(), minutesOccupied, Long::sum);
//            }
//        }

        return spotOccupancyTime.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> ((double) entry.getValue() / (double) totalMinutes) * 100.0,
                        (e1, e2) -> e1,
                        TreeMap::new));
    }

//        Map<String, Double> toReturn = new HashMap<>();
//        return toReturn;
}


public class ParkingTesting {

    public static <K, V extends Comparable<V>> void printMapSortedByValue(Map<K, V> map) {
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> System.out.println(String.format("%s -> %s", entry.getKey().toString(), entry.getValue().toString())));

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int capacity = Integer.parseInt(sc.nextLine());

        Parking parking = new Parking(capacity);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equals("update")) {
                String registration = parts[1];
                String spot = parts[2];
                LocalDateTime timestamp = LocalDateTime.parse(parts[3]);
                boolean entrance = Boolean.parseBoolean(parts[4]);
                parking.update(registration, spot, timestamp, entrance);
            } else if (parts[0].equals("currentState")) {
                System.out.println("PARKING CURRENT STATE");
                parking.currentState();
            } else if (parts[0].equals("history")) {
                System.out.println("PARKING HISTORY");
                parking.history();
            } else if (parts[0].equals("carStatistics")) {
                System.out.println("CAR STATISTICS");
                printMapSortedByValue(parking.carStatistics());
            } else if (parts[0].equals("spotOccupancy")) {
                LocalDateTime start = LocalDateTime.parse(parts[1]);
                LocalDateTime end = LocalDateTime.parse(parts[2]);
                printMapSortedByValue(parking.spotOccupancy(start, end));
            }
        }
    }
}
