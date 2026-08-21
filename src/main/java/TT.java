import java.util.Scanner;

public class TT {
    public static void main(String[] args) {

        String divider = "________________________________________ \n";
        String banner = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";

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

        while(true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                System.out.println(divider + bye);
                System.out.println(divider);
                break;

            } else if (input.equals("list")) {
                System.out.println(divider + "Here are the tasks in your list: ");

                for (int i = 0; i < taskCount; i++) {
                    System.out.println( (i + 1) + "." + tasks[i].toString());
                }

                System.out.println(divider);

            } else if (input.startsWith("mark ")) {
                int index = Integer.parseInt(input.substring(5)) - 1;
                tasks[index].mark();
                System.out.println(divider + "Nice! I've marked this task as done: \n" + tasks[index].toString());
                System.out.println(divider);

            } else if (input.startsWith("unmark ")) {
                int index = Integer.parseInt(input.substring(7)) - 1;
                tasks[index].unmark();
                System.out.println(divider + "OK, I've marked this task as not done yet: \n" + tasks[index].toString());
                System.out.println(divider);

            } else {
                if(input.startsWith("todo ")) {
                    String description = input.substring(4).trim();

                    if (description.isEmpty()) {
                        throw new IllegalArgumentException("Todo description cannot be empty");

                    } else {
                        Todo todo = new Todo(input.substring(5));
                        tasks[taskCount] = todo;
                        taskCount++;
                        System.out.println(divider
                                + addtask
                                + "  "
                                + todo.toString()
                                + "\n"
                                + "Now you have " + taskCount + " tasks in the list. \n"
                                + divider);
                    }

                } else if (input.startsWith("deadline ")) {
                    int index = input.indexOf("/");
                    String name = input.substring(8, input.indexOf("/")).trim();
                    String by = input.substring(index + 3).trim();
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

                } else if (input.startsWith("event ")) {
                    int firstSlash = input.indexOf("/");
                    int secondSlash = input.indexOf("/", firstSlash + 1);

                    String eventName = input.substring(5, firstSlash).trim();
                    String from = input.substring(firstSlash + 5, secondSlash - 1).trim();
                    String to = input.substring(secondSlash + 3).trim();
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
                    tasks[taskCount] = new Task(input);
                    taskCount++;
                    System.out.println(divider + "added : " + input + "\n" + divider);
                }
            }

        }

        scanner.close();
    }
}
