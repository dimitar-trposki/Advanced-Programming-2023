package IspitSeptemvri.Kolokviumski.K8;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class InvalidIDException extends Exception {

    public InvalidIDException(String id) {
        super(String.format("ID %s is not valid", id));
    }

}

class InvalidDimensionException extends Exception {

    public InvalidDimensionException() {
        super("Dimension 0 is not allowed!");
    }

}

abstract class Shape {

    String id;
    double a;

    public Shape(String id, double a) throws InvalidIDException {
        if (id.length() != 6 || !id.matches("^[a-zA-Z0-9]+$")) {
            throw new InvalidIDException(id);
        }
        this.id = id;
        this.a = a;
    }

    abstract public double area();

    abstract public double perimeter();

    abstract void scale(double coef);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shape shape = (Shape) o;
        return Double.compare(a, shape.a) == 0 && Objects.equals(id, shape.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, a);
    }
}

class Circle extends Shape {

    public Circle(String id, double a) throws InvalidIDException {
        super(id, a);
    }

    public double area() {
        return Math.PI * Math.pow(a, 2);
    }

    public double perimeter() {
        return Math.PI * 2 * a;
    }

    @Override
    void scale(double coef) {
        a *= coef;
    }

    @Override
    public String toString() {
        return String.format("Circle -> Radius: %.2f Area: %.2f Perimeter: %.2f",
                a, area(), perimeter());
    }

}

class Square extends Shape {

    public Square(String id, double a) throws InvalidIDException {
        super(id, a);
    }

    @Override
    public double area() {
        return Math.pow(a, 2);
    }

    @Override
    public double perimeter() {
        return 4 * a;
    }

    @Override
    void scale(double coef) {
        a *= coef;
    }

    @Override
    public String toString() {
        return String.format("Square: -> Side: %.2f Area: %.2f Perimeter: %.2f",
                a, area(), perimeter());
    }

}

class Rectangle extends Shape {

    double b;

    public Rectangle(String id, double a, double b) throws InvalidIDException {
        super(id, a);
        this.b = b;
    }

    @Override
    public double area() {
        return a * b;
    }

    @Override
    public double perimeter() {
        return ((2 * a) + (2 * b));
    }

    @Override
    void scale(double coef) {
        a *= coef;
        b *= coef;
    }

    @Override
    public String toString() {
        return String.format("Rectangle: -> Sides: %.2f, %.2f Area: %.2f Perimeter: %.2f",
                a, b, area(), perimeter());
    }

}

class Canvas {

    Map<String, Set<Shape>> shapesByUserId;
    DoubleSummaryStatistics dss = new DoubleSummaryStatistics();

    public Canvas() {
        this.shapesByUserId = new HashMap<>();
    }

    public void readShapes(InputStream is) {
        Scanner scanner = new Scanner(is);
        try {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split("\\s+");
                if (Double.parseDouble(parts[2]) == 0 || (parts.length == 4 && Double.parseDouble(parts[3]) == 0)) {
                    throw new InvalidDimensionException();
                }
                shapesByUserId.putIfAbsent(parts[1], new TreeSet<>(Comparator.comparing(Shape::perimeter)));
                if (parts[0].equals("1")) {
                    try {
                        shapesByUserId.get(parts[1]).add(new Circle(parts[1], Double.parseDouble(parts[2])));
                    } catch (InvalidIDException e) {
                        System.out.println(e.getMessage());
                    }
                } else if (parts[0].equals("2")) {
                    try {
                        shapesByUserId.get(parts[1]).add(new Square(parts[1], Double.parseDouble(parts[2])));
                    } catch (InvalidIDException e) {
                        System.out.println(e.getMessage());
                    }
                } else if (parts[0].equals("3")) {
                    try {
                        shapesByUserId.get(parts[1]).add(new Rectangle(parts[1], Double.parseDouble(parts[2]), Double.parseDouble(parts[3])));
                    } catch (InvalidIDException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        } catch (InvalidDimensionException e) {
            System.out.println(e.getMessage());
        }
    }

    public void scaleShapes(String userID, double coef) {
        if (shapesByUserId.get(userID) != null) {
            shapesByUserId.get(userID).stream()
                    .forEach(s -> s.scale(coef));
        }
    }

    public void printAllShapes(OutputStream os) {
        List<Shape> allShapes = new ArrayList<>();
        allShapes = shapesByUserId.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        this.dss = allShapes.stream().mapToDouble(Shape::area).summaryStatistics();
        System.out.println(allShapes.stream()
                .sorted(Comparator.comparing(Shape::area))
                .map(Object::toString)
                .collect(Collectors.joining("\n")));
    }

    public void printByUserId(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        shapesByUserId = shapesByUserId.entrySet().stream()
                .filter(s -> !s.getValue().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        shapesByUserId.entrySet().stream()
                .sorted(Comparator.comparing((Map.Entry<String, Set<Shape>> entry) -> entry.getValue().size()).reversed()
                        .thenComparing(entry -> entry.getValue().stream().mapToDouble(Shape::area).sum()))
                .forEach(entry -> {
                    pw.println(String.format("Shapes of user: %s", entry.getKey()));
                    pw.println(entry.getValue().stream()
                            .map(Object::toString)
                            .collect(Collectors.joining("\n")));
                });
        pw.flush();
    }

    public void statistics(OutputStream os) {
        System.out.println(String.format("count: %d\nsum: %.2f\nmin: %.2f\naverage: %.2f\nmax: %.2f\n",
                dss.getCount(), dss.getSum(), dss.getMin(), dss.getAverage(), dss.getMax()));
    }

}

public class CanvasTest {

    public static void main(String[] args) {
        Canvas canvas = new Canvas();

        System.out.println("READ SHAPES AND EXCEPTIONS TESTING");
        canvas.readShapes(System.in);

        System.out.println("BEFORE SCALING");
        canvas.printAllShapes(System.out);
        canvas.scaleShapes("123456", 1.5);
        System.out.println("AFTER SCALING");
        canvas.printAllShapes(System.out);

        System.out.println("PRINT BY USER ID TESTING");
        canvas.printByUserId(System.out);

        System.out.println("PRINT STATISTICS");
        canvas.statistics(System.out);
    }
}