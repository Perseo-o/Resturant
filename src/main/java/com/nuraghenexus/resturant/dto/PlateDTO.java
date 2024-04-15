package com.nuraghenexus.resturant.dto;

import com.nuraghenexus.resturant.model.enumerations.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlateDTO {

    private Long id;
    private String name;
    private List<IngredientDTO> ingredientDTOList;
}