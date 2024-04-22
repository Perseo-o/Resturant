package com.nuraghenexus.resturant.service;

import com.nuraghenexus.resturant.constants.API;
import com.nuraghenexus.resturant.dto.IngredientDTO;
import com.nuraghenexus.resturant.model.Ingredient;
import com.nuraghenexus.resturant.repository.IngredientRepository;
import com.nuraghenexus.resturant.repository.PlateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IngredientService extends AbstractService<Ingredient, IngredientDTO>{

    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private PlateRepository plateRepository;

    @Override
    @Transactional
    public boolean delete(Long ingredId) {
        try {
            Ingredient ingredient = ingredientRepository.findById(ingredId).orElseThrow(() -> new UsernameNotFoundException("Ingredient not found"));
            if (plateRepository.findByIngredientList(ingredient).isPresent()){
                    plateRepository.deleteByIngredientList(ingredient);
            }
            ingredientRepository.deleteById(ingredId);
            return true;
        } catch (Exception e) {
            System.out.println(API.AUTH_ERROR_DELETE + e.getMessage());
            throw new RuntimeException(API.AUTH_ERROR_DELETE, e);
        }

    }
}
