package com.nuraghenexus.resturant.controller;

import com.nuraghenexus.resturant.constants.PROP;
import com.nuraghenexus.resturant.dto.PlateDTO;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plate")
@CrossOrigin(origins = PROP.CORS_ORIGIN_PROP)
public class PlateController extends AbstractController<PlateDTO>{

}