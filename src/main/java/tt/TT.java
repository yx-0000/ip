package tt;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import tt.storage.TaskStorage;
import tt.task.Deadline;
import tt.task.Event;
import tt.task.Task;
import tt.task.Todo;

/** Starts the task manager and coordinates commands, storage, and the user interface. */
public class TT {
    /** Runs the interactive task manager until the user enters {@code bye}. */
    public static void main(String[] args) {

        ArrayList<Task> tasks = new ArrayList<>();
        TaskStorage storage = new TaskStorage();
        tasks.addAll(storage.load());
        Ui ui = new Ui();
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();
            String commandWord = input.split(" ", 2)[0];
            String rest = input.contains(" ") ? input.substring(input.indexOf(' ') + 1).trim() : "";
            Command command = Command.fromString(commandWord);

            try {
                switch (command) {
                    case Command.BYE:
                        ui.showBye();
                        return;

                    case Command.LIST:
                        ui.showTasks(tasks);
                        break;

                    case Command.FIND:
                        if (rest.isEmpty()) {
                            throw new TTException(" OOPS!!! Please provide a keyword to search for, e.g. find book.");
                        }
                        ArrayList<Task> matchingTasks = new ArrayList<>();
                        String keyword = rest.toLowerCase(Locale.ROOT);
                        for (Task task : tasks) {
                            if (task.getTask().toLowerCase(Locale.ROOT).contains(keyword)) {
                                matchingTasks.add(task);
                            }
                        }
                        ui.showMatchingTasks(matchingTasks);
                        break;

                    case Command.MARK: {
                        int index = parseIndex(rest, tasks.size());
                        tasks.get(index).mark();
                        storage.save(tasks);
                        ui.showMarked(tasks.get(index), true);
                        break;
                    }

                    case Command.UNMARK: {
                        int index = parseIndex(rest, tasks.size());
                        tasks.get(index).unmark();
                        storage.save(tasks);
                        ui.showMarked(tasks.get(index), false);
                        break;
                    }

                    case Command.DELETE: {
                        int index = parseIndex(rest, tasks.size());
                        Task removed = tasks.remove(index);
                        storage.save(tasks);
                        ui.showDeleted(removed, tasks.size());
                        break;
                    }

                    case Command.TODO: {
                        if (rest.isEmpty()) {
                            throw new TTException(" OOPS!!! The description of a todo cannot be empty.");
                        }
                        Todo todo = new Todo(rest);
                        tasks.add(todo);
                        storage.save(tasks);
                        ui.showAdded(todo, tasks.size());
                        break;
                    }

                    case Command.DEADLINE: {
                        if (!rest.contains("/by")) {
                            throw new TTException(" OOPS!!! A deadline needs a '/by' date, e.g. "
                                    + "deadline return book /by Sunday.");
                        }
                        int slash = rest.indexOf("/");
                        String name = rest.substring(0, slash).trim();
                        String by = rest.substring(slash + 3).trim();
                        if (name.isEmpty() || by.isEmpty()) {
                            throw new TTException(" OOPS!!! The description or /by date of a deadline "
                                    + "cannot be empty.");
                        }
                        LocalDate deadlineDate;
                        try {
                            deadlineDate = LocalDate.parse(by);
                        } catch (DateTimeParseException e) {
                            throw new TTException(" OOPS!!! Please enter the deadline as yyyy-MM-dd, "
                                    + "e.g. 2019-10-15.");
                        }
                        Deadline deadline = new Deadline(name, deadlineDate);
                        tasks.add(deadline);
                        storage.save(tasks);
                        ui.showAdded(deadline, tasks.size());
                        break;
                    }

                    case Command.EVENT: {
                        if (!rest.contains("/from") || !rest.contains("/to")) {
                            throw new TTException(" OOPS!!! An event needs both '/from' and '/to', "
                                    + "e.g. event meeting /from Mon 2pm /to 4pm.");
                        }
                        int firstSlash = rest.indexOf("/");
                        int secondSlash = rest.indexOf("/", firstSlash + 1);
                        if (secondSlash == -1) {
                            throw new TTException(" OOPS!!! An event needs both '/from' and '/to'.");
                        }
                        String eventName = rest.substring(0, firstSlash).trim();
                        String from = rest.substring(firstSlash + 5, secondSlash - 1).trim();
                        String to = rest.substring(secondSlash + 3).trim();
                        if (eventName.isEmpty() || from.isEmpty() || to.isEmpty()) {
                            throw new TTException(" OOPS!!! The description, /from, or /to of an event "
                                    + "cannot be empty.");
                        }
                        Event event = new Event(eventName, from, to);
                        tasks.add(event);
                        storage.save(tasks);
                        ui.showAdded(event, tasks.size());
                        break;
                    }

                    case Command.UNKNOWN:
                    default:
                        throw new TTException(" OOPS!!! I'm sorry, but I don't know what that means :-(");
                }

            } catch (TTException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /** Converts a one-based task number into a valid zero-based list index. */
    private static int parseIndex(String numberPart, int taskCount) throws TTException {
        int index;
        try {
            index = Integer.parseInt(numberPart.trim()) - 1;
        } catch (NumberFormatException e) {
            throw new TTException(" OOPS!!! Please enter a valid task number, e.g. mark 2.");
        }

        if (index < 0 || index >= taskCount) {
            throw new TTException(" OOPS!!! That task number doesn't exist.");
        }

        return index;
    }
}
