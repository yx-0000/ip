package tt;

import java.util.Locale;

/** Represents the commands understood by the task manager. */
public enum Command {
    BYE, LIST, FIND, SORT, MARK, UNMARK, DELETE, CLEAR, TODO, DEADLINE, EVENT, UNKNOWN;

    /** Converts user input into a command, or {@link #UNKNOWN} if it is unsupported. */
    public static Command fromString(String commandWord) {
        try {
            return Command.valueOf(commandWord.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
