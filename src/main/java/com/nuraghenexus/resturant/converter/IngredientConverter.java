package com.nuraghenexus.resturant.converter;

import com.nuraghenexus.resturant.dto.IngredientDTO;
import com.nuraghenexus.resturant.model.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IngredientConverter extends AbstractConverter<Ingredient, IngredientDTO> {

    @Autowired
    private UserConverter userConverter;

    @Override
    public Ingredient toEntity(IngredientDTO ingredientDTO) {
        Ingredient ingredient = null;
        if (ingredientDTO != null) {
            ingredient = new Ingredient(
                    ingredientDTO.getId(),
                    ingredientDTO.getName(),
                    ingredientDTO.getDescription(),
                    userConverter.toEntity(ingredientDTO.getUserDTO()));
        }
        return ingredient;
    }

    @Override
    public IngredientDTO toDTO(Ingredient ingredient) {
        IngredientDTO ingredientDTO = null;
        if (ingredient != null) {
            ingredientDTO = new IngredientDTO(
                    ingredient.getId(),
                    ingredient.getName(),
                    ingredient.getDescription(),
                    userConverter.toDTO(ingredient.getUser()));
        }
        return ingredientDTO;
    }

}
