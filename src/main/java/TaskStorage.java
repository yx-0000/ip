import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/** Reads and writes TT tasks in a simple, human-readable text format. */
public class TaskStorage {
    private final Path filePath;

    /** Creates storage using the application's relative data file. */
    public TaskStorage() {
        this(Paths.get("data", "duke.txt"));
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
                } catch (IllegalArgumentException ignored) {
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
                String type = task instanceof Todo ? "T" : task instanceof Deadline ? "D" : "E";
                String details = type.equals("D") ? ((Deadline) task).getBy()
                        : type.equals("E") ? ((Event) task).getFrom() + "|" + ((Event) task).getTo() : "";
                lines.add(type + "|" + (task.isDoneValue() ? "1" : "0") + "|" + task.getTask() + "|" + details);
            }
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println(" OOPS!!! I could not save your tasks.");
        }
    }

    private Task parse(String line) {
        String[] fields = line.split("\\|", -1);
        if (fields.length < 3 || (fields[1].equals("0") == false && fields[1].equals("1") == false)) {
            throw new IllegalArgumentException();
        }
        Task task = switch (fields[0]) {
            case "T" -> new Todo(fields[2]);
            case "D" -> fields.length >= 4 ? new Deadline(fields[2], fields[3]) : null;
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
}
