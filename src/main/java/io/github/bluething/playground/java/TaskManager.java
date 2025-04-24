package io.github.bluething.playground.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class TaskManager {
    private final Path filePath;
    private final List<Task> tasks = new ArrayList<>();

    // Constructor for custom file path (e.g., tests)
    public TaskManager(Path filePath) {
        this.filePath = filePath;
        // Ensure file exists (create empty JSON array if missing)
        try {
            if (Files.notExists(filePath)) {
                Path parent = filePath.getParent();
                if (parent != null) Files.createDirectories(parent);
                Files.writeString(filePath, "[]");
            }
        } catch (IOException e) {
            System.err.println("Error initializing tasks file: " + e.getMessage());
        }
        loadTask();
    }

    public TaskManager() {
        this(Paths.get("db.json"));
    }

    private void loadTask() {
        if (Files.notExists(filePath)) {
            System.err.println("File not found: " + filePath.toAbsolutePath());
            return;
        }

        try {
            var content = Files.readString(filePath).trim();
            if (content.isEmpty() || "[]".equals(content)) {
                System.err.println("Empty file: " + filePath.toAbsolutePath());
                return;
            }
            content = content.substring(1, content.length() - 1);
            String[] parts = content.split("},\\s*\\{");
            for (String s : parts) {
                var part = s;
                if (!part.startsWith("{")) {
                    part = "{" + part;
                }
                if (!part.endsWith("}")) {
                    part = part + "}";
                }
                Task task = Task.fromJson(part);
                if (task.getId() >= 0) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveTasks() {
        String json = tasks.stream()
                .map(Task::toJson)
                .collect(Collectors.joining(",", "[", "]"));
        try {
            Files.writeString(filePath, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error writing tasks file: " + e.getMessage());
        }
    }
    private int nextId() {
        return tasks.stream().mapToInt(Task::getId).max().orElse(0) + 1;
    }
    public void addTask(String description) {
        Task task = new Task(nextId(), description, Status.TODO);
        tasks.add(task);
        saveTasks();
        System.out.println("Added: " + task);
    }

    public void updateTask(int id, String newDesc) {
        for (Task t : tasks) {
            if (t.getId() == id) {
                t.setDescription(newDesc);
                saveTasks();
                System.out.println("Updated: " + t);
                return;
            }
        }
        System.err.println("Task not found: " + id);
    }

    public void deleteTask(int id) {
        if (tasks.removeIf(t -> t.getId() == id)) {
            saveTasks();
            System.out.println("Deleted task id " + id);
        } else {
            System.err.println("Task not found: " + id);
        }
    }

    public void markTask(int id, Status status) {
        for (Task t : tasks) {
            if (t.getId() == id) {
                t.setStatus(status);
                saveTasks();
                System.out.println("Marked: " + t);
                return;
            }
        }
        System.err.println("Task not found: " + id);
    }

    public void listTasks(Optional<Status> filter) {
        tasks.stream()
                .filter(t -> filter.isEmpty() || t.getStatus() == filter.get())
                .forEach(System.out::println);
    }
}
