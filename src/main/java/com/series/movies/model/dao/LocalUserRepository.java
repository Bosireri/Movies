package com.series.movies.model.dao;

import com.series.movies.model.LocalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocalUserRepository extends JpaRepository<LocalUser, Integer> {

    Optional<LocalUser> findByEmail(String email);
    boolean existsByEmail(String email);
}
