package tt;

import tt.task.Task;
import java.util.List;
import java.util.Scanner;

/** Handles all communication between tt.TT and the user. */
public class Ui {
    private static final String DIVIDER = "________________________________________ \n";
    private static final String BANNER = """
                         --------  --------
                             -        -
                             -        -
                             -        -
                             -        -
                             -        -
                        """;
    private static final String BYE = "Bye. Hope to see you again soon!";
    private static final String ADD_TASK = "Got it. I've added this task: \n";
    private final Scanner scanner;

    /** Creates a user interface connected to standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Displays the initial greeting. */
    public void showWelcome() {
        System.out.println(DIVIDER);
        System.out.println(BANNER);
        System.out.println("Hello! I'm tt.TT.\nWhat can I do for you?");
        System.out.println(DIVIDER);
    }

    /** Reads one complete command from the user. */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Displays the current tasks. */
    public void showTasks(List<Task> tasks) {
        System.out.println(DIVIDER + "Here are the tasks in your list: ");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        showDivider();
    }

    public void showBye() {
        System.out.println(DIVIDER + BYE);
        showDivider();
        scanner.close();
    }

    public void showMarked(Task task, boolean isDone) {
        String message = isDone
                ? "Nice! I've marked this task as done: \n"
                : "OK, I've marked this task as not done yet: \n";
        System.out.println(DIVIDER + message + task + "\n" + DIVIDER);
    }

    public void showDeleted(Task task, int remaining) {
        System.out.println(DIVIDER + "Noted. I've removed this task:\n  " + task
                + "\nNow you have " + remaining + " tasks in the list.\n" + DIVIDER);
    }

    public void showAdded(Task task, int total) {
        System.out.println(DIVIDER + ADD_TASK + "  " + task
                + "\nNow you have " + total + " tasks in the list. \n" + DIVIDER);
    }

    public void showError(String message) {
        System.out.println(DIVIDER + message + "\n" + DIVIDER);
    }

    /** Displays the standard output separator. */
    public void showDivider() {
        System.out.println(DIVIDER);
    }
}
