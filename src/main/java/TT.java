import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class TT {
    public static void main(String[] args) {

        String divider = "________________________________________ \n";
        String banner = """
                     --------  --------
                         -        -
                         -        -
                         -        -
                         -        -
                         -        -
                    """;


        String intro = "Hello! I'm TT.\n" + "What can I do for you?";
        String bye = "Bye. Hope to see you again soon!";
        String addtask = "Got it. I've added this task: \n";

        System.out.println(divider);
        System.out.println(banner);
        System.out.println(intro);
        System.out.println(divider);

        ArrayList<Task> tasks = new ArrayList<>();
        TaskStorage storage = new TaskStorage();
        tasks.addAll(storage.load());
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();
            String commandWord = input.split(" ", 2)[0];
            String rest = input.contains(" ") ? input.substring(input.indexOf(' ') + 1).trim() : "";
            Command command = Command.fromString(commandWord);

            try {
                switch (command) {
                    case BYE:
                        System.out.println(divider + bye);
                        System.out.println(divider);
                        scanner.close();
                        return;

                    case LIST:
                        System.out.println(divider + "Here are the tasks in your list: ");
                        for (int i = 0; i < tasks.size(); i++) {
                            System.out.println((i + 1) + "." + tasks.get(i).toString());
                        }
                        System.out.println(divider);
                        break;

                    case MARK: {
                        int index = parseIndex(rest, tasks.size());
                        tasks.get(index).mark();
                        storage.save(tasks);
                        System.out.println(divider + "Nice! I've marked this task as done: \n" + tasks.get(index).toString());
                        System.out.println(divider);
                        break;
                    }

                    case UNMARK: {
                        int index = parseIndex(rest, tasks.size());
                        tasks.get(index).unmark();
                        storage.save(tasks);
                        System.out.println(divider + "OK, I've marked this task as not done yet: \n" + tasks.get(index).toString());
                        System.out.println(divider);
                        break;
                    }

                    case DELETE: {
                        int index = parseIndex(rest, tasks.size());
                        Task removed = tasks.remove(index);
                        storage.save(tasks);
                        System.out.println(divider
                                + "Noted. I've removed this task:\n"
                                + "  " + removed.toString() + "\n"
                                + "Now you have " + tasks.size() + " tasks in the list.\n"
                                + divider);
                        break;
                    }

                    case TODO: {
                        if (rest.isEmpty()) {
                            throw new TTException(" OOPS!!! The description of a todo cannot be empty.");
                        }
                        Todo todo = new Todo(rest);
                        tasks.add(todo);
                        storage.save(tasks);
                        System.out.println(divider
                                + addtask
                                + "  " + todo.toString() + "\n"
                                + "Now you have " + tasks.size() + " tasks in the list. \n"
                                + divider);
                        break;
                    }

                    case DEADLINE: {
                        if (!rest.contains("/by")) {
                            throw new TTException(" OOPS!!! A deadline needs a '/by' date, e.g. deadline return book /by Sunday.");
                        }
                        int slash = rest.indexOf("/");
                        String name = rest.substring(0, slash).trim();
                        String by = rest.substring(slash + 3).trim();
                        if (name.isEmpty() || by.isEmpty()) {
                            throw new TTException(" OOPS!!! The description or /by date of a deadline cannot be empty.");
                        }
                        LocalDate deadlineDate;
                        try {
                            deadlineDate = LocalDate.parse(by);
                        } catch (DateTimeParseException e) {
                            throw new TTException(" OOPS!!! Please enter the deadline as yyyy-MM-dd, e.g. 2019-10-15.");
                        }
                        Deadline deadline = new Deadline(name, deadlineDate);
                        tasks.add(deadline);
                        storage.save(tasks);
                        System.out.println(divider
                                + addtask
                                + "  " + deadline.toString() + "\n"
                                + "Now you have " + tasks.size() + " tasks in the list. \n"
                                + divider);
                        break;
                    }

                    case EVENT: {
                        if (!rest.contains("/from") || !rest.contains("/to")) {
                            throw new TTException(" OOPS!!! An event needs both '/from' and '/to', e.g. event meeting /from Mon 2pm /to 4pm.");
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
                            throw new TTException(" OOPS!!! The description, /from, or /to of an event cannot be empty.");
                        }
                        Event event = new Event(eventName, from, to);
                        tasks.add(event);
                        storage.save(tasks);
                        System.out.println(divider
                                + addtask
                                + "  " + event.toString() + "\n"
                                + "Now you have " + tasks.size() + " tasks in the list. \n"
                                + divider);
                        break;
                    }

                    case UNKNOWN:
                    default:
                        throw new TTException(" OOPS!!! I'm sorry, but I don't know what that means :-(");
                }

            } catch (TTException e) {
                System.out.println(divider + e.getMessage() + "\n" + divider);
            }
        }
    }

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
