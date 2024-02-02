package it.ecubit.java.training.collections.test;

import it.ecubit.java.training.beans.Actor;
import it.ecubit.java.training.beans.Director;
import it.ecubit.java.training.beans.Movie;
import it.ecubit.java.training.loader.tmdb.ImdbLoader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class JavaCollectionsTest {

    public static void main(String[] args) {
        // Loading of all the top 1000 movies can take up to 10 minutes (needs to call the TMDB APIs for retrieving all the data)
        List<Movie> top1000Movies = ImdbLoader.loadMovies();
        System.out.println(top1000Movies.get(0).getActors().get(0).getImdb());
        System.out.println(top1000Movies.get(0).getActors().get(0).getId());

        // Exercise 1: Sort the movies by release year (from the most recent to the less recent)
        // and print the results with a counter before the movie info, one for each row
        // (i. e. '1) <MOVIE INFO>'\n'2) <MOVIE INFO>', ...)
        top1000Movies.sort(Comparator.comparing(Movie::getYear).reversed());
        System.out.println("\n**** SORTING IN BASE ALLA DATA ****");
        for (int i = 0; i < top1000Movies.size(); i++) {
            System.out.println(i + ") " + top1000Movies.get(i).getYear() + " " + top1000Movies.get(i));
        }

        // Exercise 2: Sort the movies lexicographically by title
        // and print the results with a counter before the movie info, one for each row
        // (i. e. '1) <MOVIE INFO>'\n'2) <MOVIE INFO>', ...)
        top1000Movies.sort(Comparator.comparing(Movie::getTitle));
        System.out.println("\n**** SORTING IN BASE AL TITOLO ****");
        for (int i = 0; i < top1000Movies.size(); i++) {
            System.out.println(i + ") " + top1000Movies.get(i));
        }

        // Exercise 3: How many movies has been directed by 'Peter Jackson'? Print all of them, one by line.
        System.out.println("\n**** FILM DI PETER JACKSON ****");
        List<Movie> filteredMovies = top1000Movies.stream()
                .filter(movie -> movie.getDirectors()
                        .stream().anyMatch(director -> director.getName()
                                .equalsIgnoreCase("Peter Jackson"))).toList();
        filteredMovies.forEach(movie -> System.out.println(movie.getYear() + " " + movie.getTitle()));

        // Exercise 4: How many movies did 'Orlando Bloom' star in as an actor? Print all of them, one by line.
        System.out.println("\n**** FILM DI ORLANDO BLOOM ****");
        List<Movie> orlandoBloomMovies = top1000Movies.stream().filter(movie -> movie.getActors()
                .stream().anyMatch(actor -> actor.getName().equalsIgnoreCase("Orlando Bloom"))
        ).toList();
        orlandoBloomMovies.forEach(movie -> System.out.println(movie.getYear() + " " + movie.getTitle()));

        // Exercise 5: Sort the movies by rating (ascending, from the less rated to the most rated)
        // and by movie title (lexicographically) as a secondary sort criterion
        // and print the results with a counter before the movie info, one for each row
        System.out.println("\n**** SORTING IN BASE A RATING E TITOLO ****");
        top1000Movies.sort(Comparator.comparing(Movie::getRating)
                .thenComparing(Comparator.comparing(Movie::getTitle)));
        for (int i = 0; i < top1000Movies.size(); i++) {
            System.out.println(i + ") " + "rating: " + top1000Movies.get(i).getRating() + " " + top1000Movies.get(i));
        }

        // Exercise 6: Sort the movies by duration (ascending, from the shortest to the longest oned)
        // and by release year (ascending, from the less recent to the most recent one) as a secondary sort criterion
        // and print the results with a counter before the movie info, one for each row
        System.out.println("\n**** SORTING IN BASE A DURATA E ANNO ****");
        top1000Movies.sort(Comparator.comparing(Movie::getDuration)
                .thenComparing(Comparator.comparing(Movie::getYear)));
        for (int i = 0; i < top1000Movies.size(); i++) {
            System.out.println(i + ") " + "durata: " + top1000Movies.get(i).getDuration() + " " + top1000Movies.get(i));
        }

        // Exercise 7: Group movies by actor, i.e. produce a map with actor name as key and a list of movies as values;
        // the list should contain the films in which the actor starred in (no duplicates)
        // and print the map with a counter before the map entry, one for each row
        System.out.println("\n**** GRUPPO DI FILM IN BASE ALL'ATTORE ****");
        HashMap<Actor, Set<Movie>> moviesByActor = new HashMap<>();
        Set<Actor> actors = new HashSet<>();
        top1000Movies.forEach(movie -> actors.addAll(movie.getActors()));

        // Per ogni attore creare un array di film
        actors.forEach(actor -> {
            Set<Movie> actorMovies = new HashSet<>();
            // Ciclare la lista dei film e se si trova il nome dell'attore, aggiungerlo all'array
            top1000Movies.forEach(movie -> {
                movie.getActors().forEach(movieActor -> {
                    if (movieActor.getName().equalsIgnoreCase(actor.getName())) {
                        actorMovies.add(movie);
                    }
                });
            });
            // Inserire chiave valore
            moviesByActor.put(actor, actorMovies);
        });

        moviesByActor.forEach((actor, movie) -> System.out.println("\n" + movie.stream().count() + " film di " + actor.getName() + ": " + movie));


        // Exercise 8: Group movies by director, i.e. produce a map with director name as key and a list of movies as values;
        // the list should contain the films in which the director took care of the direction (no duplicates)
        // and print the map with a counter before the map entry, one for each row
        System.out.println("\n**** GRUPPO DI FILM IN BASE AL REGISTA ****");
        HashMap<Director, Set<Movie>> moviesByDirector = new HashMap<>();
        Set<Director> directors = new HashSet<>();
        top1000Movies.forEach(movie -> directors.addAll(movie.getDirectors()));

        // Per ogni regista creare un array di film
        directors.forEach(director -> {
            Set<Movie> directorMovies = new HashSet<>();
            // Ciclare la lista dei film e se si trova il nome del regista, aggiungerlo all'array
            top1000Movies.forEach(movie -> {
                movie.getDirectors().forEach(movieDirector -> {
                    if (movieDirector.getName().equalsIgnoreCase(director.getName())) {
                        directorMovies.add(movie);
                    }
                });
            });
            // Inserire chiave valore
            moviesByDirector.put(director, directorMovies);
        });

        moviesByDirector.forEach((director, movie) -> System.out.println("\n" + movie.stream().count() + " film di " + director.getName() + ": " + movie));

        // Exercise 9: Add the film's box office total income to the movie loading process (field 'Gross' in the CSV)
        // and print the first 20 films who earned most money ever, one for each row, from the first to the 20th
        System.out.println("\n**** SORTING IN BASE A GUADAGNI ****");

        top1000Movies.sort(Comparator.comparing(Movie::getGross).reversed());
        top1000Movies.stream().limit(20).forEach(System.out::println);

        // Exercise 10: Add the number of votes received on the Social Media for each film (field 'No_of_Votes' in the CSV)
        // and print the first 20 films who received most votes, one for each row, from the first to the 20th
        System.out.println("\n**** SORTING IN BASE AI VOTI ****");

        top1000Movies.sort(Comparator.comparing(Movie::getVotes).reversed());
        top1000Movies.stream().limit(20).forEach(System.out::println);

    }
}