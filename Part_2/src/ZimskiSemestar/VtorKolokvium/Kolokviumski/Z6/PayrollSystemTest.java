package ZimskiSemestar.VtorKolokvium.Kolokviumski.Z6;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

class PayrollSystem {
    private Map<String, Double> hourlyRateByLevel;
    private Map<String, Double> ticketRateByLevel;
    private List<Employee> employees;

    public PayrollSystem(Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;
        employees = new ArrayList<>();
    }

    public void readEmployees(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        List<String> lines = br.lines().collect(Collectors.toList());
        for (String line : lines) {
            String[] parts = line.split(";");

            String type = parts[0];
            String id = parts[1];
            String level = parts[2];

            if (type.equals("H")) {
                float hours = (float) Double.parseDouble(parts[3]);
                HourlyEmployee newEmployee = new HourlyEmployee(id, level, hours, hourlyRateByLevel.get(level));
                employees.add(newEmployee);
            } else {
                List<Integer> list = Arrays.stream(parts)
                        .skip(3)
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                FreelanceEmployee newEmployee = new FreelanceEmployee(id, level, list, ticketRateByLevel.get(level));
                employees.add(newEmployee);
            }
        }
    }

    public Map<String, Set<Employee>> printEmployeesByLevels(OutputStream os, Set<String> levels) {
        Map<String, Set<Employee>> levelToEmployees = new LinkedHashMap<>();
        levels.forEach(l -> {
            employees.stream()
                    .filter(e -> e.getLevel().equals(l))
                    .forEach(e ->
                    {
                        levelToEmployees.putIfAbsent(l, new TreeSet<>(Employee.EMPLOYEE_COMPARATOR));
                        levelToEmployees.get(l).add(e);
                    });
        });
        return levelToEmployees;
    }
}

abstract class Employee {
    private String id;
    private String level;
    public static final Comparator<Employee> EMPLOYEE_COMPARATOR =
            Comparator.comparing(Employee::getSalary).reversed()
                    .thenComparing(Employee::getLevel);

    public Employee(String id, String level) {
        this.id = id;
        this.level = level;
    }

    public abstract double getSalary();

    public String getId() {
        return id;
    }

    public String getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f", id, level, getSalary());
    }
}

class HourlyEmployee extends Employee {
    private double hours;
    private double hourlyRateByLevel;
    private double regularHours, overtimeHours;

    public HourlyEmployee(String id, String level, float hours, Double hourlyRateByLevel) {
        super(id, level);
        this.hours = hours;
        this.hourlyRateByLevel = hourlyRateByLevel;
        if (hours <= 40) {
            regularHours = hours;
            overtimeHours = 0;
        } else {
            regularHours = 40;
            overtimeHours = hours - 40;
        }
    }

    @Override
    public double getSalary() {
        return (regularHours * hourlyRateByLevel + overtimeHours * hourlyRateByLevel * 1.500);
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" Regular hours: %.2f Overtime hours: %.2f", regularHours, overtimeHours);
    }
}

class FreelanceEmployee extends Employee {
    private List<Integer> ticketPoints;
    private double ticketRateByLevel;
    private int sum;

    public FreelanceEmployee(String id, String level, List<Integer> ticketPoints, Double ticketRateByLevel) {
        super(id, level);
        this.ticketPoints = ticketPoints;
        this.ticketRateByLevel = ticketRateByLevel;
        sum = ticketPoints.stream().mapToInt(i -> i).sum();
    }

    @Override
    public double getSalary() {
        return (double) sum * ticketRateByLevel;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" Tickets count: %d Tickets points: %d", ticketPoints.size(), sum);
    }
}

public class PayrollSystemTest {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployees(System.in);

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i = 5; i <= 10; i++) {
            levels.add("level" + i);
        }
        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: " + level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
        });


    }
}