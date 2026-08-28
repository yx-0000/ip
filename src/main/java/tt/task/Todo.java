package tt.task;

/** A task without a deadline or scheduled time. */
public class Todo extends Task {

    /** Creates an incomplete todo task. */
    public Todo(String todo) {
        super(todo);
    }

    @Override
    /** Returns the todo with its type marker and completion state. */
    public String toString() {
        return "[T]" + super.toString();
    }
}
