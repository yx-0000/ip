package tt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import tt.storage.TaskStorage;

class TTTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void clearTasks_validTaskTypes_removesOnlyMatchingTasks() {
        TaskStorage storage = new TaskStorage(temporaryDirectory.resolve("tasks.txt"));
        TT taskManager = new TT(storage);
        taskManager.getResponse("todo read book");
        taskManager.getResponse("todo buy milk");
        taskManager.getResponse("deadline submit report /by 2026-09-10");
        taskManager.getResponse("event meeting /from 2pm /to 3pm");

        assertEquals("Cleared 2 todo tasks.", taskManager.getResponse("clear todo"));
        String remainingTasks = taskManager.getResponse("list");
        assertFalse(remainingTasks.contains("read book"));
        assertFalse(remainingTasks.contains("buy milk"));
        assertTrue(remainingTasks.contains("submit report"));
        assertTrue(remainingTasks.contains("meeting"));

        assertEquals("Cleared 1 deadline task.", taskManager.getResponse("clear deadlines"));
        assertEquals("Cleared 1 event task.", taskManager.getResponse("clear event"));
        assertEquals(0, storage.load().size());
    }

    @Test
    void clearTasks_all_removesEveryTask() {
        TaskStorage storage = new TaskStorage(temporaryDirectory.resolve("tasks.txt"));
        TT taskManager = new TT(storage);
        taskManager.getResponse("todo read book");
        taskManager.getResponse("event meeting /from 2pm /to 3pm");

        assertEquals("Cleared 2 tasks.", taskManager.getResponse("clear all"));
        assertTrue(taskManager.getResponse("list").contains("There are no tasks to show."));
    }
}
