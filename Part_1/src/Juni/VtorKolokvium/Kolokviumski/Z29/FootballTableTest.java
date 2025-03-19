package Juni.VtorKolokvium.Kolokviumski.Z29;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

class Match {
    String homeTeam;
    String awayTeam;
    int homeGoals;
    int awayGoals;

    public Match(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
    }

    public String getWinner() {
        if (homeGoals > awayGoals) {
            return "HOME";
        } else if (awayGoals > homeGoals) {
            return "AWAY";
        }
        return "DRAW";
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public int getAwayGoals() {
        return awayGoals;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }
}

class Team implements Comparable<Team> {
    String name;
    Set<Match> matches;

    public Team(String name) {
        this.name = name;
        matches = new HashSet<>();
    }

    public void addMatch(Match match) {
        matches.add(match);
    }

    public int playedGames() {
        return matches.size();
    }

    public int wonGames() {
        int count = 0;
        for (Match match : matches) {
            if (match.homeTeam.equals(name) && match.getWinner().equals("HOME")
                    || match.awayTeam.equals(name) && match.getWinner().equals("AWAY")) {
                count++;
            }
        }
        return count;
    }

    public int drawGames() {
        int count = 0;
        for (Match match : matches) {
            if (match.getWinner().equals("DRAW")) {
                count++;
            }
        }
        return count;
    }

    public int lostGames() {
        int count = 0;
        for (Match match : matches) {
            if (match.homeTeam.equals(name) && match.getWinner().equals("AWAY")
                    || match.awayTeam.equals(name) && match.getWinner().equals("HOME")) {
                count++;
            }
        }
        return count;
    }

    public int totalPoints() {
        return wonGames() * 3 + drawGames();
    }

    public int goalDifference() {
        int goalsFor = matches.stream()
                .filter(match -> match.getHomeTeam().equals(name) || match.getAwayTeam().equals(name))
                .mapToInt(match -> match.getHomeTeam().equals(name) ? match.getHomeGoals() : match.getAwayGoals())
                .sum();

        int goalsAgainst = matches.stream()
                .filter(match -> match.getHomeTeam().equals(name) || match.getAwayTeam().equals(name))
                .mapToInt(match -> match.getHomeTeam().equals(name) ? match.getAwayGoals() : match.getHomeGoals())
                .sum();

        return goalsFor - goalsAgainst;
    }

    @Override
    public int compareTo(Team o) {
        Comparator<Team> comparator = Comparator.comparing(Team::totalPoints)
                .thenComparing(Team::goalDifference);
        return comparator.compare(this, o);
    }

    @Override
    public String toString() {
        return String.format("%-15s%5s%5s%5s%5s%5s\n",
                name, playedGames(), wonGames(), drawGames(), lostGames(), totalPoints());
    }
}

class FootballTable {
    Map<String, Team> teams;

    public FootballTable() {
        teams = new TreeMap<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        teams.putIfAbsent(homeTeam, new Team(homeTeam));
        teams.putIfAbsent(awayTeam, new Team(awayTeam));
        Match match = new Match(homeTeam, awayTeam, homeGoals, awayGoals);
        teams.get(homeTeam).addMatch(match);
        teams.get(awayTeam).addMatch(match);
    }

    public void printTable() {
        AtomicInteger counter = new AtomicInteger(1);
        teams.values()
                .stream().sorted(Comparator.reverseOrder())
                .forEach(t -> System.out.printf("%2d. %s", counter.getAndIncrement(), t.toString()));
    }
}

public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}