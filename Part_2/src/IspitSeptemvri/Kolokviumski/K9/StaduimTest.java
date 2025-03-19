package IspitSeptemvri.Kolokviumski.K9;

import java.util.*;
import java.util.stream.IntStream;

class SeatNotAllowedException extends Exception {

    public SeatNotAllowedException() {
    }

}

class SeatTakenException extends Exception {

    public SeatTakenException() {
    }

}

class Sector {

    String code;
    int numberOfSeats;
    Map<Integer, Integer> seatsInfo;
    Set<Integer> types;

    public Sector(String code, int numberOfSeats) {
        this.code = code;
        this.numberOfSeats = numberOfSeats;
        this.seatsInfo = new HashMap<>();
        this.types = new HashSet<>();
    }

    public void buyTicket(int seat, int type) throws SeatNotAllowedException {
        if (type == 1) {
            if (types.contains(2))
                throw new SeatNotAllowedException();
        } else if (type == 2) {
            if (types.contains(1))
                throw new SeatNotAllowedException();
        }
        types.add(type);
        seatsInfo.put(seat, type);
    }

    private double freeSeatsPercent() {
        return ((double) seatsInfo.size() / numberOfSeats) * 100.0;
    }

    public int freeSeats() {
        return numberOfSeats - seatsInfo.size();
    }

    public boolean isTaken(int seat) {
        return seatsInfo.containsKey(seat);
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%", code, freeSeats(), numberOfSeats, freeSeatsPercent());
    }

}

class Stadium {

    String name;
    Map<String, Sector> sectors;

    public Stadium(String name) {
        this.name = name;
        this.sectors = new HashMap<>();
    }

    public void createSectors(String[] sectorNames, int[] sizes) {
        for (int i = 0; i < sectorNames.length; i++) {
            sectors.put(sectorNames[i], new Sector(sectorNames[i], sizes[i]));
        }
    }

    public void buyTicket(String sectorName, int seat, int type) throws SeatNotAllowedException, SeatTakenException {
        Sector sector = sectors.get(sectorName);
        if (sector.isTaken(seat)) {
            throw new SeatTakenException();
        }
        sector.buyTicket(seat, type);
    }

    public void showSectors() {
        sectors.values().stream()
                .sorted(Comparator.comparing(Sector::freeSeats).reversed().thenComparing(Sector::getCode))
                .forEach(System.out::println);
    }

}

public class StaduimTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}