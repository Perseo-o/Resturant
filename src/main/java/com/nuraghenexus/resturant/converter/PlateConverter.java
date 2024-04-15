package com.nuraghenexus.resturant.converter;

import com.nuraghenexus.resturant.dto.IngredientDTO;
import com.nuraghenexus.resturant.dto.PlateDTO;
import com.nuraghenexus.resturant.model.Ingredient;
import com.nuraghenexus.resturant.model.Plate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlateConverter extends AbstractConverter<Plate, PlateDTO> {

    @Autowired
    private IngredientConverter ingredientConverter;

    @Override
    public Plate toEntity(PlateDTO plateDTO) {
        Plate plate = null;
        if (plateDTO != null) {
            plate = new Plate(
                    plateDTO.getId(),
                    plateDTO.getName(),
                    ingredientConverter.toEntityList(plateDTO.getIngredientDTOList()));
        }
        return plate;
    }

    @Override
    public PlateDTO toDTO(Plate plate) {
        PlateDTO plateDTO = null;
        if (plate != null) {
            plateDTO = new PlateDTO(
                    plate.getId(),
                    plate.getName(),
                    ingredientConverter.toDTOList(plate.getIngredientList()));
        }
        return plateDTO;
    }

}