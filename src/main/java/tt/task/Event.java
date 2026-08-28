package tt.task;

/** A task associated with a starting and ending time description. */
public class Event extends Task {
    private String from;
    private String to;

    /** Creates an incomplete event with its time range. */
    public Event(String event, String from, String to) {
        super(event);
        this.from = from;
        this.to = to;
    }

    /** Returns the event's starting time description. */
    public String getFrom() { return from; }
    /** Returns the event's ending time description. */
    public String getTo() { return to; }

    @Override
    /** Returns the event with its type marker, status, and time range. */
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
