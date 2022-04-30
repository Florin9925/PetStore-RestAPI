package com.example.petstore.repository;

import com.example.petstore.model.Category;
import com.example.petstore.model.Pet;
import com.example.petstore.model.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Repository
@AllArgsConstructor
public class PetRepository {

    private static final HashMap<Long, Pet> pets = new HashMap<>();
    private static final HashMap<Long, Category> categories = new HashMap<>();

    static {
        categories.put(0L, createCategory(0, "Dogs"));
        categories.put(1L, createCategory(1, "Cats"));
        categories.put(2L, createCategory(2, "Rabbits"));
        categories.put(3L, createCategory(3, "Lions"));

        pets.put(1L, createPet(1, categories.get(1L), "Cat 1", new String[]{
                "url1", "url2"}, new String[]{"tag1", "tag2"}, "available"));
        pets.put(2L, createPet(2, categories.get(1L), "Cat 2", new String[]{
                "url1", "url2"}, new String[]{"tag2", "tag3"}, "available"));
        pets.put(3L, createPet(3, categories.get(1L), "Cat 3", new String[]{
                "url1", "url2"}, new String[]{"tag3", "tag4"}, "pending"));

        pets.put(4L, createPet(4, categories.get(0L), "Dog 1", new String[]{
                "url1", "url2"}, new String[]{"tag1", "tag2"}, "available"));
        pets.put(5L, createPet(5, categories.get(0L), "Dog 2", new String[]{
                "url1", "url2"}, new String[]{"tag2", "tag3"}, "sold"));
        pets.put(6L, createPet(6, categories.get(0L), "Dog 3", new String[]{
                "url1", "url2"}, new String[]{"tag3", "tag4"}, "pending"));

        pets.put(7L, createPet(7, categories.get(3L), "Lion 1", new String[]{
                "url1", "url2"}, new String[]{"tag1", "tag2"}, "available"));
        pets.put(8L, createPet(8, categories.get(3L), "Lion 2", new String[]{
                "url1", "url2"}, new String[]{"tag2", "tag3"}, "available"));
        pets.put(9L, createPet(9, categories.get(3L), "Lion 3", new String[]{
                "url1", "url2"}, new String[]{"tag3", "tag4"}, "available"));

        pets.put(10L, createPet(10, categories.get(2L), "Rabbit 1", new String[]{
                "url1", "url2"}, new String[]{"tag3", "tag4"}, "available"));
    }

    private static Pet createPet(final long id, final Category cat, final String name, final String[] urls,
                                 final String[] tags, final String status) {
        final Pet pet = new Pet();
        pet.setId(id);
        pet.setCategory(cat);
        pet.setName(name);
        if (null != urls) {
            final List<String> urlObjs = new ArrayList<>(Arrays.asList(urls));
            pet.setPhotoUrls(urlObjs);
        }
        final List<Tag> tagObjs = new ArrayList<>();
        int i = 0;
        if (null != tags) {
            for (final String tagString : tags) {
                i = i + 1;
                final Tag tag = new Tag();
                tag.setId(i);
                tag.setName(tagString);
                tagObjs.add(tag);
            }
        }
        pet.setTags(tagObjs);
        pet.setStatus(status);
        return pet;
    }

    private static Category createCategory(final long id, final String name) {
        final Category category = new Category();
        category.setId(id);
        category.setName(name);
        return category;
    }

    public Pet getPetById(final long petId) {
        return pets.get(petId);
    }

    public List<Pet> findPetByStatus(final String status) {
        final String[] statues = status.split(",");
        final List<Pet> result = new ArrayList<>();
        for (final Pet pet : pets.values()) {
            for (final String s : statues) {
                if (s.equals(pet.getStatus())) {
                    result.add(pet);
                }
            }
        }
        return result;
    }

    public void addPet(final Pet pet) {
        pets.put(pet.getId(), pet);
    }

    public void deletePetById(final Long petId) {
        pets.remove(petId);
    }

    public Long autoIncrement() {
        return pets.values().stream().map(Pet::getId).max(Long::compare).orElse(0L) + 1;
    }

}
