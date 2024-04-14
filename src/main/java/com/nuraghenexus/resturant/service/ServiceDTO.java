package com.nuraghenexus.resturant.service;

import java.util.Map;

public interface ServiceDTO<DTO> {

	public Map<String, Object>  getAll();

	public Map<String, Object> create (DTO dto);

	public Map<String, Object> read(Long id);

	public String update (DTO dto);

	public String delete (Long id);
}