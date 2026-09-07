package tt.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import tt.task.Deadline;
import tt.task.Event;
import tt.task.Task;
import tt.task.Todo;

/** Reads and writes tt.TT tasks in a simple, human-readable text format. */
public class TaskStorage {
    private final Path filePath;

    /** Creates storage using the application's relative data file. */
    public TaskStorage() {
        this(Paths.get("data", "tt.TT.txt"));
    }

    /** Creates storage at the supplied path, primarily useful for tests. */
    public TaskStorage(Path filePath) {
        this.filePath = filePath;
    }

    /** Loads all valid tasks, returning an empty list when the file is absent. */
    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return tasks;
        }
        try {
            for (String line : Files.readAllLines(filePath, StandardCharsets.UTF_8)) {
                try {
                    Task task = parse(line);
                    if (task != null) {
                        tasks.add(task);
                    }
                } catch (IllegalArgumentException | DateTimeParseException ignored) {
                    // Ignore malformed lines so one corrupted task does not lose all data.
                }
            }
        } catch (IOException e) {
            System.out.println(" OOPS!!! I could not read the save file.");
        }
        return tasks;
    }

    /** Saves the complete task list, creating its parent directory when necessary. */
    public void save(List<Task> tasks) {
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            ArrayList<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(serialize(task));
            }
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println(" OOPS!!! I could not save your tasks.");
        }
    }

    /** Saves the supplied tasks, providing a concise API for a small number of tasks. */
    public void save(Task... tasks) {
        save(List.of(tasks));
    }

    private Task parse(String line) {
        String[] fields = line.split("\\|", -1);
        for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].trim();
        }
        if (fields.length < 3 || !isValidCompletionStatus(fields[1])) {
            throw new IllegalArgumentException();
        }
        Task task = switch (fields[0]) {
            case "T" -> new Todo(fields[2]);
            case "D" -> fields.length >= 4 ? new Deadline(fields[2], LocalDate.parse(fields[3].trim())) : null;
            case "E" -> fields.length >= 5 ? new Event(fields[2], fields[3], fields[4]) : null;
            default -> null;
        };
        if (task == null || task.getTask().isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (fields[1].equals("1")) {
            task.mark();
        }
        return task;
    }

    private boolean isValidCompletionStatus(String status) {
        return status.equals("0") || status.equals("1");
    }

    private String serialize(Task task) {
        String line = getTaskTypeCode(task) + " | "
                + (task.isDone() ? "1" : "0")
                + " | " + task.getTask();

        if (task instanceof Deadline deadline) {
            line += " | " + deadline.getBy();
        } else if (task instanceof Event event) {
            line += " | " + event.getFrom()
                    + " | " + event.getTo();
        }
        return line;
    }

    private String getTaskTypeCode(Task task) {
        if (task instanceof Todo) {
            return "T";
        } else if (task instanceof Deadline) {
            return "D";
        } else if (task instanceof Event) {
            return "E";
        }
        throw new IllegalArgumentException("Unsupported task type: " + task.getClass().getName());
    }
}
