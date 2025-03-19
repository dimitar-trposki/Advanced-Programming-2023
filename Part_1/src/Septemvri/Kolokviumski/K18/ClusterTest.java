package Septemvri.Kolokviumski.K18;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

interface ICluster<T> {

    long getId();

    double distance(T other);

}

class Cluster<T extends ICluster<T>> {

    List<T> elements;

    public Cluster() {
        elements = new ArrayList<>();
    }

    public void addItem(T element) {
        elements.add(element);
    }

    public void near(long id, int top) {
        T element = elements.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .get();

        Point2D.otherElement = (Point2D) element;

        AtomicInteger i = new AtomicInteger(1);
        elements.stream()
                .sorted(Comparator.comparingDouble(element::distance))
                .skip(1)
                .limit(top)
                .forEach(e -> System.out.printf("%d. %s%n", i.getAndIncrement(), e.toString()));
    }

}

class Point2D implements ICluster<Point2D> {

    long id;
    float x;
    float y;
    static Point2D otherElement = null;

    public Point2D(long id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public double distance(Point2D other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    @Override
    public String toString() {
        return String.format("%s -> %.3f", id, distance(otherElement));
    }

}

public class ClusterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cluster<Point2D> cluster = new Cluster<>();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            long id = Long.parseLong(parts[0]);
            float x = Float.parseFloat(parts[1]);
            float y = Float.parseFloat(parts[2]);
            cluster.addItem(new Point2D(id, x, y));
        }
        int id = scanner.nextInt();
        int top = scanner.nextInt();
        cluster.near(id, top);
        scanner.close();
    }
}