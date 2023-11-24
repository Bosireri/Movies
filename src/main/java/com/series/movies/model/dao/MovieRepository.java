package com.series.movies.model.dao;

import com.series.movies.model.Movies;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movies, Integer> {
    boolean findByTitleIgnoreCase(String title);
    boolean existsByTitle(String title);

    Optional<Object> findByTitle(String title);
}
