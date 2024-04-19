package com.nuraghenexus.resturant.service;

import java.util.List;
import java.util.Map;

public interface ServiceDTO<DTO> {

	List<DTO> getAll();

	DTO create (DTO dto);

	DTO read(Long id);

	DTO update (DTO dto);

	boolean delete (Long id);
}