package com.nuraghenexus.resturant.controller;

import com.nuraghenexus.resturant.constants.API;
import com.nuraghenexus.resturant.constants.PROP;
import com.nuraghenexus.resturant.dto.IngredientDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ingredient")
@CrossOrigin(origins = PROP.CORS_ORIGIN_PROP)
public class IngredientController extends AbstractController<IngredientDTO>{

}