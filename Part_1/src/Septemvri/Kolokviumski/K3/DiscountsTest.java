package Septemvri.Kolokviumski.K3;

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
        this.prices = new TreeMap<>(Comparator.reverseOrder());
        for (int i = 1; i < parts.length; i++) {
            String[] p = parts[i].split(":");
            this.prices.put(Integer.parseInt(p[0]), Integer.parseInt(p[1]));
        }
    }

    public String getName() {
        return name;
    }

    public int discount(Integer key) {
        return (int) ((((double) prices.get(key) - key) / prices.get(key)) * 100.0);
    }

    public double averageDiscount() {
        int total = 0;
        for (Map.Entry<Integer, Integer> entry : prices.entrySet()) {
            total += (discount(entry.getKey()));
        }
        return (double) total / prices.size();
    }

    public int totalDiscount() {
        int total = 0;
        for (Map.Entry<Integer, Integer> entry : prices.entrySet()) {
            total += (entry.getValue() - entry.getKey());
        }
        return total;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name).append("\n");
        stringBuilder.append(String.format("Average discount: %.1f%%\n", averageDiscount()));
        stringBuilder.append("Total discount: ").append(totalDiscount()).append("\n");
        List<Integer> keys = new ArrayList<>(prices.keySet());
        keys = keys.stream().sorted(Comparator.comparingInt(this::discount).reversed()).collect(Collectors.toList());
//        stringBuilder.append(prices.entrySet().stream()
//                .sorted()
//                .map(e -> String.format("%2d%% %d/%d", discountByProduct(e.getValue(), e.getKey()), e.getKey(), e.getValue()))
//                .sorted(Comparator.reverseOrder())
//                .collect(Collectors.joining("\n")));
        stringBuilder.append(keys.stream()
                .map(e -> String.format("%2d%% %d/%d", discount(e), e, prices.get(e)))
                .collect(Collectors.joining("\n")));
        return stringBuilder.toString();
    }

}

class Discounts {

    List<Store> stores;

    public Discounts() {
        stores = new ArrayList<>();
    }

    public int readStores(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        stores = br.lines().map(Store::new).collect(Collectors.toList());
        return stores.size();
    }

    public List<Store> byAverageDiscount() {
        return stores.stream()
                .sorted(Comparator.comparing(Store::averageDiscount).thenComparing(Store::getName).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    public List<Store> byTotalDiscount() {
        return stores.stream()
                .sorted(Comparator.comparing(Store::totalDiscount).reversed().thenComparing(Store::getName).reversed())
                .limit(3)
                .collect(Collectors.toList());
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