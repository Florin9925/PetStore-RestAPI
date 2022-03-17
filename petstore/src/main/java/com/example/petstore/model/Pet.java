package com.example.petstore.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Pet {

    private Long id;

    private Category category;

    @NotNull
    @ApiModelProperty(
            name = "name",
            dataType = "string",
            example = "doggie",
            required = true)
    private String name;

    @NotNull
    @ApiModelProperty(
            name = "photoUrls",
            dataType = "array",
            required = true
    )

    private List<String> photoUrls = new ArrayList<>();

    private List<Tag> tags = new ArrayList<>();

    private String status;
}
