package tt.task;

/** The base representation of a task and its completion state. */
public class Task {
    private String name;
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

    /** Returns {@code X} when complete, otherwise a blank marker. */
    public String isDone() {
        return isDone ? "X" : " ";
    }

    /** Returns whether the task is complete. */
    public boolean isDoneValue() {
        return isDone;
    }

    /** Marks the task as complete. */
    public void mark() {
        this.isDone = true;
    }

    /** Marks the task as incomplete. */
    public void unmark() {
        this.isDone = false;
    }

    @Override
    /** Returns the task in the format displayed by the user interface. */
    public String toString() {
        return "[" + isDone() + "] " + name;
    }
}
