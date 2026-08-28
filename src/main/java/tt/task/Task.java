package tt.task;

public class Task {
    private String name;
    private boolean isDone;

    public Task(String name) {
        this.name = name;
        isDone = false;
    }

    public String getTask() {
        return name;
    }

    public String isDone() {
        return isDone ? "X" : " ";
    }

    public boolean isDoneValue() {
        return isDone;
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        return "[" + isDone() + "] " + name;
    }
}
