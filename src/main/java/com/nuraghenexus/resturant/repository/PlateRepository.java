package com.nuraghenexus.resturant.repository;

import com.nuraghenexus.resturant.model.Plate;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface PlateRepository extends JpaRepository<Plate, Long> {
}
