package tt.task;

/** The base representation of a task and its completion state. */
public class Task {
    private final String name;
    private boolean isDone;

    /** Creates an incomplete task with the supplied description. */
    public Task(String name) {
        this.name = name;
        isDone = false;
    }

    /** Returns the task description. */
    public String getTask() {
        return name;
    }

    /** Returns {@code X} when complete, otherwise a blank status marker. */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /** Returns whether the task is complete. */
    public boolean isDone() {
        return isDone;
    }

    /** Marks the task as complete. */
    public void mark() {
        isDone = true;
    }

    /** Marks the task as incomplete. */
    public void unmark() {
        isDone = false;
    }

    /** Returns the task in the format displayed by the user interface. */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + name;
    }
}
