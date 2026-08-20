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

        Scanner scanner = new Scanner(System.in);

        while(true) {
            String input = scanner.nextLine();

            if(input.equals("bye")) {
                System.out.println(divider + bye);
                System.out.println(divider);
                break;
            }

            if (input.equals("list")) {
                System.out.println(divider + "list");
                System.out.println(divider);
                break;
            }

            System.out.println(input);
            System.out.println(divider);
        }

        scanner.close();
    }
}
