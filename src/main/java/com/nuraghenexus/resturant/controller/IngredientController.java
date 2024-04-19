package com.nuraghenexus.resturant.controller;

import com.nuraghenexus.resturant.dto.IngredientDTO;
import com.nuraghenexus.resturant.dto.PlateDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ingredient")
public class IngredientController  extends AbstractController<IngredientDTO>{

}