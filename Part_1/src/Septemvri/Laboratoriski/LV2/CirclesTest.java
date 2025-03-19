package Septemvri.Laboratoriski.LV2;

import java.util.*;

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

class ObjectCanNotBeMovedException extends Exception {

    public ObjectCanNotBeMovedException(int x, int y) {
        super("Point (" + x + "," + y + ") is out of bounds");
    }

}

class MovableObjectNotFittableException extends Exception {

    public MovableObjectNotFittableException(int x, int y, int radius) {
        super("Movable circle with center (" + x + "," + y + ") and radius "
                + radius + " can not be fitted into the collection");
    }
}

interface Movable {

    public void moveUp() throws ObjectCanNotBeMovedException;

    public void moveDown() throws ObjectCanNotBeMovedException;

    public void moveRight() throws ObjectCanNotBeMovedException;

    public void moveLeft() throws ObjectCanNotBeMovedException;

    public int getCurrentXPosition();

    public int getCurrentYPosition();

    public boolean canFit();

    public TYPE getType();

}

class MovablePoint implements Movable {

    private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;

    public MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public boolean canFit() {
        if (x > MovablesCollection.maxXCoordinate)
            return false;
        else if (y > MovablesCollection.maxYCoordinate)
            return false;
        else if (x < MovablesCollection.minXCoordinate)
            return false;
        else if (y < MovablesCollection.minYCoordinate)
            return false;
        return true;
    }

    @Override
    public TYPE getType() {
        return TYPE.POINT;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        if (y + ySpeed > MovablesCollection.maxYCoordinate)
            throw new ObjectCanNotBeMovedException(x, y + ySpeed);

        y += ySpeed;
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        if (y - ySpeed < MovablesCollection.minYCoordinate)
            throw new ObjectCanNotBeMovedException(x, y - ySpeed);

        y -= ySpeed;
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        if (x + xSpeed > MovablesCollection.maxXCoordinate)
            throw new ObjectCanNotBeMovedException(x + xSpeed, y);

        x += xSpeed;
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        if (x - xSpeed < MovablesCollection.minXCoordinate)
            throw new ObjectCanNotBeMovedException(x - xSpeed, y);

        x -= xSpeed;
    }

    public int getxSpeed() {
        return xSpeed;
    }

    public int getySpeed() {
        return ySpeed;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }

    @Override
    public String toString() {
        return "Movable point with coordinates (" + x + "," + y + ")\n";
    }

}

class MovableCircle implements Movable {

    private int radius;
    private MovablePoint center;

    public MovableCircle(int radius, MovablePoint center) {
        this.radius = radius;
        this.center = center;
    }

    public boolean canFit() {
        if (center.getCurrentXPosition() + radius > MovablesCollection.maxXCoordinate)
            return false;
        else if (center.getCurrentYPosition() + radius > MovablesCollection.maxYCoordinate)
            return false;
        else if (center.getCurrentXPosition() - radius < MovablesCollection.minXCoordinate)
            return false;
        else if (center.getCurrentYPosition() - radius < MovablesCollection.minYCoordinate)
            return false;
        return true;
    }

    @Override
    public TYPE getType() {
        return TYPE.CIRCLE;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        if (center.getCurrentYPosition() + center.getySpeed() > MovablesCollection.maxYCoordinate)
            throw new ObjectCanNotBeMovedException(center.getCurrentXPosition(),
                    center.getCurrentYPosition() + center.getySpeed());

        center.setY(center.getCurrentYPosition() + center.getySpeed());
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        if (center.getCurrentYPosition() - center.getySpeed() < MovablesCollection.minYCoordinate)
            throw new ObjectCanNotBeMovedException(center.getCurrentXPosition(),
                    center.getCurrentYPosition() - center.getySpeed());

        center.setY(center.getCurrentYPosition() - center.getySpeed());
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        if (center.getCurrentXPosition() + center.getxSpeed() > MovablesCollection.maxXCoordinate)
            throw new ObjectCanNotBeMovedException(center.getCurrentXPosition() + center.getxSpeed(),
                    center.getCurrentYPosition());

        center.setX(center.getCurrentXPosition() + center.getxSpeed());
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        if (center.getCurrentXPosition() - center.getxSpeed() < MovablesCollection.minXCoordinate)
            throw new ObjectCanNotBeMovedException(center.getCurrentXPosition() - center.getxSpeed(),
                    center.getCurrentYPosition());

        center.setX(center.getCurrentXPosition() + center.getxSpeed());
    }

    @Override
    public int getCurrentXPosition() {
        return center.getCurrentXPosition();
    }

    @Override
    public int getCurrentYPosition() {
        return center.getCurrentYPosition();
    }

    @Override
    public String toString() {
        return "Movable circle with center coordinates ("
                + center.getCurrentXPosition() + "," + center.getCurrentYPosition()
                + ") and radius " + radius + "\n";
    }

}

class MovablesCollection {

    //private Movable[] movable;
    private List<Movable> movable;
    static int maxXCoordinate = 0;
    static int maxYCoordinate = 0;
    static final int minXCoordinate = 0;
    static final int minYCoordinate = 0;

    public MovablesCollection(int x_MAX, int y_MAX) {
        maxXCoordinate = x_MAX;
        maxYCoordinate = y_MAX;

        this.movable = new ArrayList<>();
    }

    public static void setxMax(int maxXCoordinate) {
        MovablesCollection.maxXCoordinate = maxXCoordinate;
    }

    public static void setyMax(int maxYCoordinate) {
        MovablesCollection.maxYCoordinate = maxYCoordinate;
    }

    public void addMovableObject(Movable m) throws MovableObjectNotFittableException {
        if (!m.canFit()) {
            MovableCircle movableCircle = (MovableCircle) m;
            throw new MovableObjectNotFittableException(m.getCurrentXPosition(), m.getCurrentYPosition(), movableCircle.getRadius());
        }
        movable.add(m);
    }

    public void moveObjectsFromTypeWithDirection(TYPE type, DIRECTION direction) {
        for (Movable m : movable) {
            if (m.getType().equals(type)) {
                try {
                    if (direction.equals(DIRECTION.UP))
                        m.moveUp();
                    if (direction.equals(DIRECTION.DOWN))
                        m.moveDown();
                    if (direction.equals(DIRECTION.LEFT))
                        m.moveLeft();
                    if (direction.equals(DIRECTION.RIGHT))
                        m.moveRight();
                } catch (ObjectCanNotBeMovedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Collection of movable objects with size ");
        stringBuilder.append(movable.size()).append(":\n");

        for (Movable movables : movable) {
            stringBuilder.append(movables.toString());
        }

        return stringBuilder.toString();
    }
}

public class CirclesTest {

    public static void main(String[] args) {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                try {
                    collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
                } catch (MovableObjectNotFittableException e) {
                    System.out.println(e.getMessage());
                }
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                try {
                    collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
                } catch (MovableObjectNotFittableException e) {
                    System.out.println(e.getMessage());
                }
            }

        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        System.out.println(collection.toString());


    }


}