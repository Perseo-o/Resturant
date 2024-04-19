package com.nuraghenexus.resturant.controller;

import com.nuraghenexus.resturant.constants.API;
import com.nuraghenexus.resturant.util.ResponseUtilController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.nuraghenexus.resturant.service.ServiceDTO;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public abstract class AbstractController<DTO> {

	@Autowired
	private ServiceDTO<DTO> service;


	@GetMapping("/getAll")
	@PreAuthorize("hasRole('USER')")
	public List<DTO> getAll() {
		return service.getAll();
	}


	@PostMapping("/create")
	@PreAuthorize("hasRole('USER')")
	public DTO create(@RequestBody DTO dto) {return service.create(dto); }

	@GetMapping("/read")
	@PreAuthorize("hasRole('USER')")
	public DTO read(@RequestParam("id") Long id) {
		return service.read(id);
	}

	@PutMapping("/update")
	@PreAuthorize("hasRole('USER')")
	public DTO update(@RequestBody DTO dto) {
		return service.update(dto);
	}


	@DeleteMapping("/delete")
	public boolean delete(@RequestParam("id") Long id) {
		return service.delete(id);
	}
}
