package io.github.bluething.playground.java;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void fromJson_returnSameValueAstoJson() {
        Task original = new Task(
                42,
                "Write unit tests",
                Status.IN_PROGRESS,
                Instant.parse("2025-04-22T00:00:00Z"),
                Instant.parse("2025-04-22T00:00:00Z")
        );
        String json = original.toJson();
        Task restored = Task.fromJson(json);

        assertEquals(original.getId(), restored.getId(), "ID should round-trip");
        assertEquals(original.getDescription(), restored.getDescription(), "Description should round-trip");
        assertEquals(original.getStatus(), restored.getStatus(), "Status should round-trip");
        assertEquals(original.getCreatedAt(), restored.getCreatedAt(), "createdAt should round-trip");
        assertEquals(original.getUpdatedAt(), restored.getUpdatedAt(), "updatedAt should round-trip");
    }

    @Test
    void setDescriptionUpdates_alsoUpdateUpdatedAtField() {
        Instant created = Instant.parse("2025-04-22T00:00:00Z");
        Instant updated = Instant.parse("2025-04-22T00:00:00Z");
        Task t = new Task(1, "Initial", Status.TODO, created, updated);

        // change description
        t.setDescription("Changed");

        assertEquals(created, t.getCreatedAt(), "createdAt should not change");
        assertTrue(t.getUpdatedAt().isAfter(updated), "updatedAt should be after initial updatedAt");
        assertEquals("Changed", t.getDescription(), "Description should be updated");
    }

    @Test
    void setStatusUpdates_alsoUpdateUpdatedAtField() {
        Instant created = Instant.parse("2025-04-22T00:00:00Z");
        Instant updated = Instant.parse("2025-04-22T00:00:00Z");
        Task t = new Task(2, "Desc", Status.TODO, created, updated);

        // change status
        t.setStatus(Status.DONE);

        assertEquals(created, t.getCreatedAt(), "createdAt should not change");
        assertTrue(t.getUpdatedAt().isAfter(updated), "updatedAt should be after initial updatedAt");
        assertEquals(Status.DONE, t.getStatus(), "Status should be updated");
    }
}