package com.nuraghenexus.resturant.controller;

import com.nuraghenexus.resturant.constants.API;
import com.nuraghenexus.resturant.dto.*;
import com.nuraghenexus.resturant.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(API.USER_REQ_MAP)
public class UserController extends AbstractController<UserDTO>{

	@Autowired
	private UserService userService;


}
