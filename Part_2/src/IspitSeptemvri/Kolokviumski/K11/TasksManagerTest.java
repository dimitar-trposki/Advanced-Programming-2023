package IspitSeptemvri.Kolokviumski.K11;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.*;

class DeadlineNotValidException extends Exception {

    public DeadlineNotValidException(LocalDateTime localDateTime) {
        super(String.format("The deadline %s has already passed", localDateTime));
    }

}

interface ITask {

    LocalDateTime getDeadline();

    int getPriority();

}

class Task implements ITask {

    String category;
    String name;
    String description;

    public Task(String category, String name, String description) {
        this.category = category;
        this.name = name;
        this.description = description;
    }

    @Override
    public LocalDateTime getDeadline() {
        return LocalDateTime.MAX;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' + "}";
    }

}

abstract class TaskDecorator implements ITask {

    ITask wrappedTask;

    public TaskDecorator(ITask wrappedTask) {
        this.wrappedTask = wrappedTask;
    }

}

class DeadlineDecorator extends TaskDecorator {

    LocalDateTime deadline;

    public DeadlineDecorator(ITask wrappedTask, LocalDateTime deadline) throws DeadlineNotValidException {
        super(wrappedTask);
        if (deadline.isBefore(LocalDateTime.of(2020, 6, 2, 0, 0))) {
            throw new DeadlineNotValidException(deadline);
        }
        this.deadline = deadline;
    }

    @Override
    public LocalDateTime getDeadline() {
        return deadline;
    }

    @Override
    public int getPriority() {
        return wrappedTask.getPriority();
    }

    @Override
    public String toString() {
        return wrappedTask.toString().replace("}", String.format(", deadline=%s}", deadline.toString()));
    }

}

class PriorityDecorator extends TaskDecorator {

    int priority;

    public PriorityDecorator(ITask wrappedTask, int priority) {
        super(wrappedTask);
        this.priority = priority;
    }

    @Override
    public LocalDateTime getDeadline() {
        return wrappedTask.getDeadline();
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return wrappedTask.toString().replace("}", String.format(", priority=%d}", priority));
    }

}

class TaskManager {

    Map<String, List<ITask>> tasks;

    public TaskManager() {
        this.tasks = new TreeMap<>();
    }

    public void readTasks(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        bufferedReader.lines().forEach(line -> {
            String[] parts = line.split(",");
            try {
                ITask task = new Task(parts[0], parts[1], parts[2]);
                if (parts.length == 5) {
                    task = new PriorityDecorator(new DeadlineDecorator(task, LocalDateTime.parse(parts[3])), Integer.parseInt(parts[4]));
                } else if (parts.length == 4) {
                    if (parts[3].length() > 2) {
                        task = new DeadlineDecorator(task, LocalDateTime.parse(parts[3]));
                    } else {
                        task = new PriorityDecorator(task, Integer.parseInt(parts[3]));
                    }
                }
                tasks.putIfAbsent(parts[0], new ArrayList<>());
                tasks.get(parts[0]).add(task);
            } catch (DeadlineNotValidException e) {
                System.out.println(e.getMessage());
            }

        });
    }

    void printTasks(OutputStream os, boolean includePriority, boolean includeCategory) {
        PrintWriter printWriter = new PrintWriter(os);
        Comparator<ITask> comparator;
        comparator = includePriority ?
                Comparator.comparing(ITask::getPriority)
                        .thenComparing(task -> Duration.between(LocalDateTime.now(), task.getDeadline())) :
                Comparator.comparing(task -> Duration.between(LocalDateTime.now(), task.getDeadline()));
        if (includeCategory) {
            tasks.entrySet().stream()
                    .forEach(entry -> {
                        printWriter.println(entry.getKey().toUpperCase());
                        entry.getValue().stream()
                                .sorted(comparator)
                                .forEach(printWriter::println);
                    });
        } else {
            tasks.values().stream()
                    .flatMap(Collection::stream)
                    .sorted(comparator)
                    .forEach(printWriter::println);
        }
        printWriter.flush();
    }

}

public class TasksManagerTest {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        System.out.println("Tasks reading");
        manager.readTasks(System.in);
        System.out.println("By categories with priority");
        manager.printTasks(System.out, true, true);
        System.out.println("-------------------------");
        System.out.println("By categories without priority");
        manager.printTasks(System.out, false, true);
        System.out.println("-------------------------");
        System.out.println("All tasks without priority");
        manager.printTasks(System.out, false, false);
        System.out.println("-------------------------");
        System.out.println("All tasks with priority");
        manager.printTasks(System.out, true, false);
        System.out.println("-------------------------");

    }
}