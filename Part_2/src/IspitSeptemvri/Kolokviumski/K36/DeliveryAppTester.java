package IspitSeptemvri.Kolokviumski.K36;

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

class User {

    String id;
    String name;
    Map<String, Location> addresses;
    int totalOrders;
    double totalAmountSpent;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.addresses = new TreeMap<>();
        this.totalOrders = 0;
        this.totalAmountSpent = 0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<String, Location> getAddresses() {
        return addresses;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public double getTotalAmountSpent() {
        return totalAmountSpent;
    }

    public void addAddress(String addressName, Location location) {
        addresses.put(addressName, location);
    }

    public double avgAmountSpent() {
        return totalOrders == 0 ? 0 : totalAmountSpent / totalOrders;
    }

    public void processOrder(float cost) {
        totalOrders++;
        totalAmountSpent += cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(addresses, user.addresses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, addresses);
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount spent: %.2f Average amount spent: %.2f",
                id, name, totalOrders, totalAmountSpent, avgAmountSpent());
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

//class DeliveryPerson implements Comparable<DeliveryPerson> {
//
//    String id;
//    String name;
//    Location currentLocation;
//    int totalDeliveries;
//    double totalDeliveryFee;
//
//    public DeliveryPerson(String id, String name, Location currentLocation) {
//        this.id = id;
//        this.name = name;
//        this.currentLocation = currentLocation;
//        this.totalDeliveries = 0;
//        this.totalDeliveryFee = 0;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public Location getCurrentLocation() {
//        return currentLocation;
//    }
//
//    public void setCurrentLocation(Location currentLocation) {
//        this.currentLocation = currentLocation;
//    }
//
//    public int getTotalDeliveries() {
//        return totalDeliveries;
//    }
//
//    public double getTotalDeliveryFee() {
//        return totalDeliveryFee;
//    }
//
//    public double avgDeliveryFee() {
//        return totalDeliveries == 0 ? 0 : totalDeliveryFee / totalDeliveries;
//    }
//
//    public int compareDistanceToRestaurant(DeliveryPerson other, Location restaurant) {
//        int currentDistance = currentLocation.distance(restaurant);
//        int otherDistance = other.currentLocation.distance(restaurant);
//        if (currentDistance == otherDistance) {
//            return Double.compare(this.totalDeliveryFee, other.totalDeliveryFee);
//        }
//        return currentDistance - otherDistance;
//    }
//
//    public void processOrder(Location location, int distance) {
//        this.currentLocation = location;
//        totalDeliveries++;
//        totalDeliveryFee += ((distance / 10) * 10) + 90;
//    }
//
////    @Override
////    public boolean equals(Object o) {
////        if (this == o) return true;
////        if (o == null || getClass() != o.getClass()) return false;
////        DeliveryPerson that = (DeliveryPerson) o;
////        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(currentLocation, that.currentLocation);
////    }
////
////    @Override
////    public int hashCode() {
////        return Objects.hash(id, name, currentLocation);
////    }
//
//    @Override
//    public String toString() {
//        return String.format("ID: %s Name: %s Total deliveries: %d Total delivery fee: %.2f Average delivery fee: %.2f",
//                id, name, totalDeliveries, totalDeliveryFee, avgDeliveryFee());
//    }
//
//    @Override
//    public int compareTo(DeliveryPerson o) {
//        Comparator<DeliveryPerson> comparator = Comparator.comparing(DeliveryPerson::getTotalDeliveryFee)
//                .thenComparing(DeliveryPerson::getId)
//                .reversed();
//        return comparator.compare(this, o);
//    }
//}

class Restaurant {

    String id;
    String name;
    Location location;
    int totalOrders;
    double totalAmountEarned;

    public Restaurant(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
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

    public int getTotalOrders() {
        return totalOrders;
    }

    public double getTotalAmountEarned() {
        return totalAmountEarned;
    }

    public double avgAmountEarned() {
        return totalOrders == 0 ? 0 : totalAmountEarned / totalOrders;
    }

    public void processOrder(float cost) {
        totalOrders++;
        totalAmountEarned += cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location);
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount earned: %.2f Average amount earned: %.2f",
                id, name, totalOrders, totalAmountEarned, avgAmountEarned());
    }

}

class DeliveryApp {

    String name;
    Map<String, User> users;
    Map<String, DeliveryPerson> deliveryPeople;
    Map<String, Restaurant> restaurants;

    public DeliveryApp(String name) {
        this.name = name;
        this.users = new HashMap<>();
        this.deliveryPeople = new HashMap<>();
        this.restaurants = new HashMap<>();
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
        users.get(id).addAddress(addressName, location);
    }

    public void orderFood(String userId, String userAddressName, String restaurantId, float cost) {
        User user = users.get(userId);
        Restaurant restaurant = restaurants.get(restaurantId);
        Location addressUser = user.getAddresses().get(userAddressName);

        DeliveryPerson deliveryPerson = deliveryPeople.values().stream()
                .min((l, r) -> l.compareDistanceToRestaurant(r, restaurant.getLocation()))
                .get();

        int distance = deliveryPerson.getCurrentLocation().distance(restaurant.getLocation());
        deliveryPerson.processOrder(addressUser, distance);

        user.processOrder(cost);
        restaurant.processOrder(cost);
    }

    public void printUsers() {
        users.values().stream()
                .sorted(Comparator.comparing(User::getTotalAmountSpent).thenComparing(User::getId).reversed())
                .forEach(System.out::println);
    }

    public void printRestaurants() {
        restaurants.values().stream()
                .sorted(Comparator.comparing(Restaurant::avgAmountEarned).thenComparing(Restaurant::getId).reversed())
                .forEach(System.out::println);
    }

    public void printDeliveryPeople() {
        deliveryPeople.values().stream()
                .sorted()
                .forEach(System.out::println);
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