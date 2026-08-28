package tt;

/** Represents the commands understood by the task manager. */
public enum Command {
    BYE, LIST, FIND, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, UNKNOWN;

    /** Converts user input into a command, or {@link #UNKNOWN} if it is unsupported. */
    public static Command fromString(String commandWord) {
        try {
            return Command.valueOf(commandWord.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
