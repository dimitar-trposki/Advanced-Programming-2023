package Juni.VtorKolokvium.Kolokviumski.Z18;

import java.util.*;
import java.util.stream.Collectors;

/**
 * January 2016 Exam problem 2
 */

interface ICluster<T> {
    long getId();

    double distance(T point);
}

class Point2D implements ICluster<Point2D>, Comparable<Point2D> {
    private final long id;
    private final float x;
    private final float y;
    private double distanceToReference;

    public Point2D(long id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setDistanceToReference(double distance) {
        this.distanceToReference = distance;
    }

    @Override
    public double distance(Point2D p) {
        return Math.sqrt(Math.pow(this.x - p.x, 2) + Math.pow(this.y - p.y, 2));
    }

    @Override
    public int compareTo(Point2D o) {
        return Double.compare(this.distanceToReference, o.distanceToReference);
    }
}

class Cluster<T extends ICluster<T>> {
    private final List<T> items;

    public Cluster() {
        items = new ArrayList<>();
    }

    public void addItem(T element) {
        items.add(element);
    }

    public void near(long id, int top) {
        T result = items.stream()
                .filter(l -> l.getId() == id)
                .findFirst().get();

        List<T> sorted = items.stream()
                .filter(l -> l.getId() != id)
                .peek(l -> ((Point2D) l).setDistanceToReference(l.distance(result)))
                .sorted()
                .limit(top)
                .collect(Collectors.toList());

        for (int i = 0; i < sorted.size(); i++) {
            System.out.printf("%d. %d -> %.3f%n", i + 1, sorted.get(i).getId(), sorted.get(i).distance(result));
        }
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
