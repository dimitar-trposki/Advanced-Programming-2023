package ZimskiSemestar.VtorKolokvium.Kolokviumski.Z24;

import java.util.*;
import java.util.stream.Collectors;

class Movie {
    private String title;
    private List<Integer> ratings;

    public Movie(String title, int[] ratings) {
        this.title = title;
        this.ratings = new ArrayList<>();
        for (int i = 0; i < ratings.length; i++) {
            this.ratings.add(i, ratings[i]);
        }
    }

    public double averageRating() {
        return (double) ratings.stream().mapToInt(i -> i).sum() / ratings.size();
    }

    public double averageRatingNumRatings() {
        return averageRating() * ratings.size();
    }

    public String getTitle() {
        return title;
    }

    public int getNumOfRatings() {
        return ratings.size();
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings", title, averageRating(), ratings.size());
    }
}

class MoviesList {
    List<Movie> movies;

    public MoviesList() {
        this.movies = new ArrayList<>();
    }

    public void addMovie(String title, int[] ratings) {
        movies.add(new Movie(title, ratings));
    }


    public List<Movie> top10ByAvgRating() {
        return movies.stream()
                .sorted(Comparator.comparing(Movie::averageRating).reversed().thenComparing(Movie::getTitle))
                .limit(10)
                .collect(Collectors.toList());
    }

    private int maxNumOfRatings() {
        IntSummaryStatistics iss = movies.stream()
                .mapToInt(Movie::getNumOfRatings)
                .summaryStatistics();
        return iss.getMax();
    }

    public List<Movie> top10ByRatingCoef() {
        return movies.stream()
                .sorted(Comparator.comparingDouble((Movie m) -> m.averageRatingNumRatings() / maxNumOfRatings())
                        .reversed()
                        .thenComparing(Movie::getTitle))
                .limit(10)
                .collect(Collectors.toList());
    }
}

public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}