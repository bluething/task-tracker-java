package io.github.bluething.playground.java;

import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Task {
    private final int id;
    private String description;
    private Status status;
    private final Instant createdAt;
    private Instant updatedAt;

    public Task(int id, String description, Status status, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Task(int id, String description, Status status) {
        this(id, description, status, Instant.now(), Instant.now());
    }

    private static String unescapeJson(String s) {
        return s.replace("\\\"", "\"").replace("\\\\", "\\");
    }
    private String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
    public String toJson() {
        return String.format("{\"id\":%d,\"description\":\"%s\",\"status\":\"%s\",\"createdAt\":\"%s\",\"updatedAt\":\"%s\"}",
                id, escapeJson(description), status.name(), createdAt, updatedAt);
    }
    public static Task fromJson(String json) {
        Pattern idPat = Pattern.compile("\\\"id\\\"\\s*:\\s*(\\d+)");
        Pattern descPat = Pattern.compile("\\\"description\\\"\\s*:\\s*\\\"(.*?)\\\"");
        Pattern statusPat = Pattern.compile("\\\"status\\\"\\s*:\\s*\\\"(TODO|IN_PROGRESS|DONE)\\\"");
        Pattern createdAtPat = Pattern.compile("\\\"createdAt\\\"\\s*:\\s*\\\"(.*?)\\\"");
        Pattern updatedAtPat = Pattern.compile("\\\"updatedAt\\\"\\s*:\\s*\\\"(.*?)\\\"");
        Matcher m;

        m = idPat.matcher(json);
        int id = m.find() ? Integer.parseInt(m.group(1)) : -1;

        m = descPat.matcher(json);
        String desc = m.find() ? unescapeJson(m.group(1)) : "";

        m = statusPat.matcher(json);
        Status status = m.find() ? Status.valueOf(m.group(1)) : Status.TODO;

        Instant createdAt = Instant.now();
        m = createdAtPat.matcher(json);
        if (m.find()) {
            try {
                createdAt = Instant.parse(m.group(1));
            } catch (Exception e) {
                System.err.println("Error when parsing created at: " + json + ", using current value");
                createdAt = Instant.now();
            }
        }

        Instant updatedAt = createdAt;
        m = updatedAtPat.matcher(json);
        if (m.find()) {
            try {
                updatedAt = Instant.parse(m.group(1));
            } catch (Exception e) {
                System.err.println("Error when parsing updated at: " + json);
            }
        }

        return new Task(id, desc, status, createdAt, updatedAt);
    }

    public void setStatus(Status status) {
        this.status = status;
        this.updatedAt = Instant.now();
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = Instant.now();
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
