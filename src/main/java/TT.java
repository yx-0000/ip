import java.util.Scanner;

public class TT {
    public static void main(String[] args) {

        String divider = "_________________________________________________________ \n";
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

        Task[] tasks = new Task[100];
        int taskCount = 0;

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();

            try {
                if (input.equals("bye")) {
                    System.out.println(divider + bye);
                    System.out.println(divider);
                    break;

                } else if (input.equals("list")) {
                    System.out.println(divider + "Here are the tasks in your list: ");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println((i + 1) + "." + tasks[i].toString());
                    }
                    System.out.println(divider);

                } else if (input.startsWith("mark ")) {
                    int index = parseIndex(input.substring(5), taskCount);
                    tasks[index].mark();
                    System.out.println(divider + "Nice! I've marked this task as done: \n" + tasks[index].toString());
                    System.out.println(divider);

                } else if (input.startsWith("unmark ")) {
                    int index = parseIndex(input.substring(7), taskCount);
                    tasks[index].unmark();
                    System.out.println(divider + "OK, I've marked this task as not done yet: \n" + tasks[index].toString());
                    System.out.println(divider);

                } else if (input.equals("todo") || input.startsWith("todo ")) {
                    String description = input.length() > 4 ? input.substring(4).trim() : "";
                    if (description.isEmpty()) {
                        throw new TTException(" OOPS!!! The description of a todo cannot be empty.");
                    }

                    Todo todo = new Todo(description);
                    tasks[taskCount] = todo;
                    taskCount++;

                    System.out.println(divider
                            + addtask
                            + "  "
                            + todo.toString()
                            + "\n"
                            + "Now you have " + taskCount + " tasks in the list. \n"
                            + divider);

                } else if (input.equals("deadline") || input.startsWith("deadline ")) {
                    if (!input.contains("/by")) {
                        throw new TTException(" OOPS!!! A deadline needs a '/by' date, e.g. deadline return book /by Sunday.");
                    }

                    int slash = input.indexOf("/");
                    String name = input.substring(8, slash).trim();
                    String by = input.substring(slash + 3).trim();

                    if (name.isEmpty() || by.isEmpty()) {
                        throw new TTException(" OOPS!!! The description or /by date of a deadline cannot be empty.");
                    }

                    Deadline deadline = new Deadline(name, by);
                    tasks[taskCount] = deadline;
                    taskCount++;

                    System.out.println(divider
                            + addtask
                            + "  "
                            + deadline.toString()
                            + "\n"
                            + "Now you have " + taskCount + " tasks in the list. \n"
                            + divider);

                } else if (input.equals("event") || input.startsWith("event ")) {
                    if (!input.contains("/from") || !input.contains("/to")) {
                        throw new TTException(" OOPS!!! An event needs both '/from' and '/to', e.g. event meeting /from Mon 2pm /to 4pm.");
                    }

                    int firstSlash = input.indexOf("/");
                    int secondSlash = input.indexOf("/", firstSlash + 1);

                    if (secondSlash == -1) {
                        throw new TTException(" OOPS!!! An event needs both '/from' and '/to'.");
                    }

                    String eventName = input.substring(5, firstSlash).trim();
                    String from = input.substring(firstSlash + 5, secondSlash - 1).trim();
                    String to = input.substring(secondSlash + 3).trim();

                    if (eventName.isEmpty() || from.isEmpty() || to.isEmpty()) {
                        throw new TTException(" OOPS!!! The description, /from, or /to of an event cannot be empty.");
                    }

                    Event event = new Event(eventName, from, to);
                    tasks[taskCount] = event;
                    taskCount++;

                    System.out.println(divider
                            + addtask
                            + "  "
                            + event.toString()
                            + "\n"
                            + "Now you have " + taskCount + " tasks in the list. \n"
                            + divider);

                } else {
                    throw new TTException(" OOPS!!! I'm sorry, but I don't know what that means :-(");
                }

            } catch (TTException e) {
                System.out.println(divider + e.getMessage() + "\n" + divider);
            }
        }

        scanner.close();
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