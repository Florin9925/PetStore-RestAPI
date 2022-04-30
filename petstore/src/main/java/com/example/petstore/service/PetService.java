package com.example.petstore.service;

import com.example.petstore.model.Pet;
import com.example.petstore.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {
    private final PetRepository petRepository;

    PetService(PetRepository petRepository)
    {
        this.petRepository = petRepository;
    }

    public Pet getPetById(final long petId) {
        return petRepository.getPetById(petId);
    }

    public List<Pet> findPetByStatus(final String status) {
        return petRepository.findPetByStatus(status);
    }

    public void addPet(final Pet pet) {
        if(pet.getId() == null)
        {
            pet.setId(petRepository.autoIncrement());
        }
        petRepository.addPet(pet);
    }

    public void deletePetById(final Long petId) {
        petRepository.deletePetById(petId);
    }
}
