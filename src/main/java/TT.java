import java.util.Scanner;

public class TT {
    public static void main(String[] args) {
        String line = "________________________________________ \n";
        String banner = line
                + " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";

        String intro = "Hello! I'm TT.\n" + "What can I do for you? \n" + line;
        String bye = "Bye. Hope to see you again soon!\n" + line;

        System.out.println(banner);
        System.out.println(intro + bye);
        //System.out.println(bye);
    }
}
