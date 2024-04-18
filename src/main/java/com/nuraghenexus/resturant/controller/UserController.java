package com.nuraghenexus.resturant.controller;

import com.nuraghenexus.resturant.constants.API;
import com.nuraghenexus.resturant.constants.PROP;
import com.nuraghenexus.resturant.dto.*;
import com.nuraghenexus.resturant.service.*;
import com.nuraghenexus.resturant.util.ResponseUtilController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping(API.USER_REQ_MAP)
public class UserController extends AbstractController<UserDTO>{

	@Autowired
	private UserService userService;

	@GetMapping(API.USER_REQ_USER_BY_E_U)
	public ResponseEntity<Map<String, Object>> getUserByEmail(@RequestParam String emailOrUsername){
		return ResponseUtilController.handleGenericResponse(
				userService.getUserByEmailOrUsername(emailOrUsername),
				API.GEN_FOUND
		);
	}

}
