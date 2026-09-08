package tt;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import tt.storage.TaskStorage;
import tt.task.Deadline;
import tt.task.Event;
import tt.task.Task;
import tt.task.Todo;

/** Processes task-management commands and generates user-facing responses. */
public class TT {
    private final ArrayList<Task> tasks;
    private final TaskStorage storage;

    /** Creates a task manager backed by the default save file. */
    public TT() {
        this(new TaskStorage());
    }

    /** Creates a task manager backed by the supplied storage. */
    public TT(TaskStorage storage) {
        assert storage != null : "Storage must not be null";
        this.storage = storage;
        tasks = storage.load();
        assert tasks != null : "Storage must return a task list";
    }

    /** Processes one user command and returns the chatbot's response. */
    public String getResponse(String input) {
        String trimmedInput = input.trim();
        if (trimmedInput.isEmpty()) {
            return "Please enter a command.";
        }

        String commandWord = trimmedInput.split(" ", 2)[0];
        String rest = trimmedInput.contains(" ")
                ? trimmedInput.substring(trimmedInput.indexOf(' ') + 1).trim()
                : "";

        try {
            return execute(Command.fromString(commandWord), rest);
        } catch (TTException e) {
            return e.getMessage();
        }
    }

    private String execute(Command command, String rest) throws TTException {
        assert command != null : "Command must not be null";
        assert rest != null : "Command arguments must not be null";

        return switch (command) {
            case BYE -> "Bye. Hope to see you again soon!";
            case LIST -> formatTasks(tasks, "Here are the tasks in your list:");
            case FIND -> findTasks(rest);
            case SORT -> sortTasks(rest);
            case MARK -> setDone(rest, true);
            case UNMARK -> setDone(rest, false);
            case DELETE -> deleteTask(rest);
            case CLEAR -> clearTasks(rest);
            case TODO -> addTodo(rest);
            case DEADLINE -> addDeadline(rest);
            case EVENT -> addEvent(rest);
            case UNKNOWN -> throw new TTException("OOPS!!! I'm sorry, but I don't know what that means :-(");
        };
    }

    private String findTasks(String keywordText) throws TTException {
        if (keywordText.isEmpty()) {
            throw new TTException("OOPS!!! Please provide a keyword to search for, e.g. find book.");
        }

        String keyword = keywordText.toLowerCase(Locale.ROOT);
        List<Task> matchingTasks = tasks.stream()
                .filter(task -> task.getTask().toLowerCase(Locale.ROOT).contains(keyword))
                .toList();
        return formatTasks(matchingTasks, "Here are the matching tasks in your list:");
    }

    private String sortTasks(String arguments) throws TTException {
        if (!arguments.isEmpty()) {
            throw new TTException("OOPS!!! The sort command does not take additional arguments.");
        }

        tasks.sort(Comparator.comparing(Task::getTask, String.CASE_INSENSITIVE_ORDER));
        storage.save(tasks);
        return formatTasks(tasks, "Here are your tasks sorted alphabetically:");
    }

    private String setDone(String numberText, boolean isDone) throws TTException {
        Task task = tasks.get(parseIndex(numberText, tasks.size()));
        if (isDone) {
            task.mark();
        } else {
            task.unmark();
        }
        storage.save(tasks);

        String message = isDone
                ? "Nice! I've marked this task as done:\n"
                : "OK, I've marked this task as not done yet:\n";
        return message + task;
    }

    private String deleteTask(String numberText) throws TTException {
        Task removedTask = tasks.remove(parseIndex(numberText, tasks.size()));
        storage.save(tasks);
        return "Noted. I've removed this task:\n" + removedTask
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String clearTasks(String taskTypeText) throws TTException {
        String taskType = taskTypeText.toLowerCase(Locale.ROOT);
        int originalTaskCount = tasks.size();

        switch (taskType) {
            case "todo", "todos" -> tasks.removeIf(task -> task instanceof Todo);
            case "deadline", "deadlines" -> tasks.removeIf(task -> task instanceof Deadline);
            case "event", "events" -> tasks.removeIf(task -> task instanceof Event);
            case "all", "task", "tasks" -> tasks.clear();
            case "" -> throw new TTException("OOPS!!! To clear tasks, use clear todo, clear deadline, "
                    + "clear event, or clear all.");
            default -> throw new TTException("OOPS!!! I can only clear todo, deadline, event, or all tasks.");
        }

        int clearedTaskCount = originalTaskCount - tasks.size();
        storage.save(tasks);
        return "Cleared " + clearedTaskCount + " " + getClearDescription(taskType, clearedTaskCount) + ".";
    }

    private String getClearDescription(String taskType, int clearedTaskCount) {
        String description = switch (taskType) {
            case "todo", "todos" -> "todo task";
            case "deadline", "deadlines" -> "deadline task";
            case "event", "events" -> "event task";
            default -> "task";
        };
        return clearedTaskCount == 1 ? description : description + "s";
    }

    private String addTodo(String description) throws TTException {
        if (description.isEmpty()) {
            throw new TTException("OOPS!!! The description of a todo cannot be empty.");
        }
        return addTask(new Todo(description));
    }

    private String addDeadline(String details) throws TTException {
        if (!details.contains("/by")) {
            throw new TTException("OOPS!!! A deadline needs a '/by' date, e.g. "
                    + "deadline return book /by 2019-10-15.");
        }

        int separator = details.indexOf("/by");
        String description = details.substring(0, separator).trim();
        String dateText = details.substring(separator + 3).trim();
        if (description.isEmpty() || dateText.isEmpty()) {
            throw new TTException("OOPS!!! The description or /by date of a deadline cannot be empty.");
        }

        try {
            return addTask(new Deadline(description, LocalDate.parse(dateText)));
        } catch (DateTimeParseException e) {
            throw new TTException("OOPS!!! Please enter the deadline as yyyy-MM-dd, e.g. 2019-10-15.");
        }
    }

    private String addEvent(String details) throws TTException {
        int fromSeparator = details.indexOf("/from");
        int toSeparator = details.indexOf("/to", Math.max(fromSeparator, 0) + 5);
        if (fromSeparator < 0 || toSeparator < 0) {
            throw new TTException("OOPS!!! An event needs both '/from' and '/to', "
                    + "e.g. event meeting /from Mon 2pm /to 4pm.");
        }

        String description = details.substring(0, fromSeparator).trim();
        String from = details.substring(fromSeparator + 5, toSeparator).trim();
        String to = details.substring(toSeparator + 3).trim();
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new TTException("OOPS!!! The description, /from, or /to of an event cannot be empty.");
        }
        return addTask(new Event(description, from, to));
    }

    private String addTask(Task task) {
        assert task != null : "Task to add must not be null";

        tasks.add(task);
        storage.save(tasks);
        return "Got it. I've added this task:\n" + task
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String formatTasks(List<Task> tasksToFormat, String heading) {
        assert tasksToFormat != null : "Task list to format must not be null";
        assert heading != null && !heading.isBlank() : "Task list heading must not be blank";

        if (tasksToFormat.isEmpty()) {
            return heading + "\nThere are no tasks to show.";
        }

        StringBuilder response = new StringBuilder(heading);
        for (int i = 0; i < tasksToFormat.size(); i++) {
            response.append("\n").append(i + 1).append(". ").append(tasksToFormat.get(i));
        }
        return response.toString();
    }

    private int parseIndex(String numberText, int taskCount) throws TTException {
        int index;
        try {
            index = Integer.parseInt(numberText.trim()) - 1;
        } catch (NumberFormatException e) {
            throw new TTException("OOPS!!! Please enter a valid task number, e.g. mark 2.");
        }

        if (index < 0 || index >= taskCount) {
            throw new TTException("OOPS!!! That task number doesn't exist.");
        }
        assert index >= 0 && index < taskCount : "Parsed task index must be within bounds";
        return index;
    }
}
