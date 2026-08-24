public class Event extends Task{
    private String from;
    private String to;

    public Event(String event, String from, String to) {
        super(event);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
