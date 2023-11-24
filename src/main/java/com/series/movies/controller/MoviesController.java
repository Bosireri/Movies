package com.series.movies.controller;

import com.series.movies.dto.MoviesBody;
import com.series.movies.dto.SuccessAndMessage;
import com.series.movies.dto.UniversalResponse;
import com.series.movies.exception.MovieAlreadyExistsException;
import com.series.movies.exception.UserAlreadyExistsException;
import com.series.movies.model.Movies;
import com.series.movies.model.dao.MovieRepository;
import com.series.movies.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/movie")
public class MoviesController {

    @Autowired
    private MovieRepository movieRepository;
    private SuccessAndMessage successAndMessage;
    @Autowired
    MovieService movieService;

    @PostMapping("/registerMovie")
    public ResponseEntity<SuccessAndMessage> registerMovie(@Valid @RequestBody MoviesBody moviesBody) {
        try {
            SuccessAndMessage response = new SuccessAndMessage();
            movieService.registerMovie(moviesBody);
            response.setMessage("Movie Registered Successfully");
            response.setSuccess(true);
            return new ResponseEntity<SuccessAndMessage>(response, HttpStatus.OK);
        } catch (MovieAlreadyExistsException | UserAlreadyExistsException e) {
            SuccessAndMessage response = new SuccessAndMessage();
            response.setMessage("Movie Already Registered");
            response.setSuccess(false);
            return new ResponseEntity<SuccessAndMessage>(response, HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/updateMovie/{id}")
    public ResponseEntity<SuccessAndMessage> updateMovie (@PathVariable Integer id,
                                                         @Valid @RequestBody MoviesBody moviesBody,
                                                         @RequestHeader(name="Authorization") String jwt) {
        System.out.println("movieUpdate");
        return movieService.updateMovie(id, moviesBody);
    }

    @GetMapping("/getMovie/{id}")
    public UniversalResponse getMovieById(@PathVariable("id") Integer id) {
        Movies movie =movieRepository.findById((id)).orElse(null);
        if (movie==null)
            return UniversalResponse.builder()
                    .message("Movie not found")
                    .status(404)
                    .data(null)
                    .errors(null)
                    .build();
        return UniversalResponse.builder()
                .message("Movie retrieved")
                .status(200)
                .data(movie)
                .build();
    }

    @DeleteMapping("/deleteMovie/{id}")
    public ResponseEntity<SuccessAndMessage> deleteMovie(@PathVariable Integer id) {
        Optional<Movies> optionalSpot = movieRepository.findById(id);
        SuccessAndMessage response = new SuccessAndMessage();
        if (optionalSpot.isPresent()) {
            movieRepository.deleteById(id);
            response.setSuccess(true);
            response.setMessage("Successfully deleted movie of ID: " + id);
            return ResponseEntity.ok(response);
        } else {
            response.setSuccess(false);
            response.setMessage("movie with ID: " + id + " not found.");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getAllMovies")
    public List<Movies> getAllAvailableSpots() {
        return movieRepository.findAll();
    }

}
