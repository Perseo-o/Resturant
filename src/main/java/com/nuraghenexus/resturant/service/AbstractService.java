package com.nuraghenexus.resturant.service;

import com.nuraghenexus.resturant.constants.API;
import com.nuraghenexus.resturant.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
public abstract class AbstractService<Entity, DTO> implements ServiceDTO<DTO> {

	@Autowired
	protected JpaRepository<Entity, Long> repository;

	@Autowired
	protected Converter<Entity, DTO> converter;

	@Override
	public List<DTO> getAll() {
		return converter.toDTOList(repository.findAll());
	}

	@Override
	public DTO create(DTO dto) {
		return converter.toDTO(repository.save(converter.toEntity(dto)));
	}

	@Override
	public DTO read(Long id) {
		return converter.toDTO(repository.getById(id));
	}

	@Override
	public DTO update(DTO dto) {
		return converter.toDTO(repository.save(converter.toEntity(dto)));
	}

	@Override
	public boolean delete(Long id) {

		if (repository.existsById(id)) {
			repository.deleteById(id);
			return true;
		} else {
			return false;
		}
	}

}