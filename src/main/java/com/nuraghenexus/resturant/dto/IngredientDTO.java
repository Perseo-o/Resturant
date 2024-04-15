package com.nuraghenexus.resturant.dto;

import com.nuraghenexus.resturant.model.enumerations.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientDTO {

    private Long id;
    private String name;
    private String description;
    private UserDTO userDTO;
}