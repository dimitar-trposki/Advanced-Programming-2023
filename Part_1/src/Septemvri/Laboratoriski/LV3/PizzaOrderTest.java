package Septemvri.Laboratoriski.LV3;

import java.util.*;
import java.util.stream.Collectors;

interface Item {

    public int getPrice();

    public String getType();
}

class InvalidExtraTypeException extends Exception {

    public InvalidExtraTypeException() {
        super("InvalidExtraTypeException");
    }
}

class InvalidPizzaTypeException extends Exception {

    public InvalidPizzaTypeException() {
        super("InvalidPizzaTypeException");
    }
}

class ItemOutOfStockException extends Exception {

    public ItemOutOfStockException(Item item) {
        super("ItemOutOfStockException");
    }
}

class ArrayIndexOutOfBoundsException extends Exception {

    public ArrayIndexOutOfBoundsException(int idx) {
        super("ArrayIndexOutOfBoundsException");
    }
}

class EmptyOrder extends Exception {

    public EmptyOrder() {
        super("EmptyOrder");
    }
}

class OrderLockedException extends Exception {

    public OrderLockedException() {
        super("OrderLockedException");
    }
}

class ExtraItem implements Item {

    private String type;

    public ExtraItem(String type) throws InvalidExtraTypeException {
        if (type.equals("Coke") || type.equals("Ketchup")) {
            this.type = type;
        } else {
            throw new InvalidExtraTypeException();
        }
    }

    public String getType() {
        return type;
    }

    @Override
    public int getPrice() {
        if (type.equals("Coke")) {
            return 5;
        } else {
            return 3;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtraItem extraItem = (ExtraItem) o;
        return Objects.equals(type, extraItem.type);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type);
    }
}

class PizzaItem implements Item {

    private String type;

    public PizzaItem(String type) throws InvalidPizzaTypeException {
        if (type.equals("Standard") || type.equals("Pepperoni") || type.equals("Vegetarian")) {
            this.type = type;
        } else {
            throw new InvalidPizzaTypeException();
        }
    }

    public String getType() {
        return type;
    }

    @Override
    public int getPrice() {
        if (type.equals("Standard")) {
            return 10;
        } else if (type.equals("Pepperoni")) {
            return 12;
        } else {
            return 8;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PizzaItem pizzaItem = (PizzaItem) o;
        return Objects.equals(type, pizzaItem.type);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type);
    }
}

class Order {

    private Map<Item, Integer> items;
    private boolean locked;

    public Order() {
        items = new LinkedHashMap<>();
        locked = false;
    }

    public void addItem(Item item, int count) throws ItemOutOfStockException, OrderLockedException {
        if (locked) {
            throw new OrderLockedException();
        }
        if (count > 10) {
            throw new ItemOutOfStockException(item);
        }

        items.putIfAbsent(item, count);
        items.computeIfPresent(item, (k, v) -> count);
    }

    public int getPrice() {
        int sum = 0;

        List<Item> item = items.keySet().stream().collect(Collectors.toList());
        List<Integer> quantity = items.values().stream().collect(Collectors.toList());

        for (int i = 0; i < item.size(); i++) {
            sum += item.get(i).getPrice() * quantity.get(i);
        }

        return sum;
    }

    public void displayOrder() {
        List<Item> item = items.keySet().stream().collect(Collectors.toList());
        List<Integer> quantity = items.values().stream().collect(Collectors.toList());

        for (int i = 0; i < item.size(); i++) {
            System.out.printf("%3d.%-15sx%2d%5d$%n", i + 1, item.get(i).getType().toString(),
                    quantity.get(i), item.get(i).getPrice() * quantity.get(i));
        }

        System.out.printf("%-22s%5d$\n", "Total:", getPrice());
    }

    public void removeItem(int idx) throws ArrayIndexOutOfBoundsException, OrderLockedException {
        if (locked) {
            throw new OrderLockedException();
        }
        if (idx > items.keySet().size()) {
            throw new ArrayIndexOutOfBoundsException(idx);
        }

        List<Item> keys = items.keySet().stream().collect(Collectors.toList());
        Item item = null;

        for (int i = 0; i < keys.size(); i++) {
            if (i == idx) {
                item = keys.get(i);
            }
        }

        items.remove(item);
    }

    public void lock() throws EmptyOrder {
        if (items.isEmpty()) {
            throw new EmptyOrder();
        }

        locked = true;
    }

}

public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}