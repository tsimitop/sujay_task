package com.example.demo.service;

import com.example.demo.model.Tag;
import com.example.demo.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getAllTags() {
        return  tagRepository.findAll();
    }

    public Tag getTagById(Long id) {
        return tagRepository.findById(id).orElse(null);
    }

    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }

    public Tag updateTag(Long id, Tag updatedTag) {
        Tag existing = tagRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setTitle(updatedTag.getTitle());
            existing.setDescription((updatedTag.getDescription()));
            return tagRepository.save(existing);
        }
        return null;
    }

    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }
}
