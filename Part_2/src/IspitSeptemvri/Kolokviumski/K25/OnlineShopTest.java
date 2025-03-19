package IspitSeptemvri.Kolokviumski.K25;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {

    ProductNotFoundException(String message) {
        super(message);
    }

}


class Product {

    String id;
    String name;
    LocalDateTime createdAt;
    double price;
    int quantitySold = 0;

    public Product(String id, String name, LocalDateTime createdAt, double price) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.price = price;
        this.quantitySold = 0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void increment(int quantity) {
        quantitySold += quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", price=" + price +
                ", quantitySold=" + quantitySold +
                '}';
    }

}


class OnlineShop {

    Map<String, List<Product>> productsByCategory;
    Map<String, Product> productsById;

    OnlineShop() {
        this.productsByCategory = new HashMap<>();
        this.productsById = new HashMap<>();
    }

    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price) {
        productsByCategory.putIfAbsent(category, new ArrayList<>());
        Product product = new Product(id, name, createdAt, price);
        productsByCategory.get(category).add(product);
        productsById.put(id, product);
    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException {
        if (!productsById.containsKey(id)) {
            throw new ProductNotFoundException(String.format("Product with id %s does not exist in the online shop!", id));
        }
        Product product = productsById.get(id);
        product.increment(quantity);
        return quantity * product.price;
    }

    private Comparator<Product> productComparator(COMPARATOR_TYPE type) {
        if (type == COMPARATOR_TYPE.LOWEST_PRICE_FIRST) {
            return Comparator.comparing(Product::getPrice);
        } else if (type == COMPARATOR_TYPE.HIGHEST_PRICE_FIRST) {
            return Comparator.comparing(Product::getPrice).reversed();
        } else if (type == COMPARATOR_TYPE.LEAST_SOLD_FIRST) {
            return Comparator.comparing(Product::getQuantitySold);
        } else if (type == COMPARATOR_TYPE.MOST_SOLD_FIRST) {
            return Comparator.comparing(Product::getQuantitySold).reversed();
        } else if (type == COMPARATOR_TYPE.OLDEST_FIRST) {
            return Comparator.comparing(Product::getCreatedAt);
        } else {
            return Comparator.comparing(Product::getCreatedAt).reversed();
        }
    }

    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        List<List<Product>> result = new ArrayList<>();
        Comparator<Product> comparator = productComparator(comparatorType);
        int numOfLists;
        if (category != null) {
            numOfLists = productsByCategory.get(category).size() % pageSize == 0 ?
                    productsByCategory.get(category).size() / pageSize : productsByCategory.get(category).size() / pageSize + 1;
        } else {
            int num = (int) productsByCategory.values().stream()
                    .flatMap(Collection::stream)
                    .count();
            numOfLists = num % pageSize == 0 ?
                    num / pageSize : num / pageSize + 1;
        }

        for (int i = 0; i < numOfLists; i++) {
            result.add(new ArrayList<>());
        }

        List<Product> products = new ArrayList<>();
        if (category != null) {
            products = productsByCategory.get(category);
        } else {
            products = productsByCategory.values().stream()
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
        }
        products = products.stream().sorted(comparator).collect(Collectors.toList());
        for (int i = 0; i < products.size(); i++) {
            int pageIndex = i / pageSize;
            result.get(pageIndex).add(products.get(i));
        }
        return result;
    }

}

public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category = null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}