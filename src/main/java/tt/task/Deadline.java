package tt.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** A task that must be completed by a particular calendar date. */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);
    private final LocalDate by;

    public Deadline(String name, LocalDate by) {
        super(name);
        this.by = by;
    }

    /** Creates a deadline from an ISO date, retained for convenient text-based callers. */
    public Deadline(String name, String by) {
        this(name, LocalDate.parse(by.trim()));
    }

    /** Returns the deadline as a date suitable for comparisons and calculations. */
    public LocalDate getBy() { return by; }

    /** Returns the date in the format shown to the user. */
    public String getFormattedBy() { return by.format(DISPLAY_FORMAT); }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + getFormattedBy() + ")";
    }
}
