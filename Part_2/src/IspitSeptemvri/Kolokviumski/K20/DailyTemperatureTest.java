package IspitSeptemvri.Kolokviumski.K20;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

class Temperature {

    int day;
    List<Double> dailyTemperatures;
    char COrF;

    public Temperature(String line) {
        String[] parts = line.split("\\s+");
        this.day = Integer.parseInt(parts[0]);
        this.COrF = parts[1].charAt(parts[1].length() - 1);
        this.dailyTemperatures = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            String toReplace = String.valueOf(COrF);
            parts[i] = parts[i].replace(toReplace, "");
            dailyTemperatures.add(Double.parseDouble(parts[i]));
        }
    }

    public int getDay() {
        return day;
    }

    public double CtoF(double c) {
        return ((double) 9 / 5 * c) + 32;
    }

    public double FtoC(double f) {
        return ((double) 5 / 9) * (f - 32);
    }

    public String toString(char scale) {
        DoubleSummaryStatistics dss;
        if (COrF == scale) {
            dss = dailyTemperatures.stream()
                    .mapToDouble(i -> i)
                    .summaryStatistics();
        } else {
            dss = dailyTemperatures.stream()
                    .mapToDouble(i -> scale == 'C' ? FtoC(i) : CtoF(i))
                    .summaryStatistics();
        }
        return String.format("%3d: Count: %3d Min: %6.2f%s Max: %6.2f%s Avg: %6.2f%s",
                day, dss.getCount(), dss.getMin(), scale, dss.getMax(), scale, dss.getAverage(), scale);
    }

}

class DailyTemperatures {

    List<Temperature> temperatures;

    DailyTemperatures() {
        this.temperatures = new ArrayList<>();
    }

    public void readTemperatures(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        temperatures = bufferedReader.lines()
                .map(Temperature::new)
                .collect(Collectors.toList());
    }

    public void writeDailyStats(OutputStream outputStream, char scale) {
        PrintWriter pw = new PrintWriter(outputStream);

        temperatures.stream()
                .sorted(Comparator.comparing(Temperature::getDay))
                .forEach(t -> pw.println(t.toString(scale)));

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