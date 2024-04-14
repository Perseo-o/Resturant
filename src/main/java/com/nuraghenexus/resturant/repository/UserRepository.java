package com.nuraghenexus.resturant.repository;

import com.nuraghenexus.resturant.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);
	Optional<User> findByUsername(String username);

}
