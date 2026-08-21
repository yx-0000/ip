public class Todo extends Task{
    private String todo;

    public Todo(String todo) {
        super(todo);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
