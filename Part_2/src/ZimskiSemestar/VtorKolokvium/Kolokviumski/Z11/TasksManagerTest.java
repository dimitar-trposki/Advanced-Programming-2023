package ZimskiSemestar.VtorKolokvium.Kolokviumski.Z11;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class DeadlineNotValidException extends Exception {
    public DeadlineNotValidException(String message) {
        super(String.format("The deadline %s has already passed", message));
    }
}

interface ITask {
    LocalDateTime getDeadLine();

    int getPriority();
}

class Task implements ITask {
    String name;
    String description;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public LocalDateTime getDeadLine() {
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
                ", description='" + description + '\'';
    }
}

abstract class TaskDecorator implements ITask {
    ITask wrappedTask;

    public TaskDecorator(ITask wrappedTask) {
        this.wrappedTask = wrappedTask;
    }
}

class DeadLineDecorator extends TaskDecorator {
    LocalDateTime deadLine;

    public DeadLineDecorator(ITask wrappedTask, LocalDateTime deadLine) throws DeadlineNotValidException {
        super(wrappedTask);
        if (deadLine.isBefore(LocalDateTime.of(2020, 6, 2, 23, 59, 59))) {
            throw new DeadlineNotValidException(deadLine.toString());
        }
        this.deadLine = deadLine;
    }

    @Override
    public LocalDateTime getDeadLine() {
        return deadLine;
    }

    @Override
    public int getPriority() {
        return wrappedTask.getPriority();
    }

    @Override
    public String toString() {
        return wrappedTask.toString() + ", deadline=" + deadLine;
    }
}

class PriorityDecorator extends TaskDecorator {
    int priority;

    public PriorityDecorator(ITask wrappedTask, int priority) {
        super(wrappedTask);
        this.priority = priority;
    }

    @Override
    public LocalDateTime getDeadLine() {
        return wrappedTask.getDeadLine();
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return wrappedTask.toString() + ", priority=" + priority;
    }
}

class TaskManager {
    Map<String, List<ITask>> tasks;

    public TaskManager() {
        tasks = new HashMap<>();
    }

    public void readTasks(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        List<String> taskList = br.lines().collect(Collectors.toList());

        for (String l : taskList) {
            try {
                String[] parts = l.split(",");
                ITask task = getiTask(parts);
                tasks.putIfAbsent(parts[0], new ArrayList<>());
                tasks.get(parts[0]).add(task);
            } catch (DeadlineNotValidException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static ITask getiTask(String[] parts) throws DeadlineNotValidException {
        ITask task = new Task(parts[1], parts[2]);
        if (parts.length == 4 || parts.length == 5) {
            if (parts[3].length() > 2) {
                task = new DeadLineDecorator(task, LocalDateTime.parse(parts[3]));
            } else {
                task = new PriorityDecorator(task, Integer.parseInt(parts[3]));
            }
        }
        if (parts.length == 5) {
            task = new PriorityDecorator(task, Integer.parseInt(parts[4]));
        }
        return task;
    }

    public void printTasks(OutputStream os, boolean includePriority, boolean includeCategory) {
        PrintWriter pw = new PrintWriter(os);
        Comparator<ITask> comparator;
        if (includePriority) {
            comparator = Comparator.comparing(ITask::getPriority)
                    .thenComparing(task -> Duration.between(LocalDateTime.now(), task.getDeadLine()).toSeconds());
        } else {
            comparator = Comparator.comparing(task -> Duration.between(LocalDateTime.now(), task.getDeadLine()).toSeconds());
        }
        if (includeCategory) {
            tasks.keySet()
                    .forEach(key -> {
                        pw.println(key.toUpperCase());
                        tasks.get(key).stream()
                                .sorted(comparator)
                                .forEach(taskComponent -> pw.println(taskComponent + "}"));
                    });
        } else {
            tasks.values().stream()
                    .flatMap(List::stream)
                    .sorted(comparator)
                    .forEach(taskComponent -> System.out.println(taskComponent + "}"));
        }
        pw.flush();
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
