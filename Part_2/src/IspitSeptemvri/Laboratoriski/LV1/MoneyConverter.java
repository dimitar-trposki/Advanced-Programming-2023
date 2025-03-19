package IspitSeptemvri.Laboratoriski.LV1;

public class MoneyConverter {
    public static void main(String[] args) {
        System.out.println(String.format("%s", converterToString(12.5)));
    }

    public static double convertToDouble(String amount) {
        amount = amount.replace("$", "");
        String[] parts = amount.split("\\.");
        double wholeDollar = Double.parseDouble(parts[0]);
        double cents = Double.parseDouble(parts[1]) / 100.0;
        return wholeDollar + cents;
    }

    public static String converterToString(double amount) {
        return String.format("%.2f$", amount);
    }
}
