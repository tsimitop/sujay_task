package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
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
        return tagRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Tag not found with id " + id));
    }

    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }

    public Tag updateTag(Long id, Tag updatedTag) {
        Tag existing = tagRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Tag not found with id " + id));

        existing.setTitle(updatedTag.getTitle());
        existing.setDescription((updatedTag.getDescription()));

        return tagRepository.save(existing);
    }

    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tag not found with id " + id);
        }
        tagRepository.deleteById(id);
    }
}
