package tt.task;

/** A task without a deadline or scheduled time. */
public class Todo extends Task {

    /** Creates an incomplete todo task. */
    public Todo(String todo) {
        super(todo);
    }

    /** Returns the todo with its type marker and completion state. */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
