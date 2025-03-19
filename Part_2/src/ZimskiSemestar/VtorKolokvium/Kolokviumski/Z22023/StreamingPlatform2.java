package ZimskiSemestar.VtorKolokvium.Kolokviumski.Z22023;

import java.util.*;
import java.util.stream.Collectors;

class CosineSimilarityCalculator {

    public static double cosineSimilarity(Map<String, Integer> c1, Map<String, Integer> c2) {
        return cosineSimilarity(c1.values(), c2.values());
    }

    public static double cosineSimilarity(Collection<Integer> c1, Collection<Integer> c2) {
        int[] array1;
        int[] array2;
        array1 = c1.stream().mapToInt(i -> i).toArray();
        array2 = c2.stream().mapToInt(i -> i).toArray();
        double up = 0.0;
        double down1 = 0, down2 = 0;

        for (int i = 0; i < c1.size(); i++) {
            up += (array1[i] * array2[i]);
        }

        for (int i = 0; i < c1.size(); i++) {
            down1 += (array1[i] * array1[i]);
        }

        for (int i = 0; i < c1.size(); i++) {
            down2 += (array2[i] * array2[i]);
        }

        return up / (Math.sqrt(down1) * Math.sqrt(down2));
    }
}

class Movie implements Comparable<Movie> {
    String id;
    String name;
    List<Integer> ratings;

    public Movie(String id, String name) {
        this.id = id;
        this.name = name;
        ratings = new ArrayList<>();
    }

    public void addRating(int rating) {
        ratings.add(rating);
    }

    public double averageRating() {
        IntSummaryStatistics iss = ratings.stream()
                .mapToInt(i -> i).summaryStatistics();
        return iss.getAverage();
    }

    @Override
    public String toString() {
        return String.format("Movie ID: %s Title: %s Rating: %.2f",
                id, name, averageRating());
    }

    @Override
    public int compareTo(Movie o) {
        return Double.compare(o.averageRating(), this.averageRating());
    }
}

class User {
    String id;
    String username;
    Map<Integer, Set<Movie>> ratedMovies;

    public User(String id, String username) {
        this.id = id;
        this.username = username;
        ratedMovies = new HashMap<>();
    }

    public void addMovie(Movie movie, int rating) {
        ratedMovies.putIfAbsent(rating, new HashSet<>());
        ratedMovies.get(rating).add(movie);
    }

    public String favoriteMovies() {
//        Iterator<Integer> iterator = ratedMovies.keySet().iterator();
//
//        Set<Movie> toPrint = ratedMovies.get(iterator.next());
//
//        StringBuilder sb = new StringBuilder();
//        toPrint.forEach(v -> sb.append(v.toString()).append("\n"));
//        return sb.toString();

        Integer highestRating = ratedMovies.keySet().stream().sorted(Comparator.reverseOrder()).findFirst().orElse(null);
        if (highestRating == null) {
            return "";
        }

        Set<Movie> toPrint = ratedMovies.get(highestRating).stream().sorted(Comparator.comparing(Movie::averageRating).reversed())
                .collect(Collectors.toSet());
        StringBuilder sb = new StringBuilder();
        toPrint.forEach(v -> sb.append(v.toString()).append("\n"));
        return sb.toString();
    }

    public Map<String, Integer> getMovieRatingsMap(Collection<String> allMovieIds) {
        Map<String, Integer> movieRatings = new HashMap<>();
        allMovieIds.forEach(movieId -> movieRatings.put(movieId, 0));

        ratedMovies.forEach((key, value) -> {
            int rating = key;
            value.forEach(m -> {
                movieRatings.put(m.id, rating);
            });
        });

        return movieRatings;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Map<Integer, Set<Movie>> getRatedMovies() {
        return ratedMovies;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User ID: ")
                .append(id)
                .append(" Name: ")
                .append(username)
                .append("\n")
                .append(favoriteMovies());
        return sb.toString();
    }
}

class StreamingPlatform {
    Map<String, User> users;
    Map<String, Movie> movies;

    public StreamingPlatform() {
        users = new HashMap<>();
        movies = new HashMap<>();
    }

    public void addMovie(String id, String name) {
        movies.putIfAbsent(id, new Movie(id, name));
    }

    public void addUser(String id, String username) {
        users.putIfAbsent(id, new User(id, username));
    }

    public void addRating(String userId, String movieId, int rating) {
        User user = users.get(userId);
        Movie movie = movies.get(movieId);

        user.addMovie(movie, rating);
        movie.addRating(rating);
    }

    public void topNMovies(int n) {
        movies.values().stream()
                .sorted()
                .limit(n)
                .forEach(System.out::println);
    }

    public void favouriteMoviesForUsers(List<String> userIds) {
        users.values().stream()
                .filter(u -> userIds.contains(u.getId()))
                .forEach(System.out::println);
    }

    public void similarUsers(String userId) {
        User user = users.get(userId);
        if (user == null) {
            return;
        }

        Map<String, Integer> userRatingsMap = user.getMovieRatingsMap(movies.keySet());

        List<Map.Entry<User, Double>> similarityList = users.values().stream()
                .filter(u -> !u.getId().equals(userId))
                .map(u -> {
                    Map<String, Integer> otherUserRatingsMap = u.getMovieRatingsMap(movies.keySet());
                    double similarity = CosineSimilarityCalculator.cosineSimilarity(userRatingsMap, otherUserRatingsMap);
                    return new AbstractMap.SimpleEntry<>(u, similarity);
                })
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .collect(Collectors.toList());

        for (Map.Entry<User, Double> entry : similarityList) {
            System.out.println(String.format("User ID: %s Name: %s %.16f",
                    entry.getKey().getId(), entry.getKey().getUsername(), Double.valueOf(entry.getValue().doubleValue())));
        }
    }

}

public class StreamingPlatform2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        StreamingPlatform sp = new StreamingPlatform();

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            if (parts[0].equals("addMovie")) {
                String id = parts[1];
                String name = Arrays.stream(parts).skip(2).collect(Collectors.joining(" "));
                sp.addMovie(id, name);
            } else if (parts[0].equals("addUser")) {
                String id = parts[1];
                String name = parts[2];
                sp.addUser(id, name);
            } else if (parts[0].equals("addRating")) {
                //String userId, String movieId, int rating
                String userId = parts[1];
                String movieId = parts[2];
                int rating = Integer.parseInt(parts[3]);
                sp.addRating(userId, movieId, rating);
            } else if (parts[0].equals("topNMovies")) {
                int n = Integer.parseInt(parts[1]);
                System.out.println("TOP " + n + " MOVIES:");
                sp.topNMovies(n);
            } else if (parts[0].equals("favouriteMoviesForUsers")) {
                List<String> users = Arrays.stream(parts).skip(1).collect(Collectors.toList());
                System.out.println("FAVOURITE MOVIES FOR USERS WITH IDS: " + users.stream().collect(Collectors.joining(", ")));
                sp.favouriteMoviesForUsers(users);
            } else if (parts[0].equals("similarUsers")) {
                String userId = parts[1];
                System.out.println("SIMILAR USERS TO USER WITH ID: " + userId);
                sp.similarUsers(userId);
            }
        }
    }
}
