package com.nuraghenexus.resturant.controller;

import com.nuraghenexus.resturant.dto.PlateDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plate")
public class PlateController extends AbstractController<PlateDTO>{

}