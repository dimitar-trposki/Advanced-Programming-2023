package Juni.VtorKolokvium.Kolokviumski.Z36;

import java.util.*;
import java.util.stream.Collectors;

/*
YOUR CODE HERE
DO NOT MODIFY THE interfaces and classes below!!!
*/

interface Location {
    int getX();

    int getY();

    default int distance(Location other) {
        int xDiff = Math.abs(getX() - other.getX());
        int yDiff = Math.abs(getY() - other.getY());
        return xDiff + yDiff;
    }
}

class LocationCreator {
    public static Location create(int x, int y) {

        return new Location() {
            @Override
            public int getX() {
                return x;
            }

            @Override
            public int getY() {
                return y;
            }
        };
    }
}

class Address {
    String name;
    Location location;

    public Address(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }
}

class User implements Comparable<User> {
    String id;
    String name;
    Map<String, Address> addresses;
    List<Double> moneySpent;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        addresses = new HashMap<>();
        moneySpent = new ArrayList<>();
    }

    public void addAddress(Address address) {
        addresses.put(address.name, address);
    }

    public double totalAmountSpent() {
        return moneySpent.stream()
                .mapToDouble(i -> i)
                .sum();
    }

    public double averageAmountSpent() {
        if (moneySpent.isEmpty()) {
            return 0;
        }
        return totalAmountSpent() / moneySpent.size();
    }

    public void processOrder(float cost) {
        moneySpent.add((double) cost);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Double> getMoneySpent() {
        return moneySpent;
    }

    public Map<String, Address> getAddresses() {
        return addresses;
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount spent: %.2f Average amount spent: %.2f",
                id, name, moneySpent.size(), totalAmountSpent(), averageAmountSpent());
    }

    @Override
    public int compareTo(User o) {
        Comparator<User> comparator = Comparator.comparing(User::totalAmountSpent)
                .thenComparing(User::getId).reversed();
        return comparator.compare(this, o);
    }
}

class DeliveryPerson implements Comparable<DeliveryPerson> {
    String id;
    String name;
    Location currentLocation;
    List<Double> moneyEarned;

    public DeliveryPerson(String id, String name, Location currentLocation) {
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;
        moneyEarned = new ArrayList<>();
    }

    public double totalDeliveryFee() {
        return moneyEarned.stream()
                .mapToDouble(i -> i)
                .sum();
    }

    public double averageDeliveryFee() {
        if (moneyEarned.isEmpty()) {
            return 0;
        }
        return totalDeliveryFee() / moneyEarned.size();
    }

    public void processOrder(Location location, int distance) {
        this.currentLocation = location;
        moneyEarned.add((double) (90 + (distance / 10) * 10));
    }

    public int compareDistanceToRestaurant(DeliveryPerson other, Location restaurant) {
        int currentDistance = currentLocation.distance(restaurant);
        int otherDistance = other.currentLocation.distance(restaurant);
        if (currentDistance == otherDistance) {
            return Integer.compare(this.moneyEarned.size(), other.moneyEarned.size());
        }
        return currentDistance - otherDistance;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public List<Double> getMoneyEarned() {
        return moneyEarned;
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total deliveries: %d Total delivery fee: %.2f Average delivery fee: %.2f",
                id, name, moneyEarned.size(), totalDeliveryFee(), averageDeliveryFee());
    }

    @Override
    public int compareTo(DeliveryPerson o) {
        Comparator<DeliveryPerson> comparator = Comparator.comparing(DeliveryPerson::totalDeliveryFee)
                .thenComparing(DeliveryPerson::getId)
                .reversed();
        return comparator.compare(this, o);
    }
}

class Restaurant implements Comparable<Restaurant> {
    String id;
    String name;
    Location location;
    List<Double> moneyEarned;

    public Restaurant(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        moneyEarned = new ArrayList<>();
    }

    public double totalAmountEarned() {
        return moneyEarned.stream()
                .mapToDouble(i -> i)
                .sum();
    }

    public double averageAmountEarned() {
        if (moneyEarned.isEmpty()) {
            return 0;
        }
        return totalAmountEarned() / moneyEarned.size();
    }

    public void processOrder(float cost) {
        moneyEarned.add((double) cost);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public List<Double> getMoneyEarned() {
        return moneyEarned;
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount earned: %.2f Average amount earned: %.2f",
                id, name, moneyEarned.size(), totalAmountEarned(), averageAmountEarned());
    }

    @Override
    public int compareTo(Restaurant o) {
        Comparator<Restaurant> comparator = Comparator.comparing(Restaurant::averageAmountEarned)
                .thenComparing(Restaurant::getId).reversed();
        return comparator.compare(this, o);
    }
}

class DeliveryApp {
    String name;
    Map<String, User> users;
    Map<String, DeliveryPerson> deliveryPeople;
    Map<String, Restaurant> restaurants;

    public DeliveryApp(String name) {
        this.name = name;
        users = new HashMap<>();
        deliveryPeople = new HashMap<>();
        restaurants = new HashMap<>();
    }

    public void registerDeliveryPerson(String id, String name, Location currentLocation) {
        deliveryPeople.put(id, new DeliveryPerson(id, name, currentLocation));
    }

    public void addRestaurant(String id, String name, Location location) {
        restaurants.put(id, new Restaurant(id, name, location));
    }

    public void addUser(String id, String name) {
        users.put(id, new User(id, name));
    }

    public void addAddress(String id, String addressName, Location location) {
        users.get(id).addAddress(new Address(addressName, location));
    }

    public void orderFood(String userId, String userAddressName, String restaurantId, float cost) {
        User user = users.get(userId);
        Restaurant restaurant = restaurants.get(restaurantId);
        Address address = user.getAddresses().get(userAddressName);

        user.processOrder(cost);
        restaurant.processOrder(cost);

        DeliveryPerson deliveryPerson = deliveryPeople.values().stream()
                .min((l, r) -> l.compareDistanceToRestaurant(r, restaurant.getLocation()))
                .get();

        int distance = deliveryPerson.getCurrentLocation().distance(restaurant.getLocation());
        deliveryPerson.processOrder(address.location, distance);
    }

    public void printUsers() {
        users.values().stream().sorted().forEach(System.out::println);
    }

    public void printRestaurants() {
        restaurants.values().stream().sorted().forEach(System.out::println);
    }

    public void printDeliveryPeople() {
        deliveryPeople.values().stream().sorted().forEach(System.out::println);
    }
}

public class DeliveryAppTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String appName = sc.nextLine();
        DeliveryApp app = new DeliveryApp(appName);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");

            if (parts[0].equals("addUser")) {
                String id = parts[1];
                String name = parts[2];
                app.addUser(id, name);
            } else if (parts[0].equals("registerDeliveryPerson")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.registerDeliveryPerson(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addRestaurant")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addRestaurant(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addAddress")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addAddress(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("orderFood")) {
                String userId = parts[1];
                String userAddressName = parts[2];
                String restaurantId = parts[3];
                float cost = Float.parseFloat(parts[4]);
                app.orderFood(userId, userAddressName, restaurantId, cost);
            } else if (parts[0].equals("printUsers")) {
                app.printUsers();
            } else if (parts[0].equals("printRestaurants")) {
                app.printRestaurants();
            } else {
                app.printDeliveryPeople();
            }

        }
    }
}
