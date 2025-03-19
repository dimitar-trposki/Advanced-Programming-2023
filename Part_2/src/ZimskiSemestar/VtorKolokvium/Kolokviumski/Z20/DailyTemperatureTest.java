package ZimskiSemestar.VtorKolokvium.Kolokviumski.Z20;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class WeatherMeasurement implements Comparable<WeatherMeasurement> {
    int day;
    List<Double> measurements;
    String celOrFah;

    public WeatherMeasurement(String line) {
        String[] parts = line.split("\\s+");
        day = Integer.parseInt(parts[0]);
        measurements = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            String measurement = parts[i];
            double value = Double.parseDouble(measurement.substring(0, measurement.length() - 1));
            celOrFah = String.valueOf(measurement.charAt(measurement.length() - 1));
            measurements.add(value);
        }
    }

    public DoubleSummaryStatistics summaryStatistics() {
        return measurements.stream().mapToDouble(i -> i).summaryStatistics();
    }

    public double min() {
        return summaryStatistics().getMin();
    }

    public double max() {
        return summaryStatistics().getMax();
    }

    public double average() {
        return summaryStatistics().getAverage();
    }

    private double convertToFahrenheit(double celsius) {
        return celsius * 9 / 5 + 32;
    }

    private double convertToCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5 / 9;
    }

    public int getDay() {
        return day;
    }

    public String toString(char scale) {
        if (scale == 'C') {
            if (celOrFah.equals("F")) {
                return String.format("%3d: Count:%4d Min: %6.2fC Max: %6.2fC Avg: %6.2fC",
                        day, measurements.size(), convertToCelsius(min()), convertToCelsius(max()), convertToCelsius(average()));
            }
            return String.format("%3d: Count:%4d Min: %6.2fC Max: %6.2fC Avg: %6.2fC",
                    day, measurements.size(), min(), max(), average());
        } else {
            if (celOrFah.equals("C")) {
                return String.format("%3d: Count:%4d Min: %6.2fF Max: %6.2fF Avg: %6.2fF",
                        day, measurements.size(), convertToFahrenheit(min()), convertToFahrenheit(max()), convertToFahrenheit(average()));
            }
            return String.format("%3d: Count:%4d Min: %6.2fF Max: %6.2fF Avg: %6.2fF",
                    day, measurements.size(), min(), max(), average());
        }
    }

    @Override
    public int compareTo(WeatherMeasurement o) {
        Comparator<WeatherMeasurement> comparator = Comparator.comparing(WeatherMeasurement::getDay);
        return comparator.compare(this, o);
    }
}

class DailyTemperatures {
    Set<WeatherMeasurement> measurements;

    public DailyTemperatures() {
        measurements = new HashSet<>();
    }

    public void readTemperatures(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        measurements = br.lines().map(WeatherMeasurement::new)
                .collect(Collectors.toSet());
    }

    void writeDailyStats(OutputStream outputStream, char scale) {
        PrintWriter pw = new PrintWriter(outputStream);

        measurements.stream().sorted().forEach(l -> pw.println(l.toString(scale)));

        pw.flush();
    }
}

public class DailyTemperatureTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}
