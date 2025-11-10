package com.example.demo;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.Tag;
import com.example.demo.repository.TagRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.core.env.Environment;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TagControllerTests {
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TagRepository tagRepository;

    @Autowired
    private Environment env;

	@BeforeEach
	void setup() {
        System.out.println("Active profiles: " + String.join(",", env.getActiveProfiles()));
		tagRepository.deleteAll();
		tagRepository.saveAll(List.of(new Tag("java", "described"),
                                      new Tag("not java", "description refusal cause why not")));
	}

	@Test
	void testGetAllTags() throws Exception {
		mockMvc.perform(get("/api/tags")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].title").value("java"))
			.andExpect(jsonPath("$[1].title").value("not java"));
	}

    @Test
    void testGetInvalidPath() throws Exception {
        mockMvc.perform(get("/api/tag")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPostForSingleTag() throws Exception {
        String newTagJson = """
               {
                    "title": "New Tag",
                    "description": "I am a new Tag"
               }
               """;
        mockMvc.perform(post("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newTagJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Tag"))
                .andExpect(jsonPath("$.description").value("I am a new Tag"));

        List<Tag> allTags = tagRepository.findAll();
        assertTrue(allTags.stream().anyMatch(t -> t.getTitle().equals("New Tag") &&
                                                       t.getDescription().equals("I am a new Tag")));
    }

    @Test
    void testPostForInvalidPath() throws Exception {
        String newTagJson = """
               {
                    "title": "New Tag",
                    "description": "I am a new Tag"
               }
               """;
        mockMvc.perform(post("/api/tag")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newTagJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        List<Tag> allTags = tagRepository.findAll();
        assertTrue(allTags.stream().noneMatch(t ->
                t.getTitle().equals("New Tag") &&
                t.getDescription().equals("I am a new Tag")
        ), "Tag should not have been created for invalid path. Assuming no same Tags are allowed, not implemented yet");
    }

    @Test
    void testPostWithInvalidBody() throws Exception {
        String invalidTagJson = """
                {
                    "description": "I can't have a name!"
                }
                """;
        mockMvc.perform(post("/api/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidTagJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        List<Tag> allTags = tagRepository.findAll();
        assertTrue(allTags.stream().noneMatch(t ->
                "I can't have a name!".equals(t.getTitle())
        ), "Tag with invalid body should not be created");
    }

    @Test
    void testPostWithBodyContainingNameInsteadOfTitle() throws Exception {
        String invalidTagJson = """
                {
                    "name": "Oops!",
                    "description": "I can't have a name so my title is null!"
                }
                """;
        mockMvc.perform(post("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidTagJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        List<Tag> allTags = tagRepository.findAll();
        assertTrue(allTags.stream().noneMatch(t ->
                "I can't have a name so my title is null!".equals(t.getDescription())
        ), "I can't have a name so my title is null, I am invalid.");
    }

    @Test
    void testValidPutForExistingTag() throws Exception {
        Tag existingTag = tagRepository.save(new Tag("Title!", "Description!"));

        String updatedJson = """
                {
                    "title": "Updated title!",
                    "description": "Updated description!"
                }
                """;

        mockMvc.perform(put("/api/tags/{id}", existingTag.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated title!"))
                .andExpect(jsonPath("$.description").value("Updated description!"));

        Tag updatedTag = tagRepository.findById(existingTag.getId()).orElseThrow();
        assertEquals("Updated title!", updatedTag.getTitle());
        assertEquals("Updated description!", updatedTag.getDescription());
    }

    @Test
    void testPutWithInvalidBody() throws Exception {
        Tag existingTag = tagRepository.save(new Tag("Java", "Programming language"));

        String invalidJson = """
        {
            "description": "No title here"
        }
        """;

        mockMvc.perform(put("/api/tags/{id}", existingTag.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Tag unchangedTag = tagRepository.findById(existingTag.getId()).orElseThrow();
        assertEquals("Java", unchangedTag.getTitle());
        assertEquals("Programming language", unchangedTag.getDescription());
    }

    @Test
    void testPutForNonExistingTag() throws Exception {
        Long nonExistingId = 999L;

        String updatedTagJson = """
                {
                    "title": "Non-existent Tag",
                    "description": "I am not in the database"
                }
                """;

        Long sizeBeforeRequest = tagRepository.count();

        mockMvc.perform(put("/api/tags/{id}", nonExistingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedTagJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        Long sizeAfterRequest = tagRepository.count();

        assertEquals(sizeBeforeRequest, sizeAfterRequest, "Repository should not have new tags after PUT to non-existing ID");
    }

    @Test
    void testDeleteExistingTag() throws Exception {
        Tag tag = tagRepository.save(new Tag("Titleee", "Descriptionnnn"));

        mockMvc.perform(delete("/api/tags/{id}", tag.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertTrue(tagRepository.findById(tag.getId()).isEmpty(), "Tag should have been deleted");
    }

    @Test
    void testDeleteNonExistingTag() throws Exception {
        Long nonExistingId = 999L;

        long beforeCount = tagRepository.count();

        mockMvc.perform(delete("/api/tags/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        long afterCount = tagRepository.count();

        assertEquals(beforeCount, afterCount, "Repository should not be modified when deleting non-existing tag");
    }

}
