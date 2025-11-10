package com.example.demo.controller;

import com.example.api.model.TagModel; // <-- API model
import com.example.demo.service.TagService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@io.swagger.v3.oas.annotations.tags.Tag(
        name = "Tag API",
        description = "CRUD operations for Tag entities"
)
@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<TagModel> getAllTags() {
        return tagService.getAllTags();
    }

    @GetMapping("/{id}")
    public TagModel getTagById(@PathVariable Long id) {
        return tagService.getTagById(id);
    }

    @PostMapping
    public TagModel createTag(@Valid @RequestBody TagModel tagModel) {
        return tagService.createTag(tagModel);
    }

    @PutMapping("/{id}")
    public TagModel updateTag(@PathVariable Long id, @Valid @RequestBody TagModel tagModel) {
        return tagService.updateTag(id, tagModel);
    }

    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
    }
}
