package tt.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskTest {

    @Test
    void newTask_isUnmarkedAndFormatsDescription() {
        Task task = new Task("read book");

        assertEquals("read book", task.getTask());
        assertEquals(" ", task.isDone());
        assertFalse(task.isDoneValue());
        assertEquals("[ ] read book", task.toString());
    }

    @Test
    void markAndUnmark_toggleCompletionState() {
        Task task = new Task("submit report");

        task.mark();
        assertTrue(task.isDoneValue());
        assertEquals("X", task.isDone());
        assertEquals("[X] submit report", task.toString());

        task.unmark();
        assertFalse(task.isDoneValue());
        assertEquals("[ ] submit report", task.toString());
    }
}
