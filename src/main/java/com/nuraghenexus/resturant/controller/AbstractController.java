package com.nuraghenexus.resturant.controller;

import com.nuraghenexus.resturant.constants.API;
import com.nuraghenexus.resturant.util.ResponseUtilController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.nuraghenexus.resturant.service.ServiceDTO;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractController<DTO> {

	@Autowired
	private ServiceDTO<DTO> service;

	@GetMapping(API.GET_ALL)
	public ResponseEntity<Map<String, Object>> getAll() {
		return ResponseUtilController.handleGenericResponse(
				service.getAll(),
				API.GEN_FOUNDS);
	}

	@PostMapping(API.CREATE)
	public ResponseEntity<Map<String, Object>> create(@RequestBody DTO dto) {
		System.out.println(dto);
		return ResponseUtilController.handleGenericResponse(
				service.create(dto),
				API.GEN_CRE_SUCCESS);
	}

	@GetMapping(API.READ)
	public ResponseEntity<Map<String, Object>> read(@RequestParam(API.ID) Long id) {
		return ResponseUtilController.handleGenericResponse(
				service.read(id),
				API.GEN_FOUND);
	}

	@PutMapping(API.UPDATE)
	public ResponseEntity<Map<String, Object>> update(@RequestBody DTO dto) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put(API.GEN_MSG, service.update(dto));
		return ResponseUtilController.handleGenericResponse(
				map,
				API.GEN_UPD_SUCCESS);
	}

	@DeleteMapping(API.DELETE)
	public ResponseEntity<Map<String, Object>> delete(@RequestParam(API.ID) Long id) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put(API.GEN_MSG, service.delete(id));
		return ResponseUtilController.handleDeleteResponse(map);
	}
}
