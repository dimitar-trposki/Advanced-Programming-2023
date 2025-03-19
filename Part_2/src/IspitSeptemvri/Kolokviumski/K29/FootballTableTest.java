package IspitSeptemvri.Kolokviumski.K29;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

class Team implements Comparable<Team> {

    String name;
    int goalsScored;
    int goalsAllowed;
    int numberOfWins;
    int numberOfDraws;
    int numberOfLosses;

    public Team(String name) {
        this.name = name;
        this.goalsScored = 0;
        this.goalsAllowed = 0;
        this.numberOfWins = 0;
        this.numberOfDraws = 0;
        this.numberOfLosses = 0;
    }

    public String getName() {
        return name;
    }

    public void processMatch(int goalsScored, int goalsAllowed, String result) {
        this.goalsScored += goalsScored;
        this.goalsAllowed += goalsAllowed;
        if (result.equals("Win")) {
            this.numberOfWins++;
        } else if (result.equals("Draw")) {
            this.numberOfDraws++;
        } else {
            this.numberOfLosses++;
        }
    }

    private int totalMatchedPlayed() {
        return numberOfWins + numberOfDraws + numberOfLosses;
    }

    public int points() {
        return ((numberOfWins * 3) + (numberOfDraws));
    }

    public int goalDifference() {
        return goalsScored - goalsAllowed;
    }

    @Override
    public String toString() {
        return String.format("%-15s%5d%5d%5d%5d%5d",
                name, totalMatchedPlayed(), numberOfWins, numberOfDraws, numberOfLosses, points());
    }

    @Override
    public int compareTo(Team o) {
        Comparator<Team> comparator = Comparator.comparing(Team::points)
                .thenComparing(Team::goalDifference).reversed()
                .thenComparing(Team::getName);
        return comparator.compare(this, o);
    }

}

class FootballTable {

    Map<String, Team> teams;

    public FootballTable() {
        this.teams = new HashMap<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        teams.putIfAbsent(homeTeam, new Team(homeTeam));
        teams.putIfAbsent(awayTeam, new Team(awayTeam));

        String homeTeamResult = "";
        String awayTeamResult = "";

        if (homeGoals == awayGoals) {
            homeTeamResult = awayTeamResult = "Draw";
        } else if (homeGoals > awayGoals) {
            homeTeamResult = "Win";
            awayTeamResult = "Lost";
        } else {
            homeTeamResult = "Lost";
            awayTeamResult = "Win";
        }

        teams.get(homeTeam).processMatch(homeGoals, awayGoals, homeTeamResult);
        teams.get(awayTeam).processMatch(awayGoals, homeGoals, awayTeamResult);
    }

    public void printTable() {
        StringBuilder stringBuilder = new StringBuilder();

        AtomicInteger i = new AtomicInteger(1);
        teams.values().stream()
                .sorted()
                .forEach(team -> stringBuilder.append(String.format("%2d. %s",
                        i.getAndIncrement(), team.toString())).append("\n"));

        System.out.println(stringBuilder.toString());
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