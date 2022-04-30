package com.example.petstore.controller;

import com.example.petstore.exception.DataNotFoundException;
import com.example.petstore.exception.InvalidIdException;
import com.example.petstore.model.Pet;
import com.example.petstore.service.PetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "Pet Rest Controller", tags = "/pets")
@RestController
@RequiredArgsConstructor
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;


    @ApiOperation(value = "Add a new pet to the store", response = Pet.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
    })
    @Validated
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public void addPet(@RequestBody @Valid Pet pet) {
        petService.addPet(pet);
    }

    @ApiOperation(value = "Update an existing pet", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Invalid status value"),
            @ApiResponse(code = 404, message = "Pet not found!")
    })
    @PutMapping(consumes = APPLICATION_JSON_VALUE)
    @Validated
    public void updatePet(@RequestBody @NotNull final Pet pet) {
        if (pet.getId() == null) {
            throw new InvalidIdException("Invalid ID supplied");
        }

        Pet existingPet = petService.getPetById(pet.getId());

        if (existingPet == null) {
            throw new DataNotFoundException("Pet not found");
        }

        petService.addPet(pet);
    }

    @ApiOperation(value = "Finds Pets by status", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Invalid status value"),
    })
    @GetMapping(value = "/findByStatus")
    public List<Pet> findPetsByStatus(@RequestParam("status") String status) {
        if (status == null) {
            throw new DataNotFoundException("Invalid status value");
        }

        List<String> statusList = List.of("available", "pending", "sold");
        final List<String> statues = Arrays.stream(status.split(",")).toList();

        statues.forEach(s -> {
            if (!statusList.contains(s)) {
                throw new InvalidIdException("Invalid status value");
            }
        });

        return petService.findPetByStatus(status);
    }


    @ApiOperation(value = "Find Pet by id ", response = Pet.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "Pet not found!")})
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Pet getPetById(@PathVariable Long id) {

        if (id == null) {
            throw new InvalidIdException("Invalid ID supplied");
        }

        Pet pet = petService.getPetById(id);
        if (pet == null) {
            throw new DataNotFoundException("Pet not found");
        }

        return pet;
    }


    @ApiOperation(value = "Updates a pet in the store with form data", response = Pet.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 405, message = "Invalid input")})
    @Validated
    @PostMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public void addPetById(@PathVariable @NotNull Long id, @RequestBody @Valid Pet pet) {

        pet.setId(id);
        petService.addPet(pet);
    }


    @ApiOperation(value = "Deletes a pet", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Invalid status value"),
            @ApiResponse(code = 404, message = "Pet not found!")
    })
    @DeleteMapping(value = "{id}")
    public void deletePet(@PathVariable @NotNull Long id) {

        Pet pet = petService.getPetById(id);
        if (pet == null) {
            throw new DataNotFoundException("Pet not found");
        }

        petService.deletePetById(id);
    }


    @ApiOperation(value = "Uploads an image", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
    })
    @Validated
    @PostMapping(value = "/{id}/uploadImage")
    public void uploadFile(@PathVariable @NotNull Long id, @RequestParam("file") @NotNull MultipartFile file) {
        Pet existingPet = petService.getPetById(id);
        existingPet.getPhotoUrls().add(file.getOriginalFilename());

        petService.addPet(existingPet);
    }
}

