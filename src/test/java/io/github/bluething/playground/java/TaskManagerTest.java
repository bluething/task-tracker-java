package io.github.bluething.playground.java;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {
    @TempDir
    Path tempDir;
    private TaskManager manager;
    private Path testFile;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() throws Exception {
        // Prepare a custom db.json in temp directory
        testFile = tempDir.resolve("db.json");
        // Instantiate manager
        manager = new TaskManager(testFile);

        // Capture stdout
        originalOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent, true, StandardCharsets.UTF_8));
    }
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void addTask_mustCreatesFileAndPrints() throws IOException {
        manager.addTask("New Task");
        assertTrue(Files.exists(testFile));

        String content = Files.readString(testFile, StandardCharsets.UTF_8);
        assertTrue(content.contains("New Task"));
        assertTrue(outContent.toString(StandardCharsets.UTF_8).contains("Added:"));
    }

    @Test
    void updateTask_mustModifiesFileAndPrints() throws Exception {
        // Add a task
        manager.addTask("Old");
        outContent.reset();
        manager.updateTask(1, "Updated");

        String content = Files.readString(testFile, StandardCharsets.UTF_8);
        assertTrue(content.contains("Updated"));
        assertTrue(outContent.toString(StandardCharsets.UTF_8).contains("Updated:"));
    }

    @Test
    void updateTask_mustChangesStatusInFile() throws IOException {
        manager.addTask("Task X");
        outContent.reset();
        manager.markTask(1, Status.DONE);

        String content = Files.readString(testFile, StandardCharsets.UTF_8);
        assertTrue(content.contains("\"status\":\"DONE\""));
        assertTrue(outContent.toString(StandardCharsets.UTF_8).contains("Marked:"));
    }

    @Test
    void deleteTask_mustRemovesFromFile() throws IOException {
        manager.addTask("ToDelete");
        outContent.reset();
        manager.deleteTask(1);

        String content = Files.readString(testFile, StandardCharsets.UTF_8);
        assertFalse(content.contains("ToDelete"));
        assertTrue(outContent.toString(StandardCharsets.UTF_8).contains("Deleted task id 1"));
    }

    @Test
    void listTasksWithFilters_returnTasksCorrectly() throws IOException {
        manager.addTask("A");
        manager.addTask("B");
        manager.markTask(2, Status.IN_PROGRESS);

        outContent.reset();
        manager.listTasks(Optional.of(Status.IN_PROGRESS));
        String out = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(
                out.trim().startsWith("[2] B"),
                "Expected line to start with “[2] B” but was:\n" + out
        );

        assertFalse(out.contains("A"));
    }
}