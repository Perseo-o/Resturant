package com.nuraghenexus.resturant.repository;

import com.nuraghenexus.resturant.model.Ingredient;
import com.nuraghenexus.resturant.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    Optional<Ingredient> findByUserId(Long id);

    void deleteByUser(User user);
}
