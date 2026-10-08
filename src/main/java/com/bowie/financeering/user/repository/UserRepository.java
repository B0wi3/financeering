package com.bowie.financeering.user.repository;

import com.bowie.financeering.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findById(String id);
    User save(User user);
    boolean existsById(String id);
    void deleteById(String id);
    List<User> findAll();
}
