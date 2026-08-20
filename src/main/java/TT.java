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
                tasks[taskCount] = new Task(input);
                taskCount++;
                System.out.println(divider + "added : " + input + "\n"+ divider);
            }

        }

        scanner.close();
    }
}
