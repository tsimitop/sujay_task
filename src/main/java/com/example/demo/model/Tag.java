package com.example.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table(name = "tags")
@Schema(description = "Represents a tag in the system.")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier", example = "1")
    private Long id;

    @NotBlank(message = "Title is mandatory")
    @Schema(description = "Tag title", example = "Important")
    private  String title;

    @NotBlank(message = "Description is mandatory")
    @Schema(description = "Tag description", example = "Used to mark high-priority tasks")
    private  String description;

    @Column(updatable = false)
    @Schema(description = "Creation timestamp", example = "2025-11-04T09:15:00")
    private LocalDateTime createdAt;
    @Column(updatable = true)
    @Schema(description = "Last update timestamp", example = "2025-11-04T10:45:30")
    private LocalDateTime updatedAt;

    public Tag() {}

    public Tag(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected  void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
