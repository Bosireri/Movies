package com.series.movies.service;

import com.series.movies.dto.MoviesBody;
import com.series.movies.dto.RegistrationBody;
import com.series.movies.dto.SuccessAndMessage;
import com.series.movies.exception.MovieAlreadyExistsException;
import com.series.movies.exception.UserAlreadyExistsException;
import com.series.movies.model.LocalUser;
import com.series.movies.model.Movies;
import com.series.movies.model.dao.MovieRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public Movies registerMovie (MoviesBody moviesBody) throws UserAlreadyExistsException, MovieAlreadyExistsException {
        if (movieRepository.findByTitle(moviesBody.getTitle()).isPresent()) {
            throw new MovieAlreadyExistsException();
        }
        Movies movie = new Movies();
        movie.setTitle(moviesBody.getTitle());
        movie.setDescription(moviesBody.getDescription());
        movie.setGenres(moviesBody.getGenres());
        movie.setReleaseYear(moviesBody.getReleaseYear());
        movie.setCast(moviesBody.getCast());
        movie.setRating(moviesBody.getRating());
        movie = movieRepository.save(movie);
        return movieRepository.save(movie);
    }

    public ResponseEntity<SuccessAndMessage> updateMovie (Integer id, MoviesBody moviesBody){
        System.out.println("movieUpdate");
        SuccessAndMessage response = new SuccessAndMessage();
        if (!(movieRepository.existsById(id))) {
            response.setMessage("Movie does not exist");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        Optional<Movies> movies = movieRepository.findById(id);
        Movies movie = movies.get();
        movie.setTitle(moviesBody.getTitle());
        movie.setDescription(moviesBody.getDescription());
        movie.setCast(moviesBody.getCast());
        movie.setGenres(moviesBody.getGenres());
        movie.setReleaseYear(moviesBody.getReleaseYear());
        movie.setRating(moviesBody.getRating());
        movieRepository.save(movie);
        response.setMessage("Movie Updated Successfully");
        response.setSuccess(true);
        return new ResponseEntity<SuccessAndMessage>(response, HttpStatus.OK);
    }
}
