public class Event extends Task{
    String from;
    String to;

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
