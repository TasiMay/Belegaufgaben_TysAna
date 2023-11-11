package de.htwb.ai.repo;

import de.htwb.ai.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByUserIdAndPassword(String userId, String password);
}
