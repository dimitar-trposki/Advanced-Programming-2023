package Juni.VtorKolokvium.Kolokviumski.Z3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

class Store {
    String name;
    Map<Integer, Integer> prices;

    public Store(String line) {
        String[] parts = line.split("\\s+");
        this.name = parts[0];
        this.prices = new TreeMap<>(Collections.reverseOrder());
        for (int i = 1; i < parts.length; i++) {
            String[] parts2 = parts[i].split(":");
            prices.put(Integer.parseInt(parts2[0]), Integer.parseInt(parts2[1]));
        }
    }

    public int discount(Integer key) {
        return (int) ((((double) prices.get(key) - key) / prices.get(key)) * 100.0);
    }

    public double averageDiscount() {
        List<Integer> keys = new ArrayList<>(prices.keySet());
        double sum = 0;

        for (Integer key : keys) {
            sum += discount(key);
        }

        return sum / keys.size();
    }

    public int sumDiscount() {
        List<Integer> keys = new ArrayList<>(prices.keySet());
        int sum = 0;

        for (Integer key : keys) {
            sum += prices.get(key) - key;
        }

        return sum;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getPrices() {
        return prices.keySet().stream().collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n")
                .append("Average discount: ")
                .append(String.format("%.1f%%\n", averageDiscount()))
                .append("Total discount: ")
                .append(String.format("%d\n", sumDiscount()));

        List<Integer> keys = new ArrayList<>(prices.keySet());

        keys = keys.stream().sorted(Comparator.comparingInt(this::discount).reversed()).collect(Collectors.toList());

        for (int i = 0; i < keys.size() - 1; i++) {
            sb.append(String.format("%2d%% %d/%d\n", discount(keys.get(i)), keys.get(i), prices.get(keys.get(i))));
        }
        sb.append(String.format("%2d%% %d/%d", discount(keys.get(keys.size() - 1)), keys.get(keys.size() - 1), prices.get(keys.get(keys.size() - 1))));

        return sb.toString();
    }
}

class Discounts {
    Set<Store> stores;

    public Discounts() {
        stores = new HashSet<>();
    }

    public int readStores(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        stores = br.lines().map(Store::new).collect(Collectors.toSet());

        return stores.size();
    }

    public List<Store> byAverageDiscount() {
        Comparator<Store> comparator = Comparator.comparing(Store::averageDiscount).thenComparing(Store::getName);
        return stores.stream().sorted(comparator.reversed()).limit(3).collect(Collectors.toList());
    }

    public List<Store> byTotalDiscount() {
        Comparator<Store> comparator = Comparator.comparing(Store::sumDiscount);
        return stores.stream().sorted(comparator).limit(3).collect(Collectors.toList());
    }
}

public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}
