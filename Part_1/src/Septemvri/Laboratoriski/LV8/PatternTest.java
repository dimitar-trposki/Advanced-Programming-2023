package Septemvri.Laboratoriski.LV8;

import java.util.ArrayList;
import java.util.List;

interface ISongState {

    void play();

    void stop();

    void fwd();

    void rew();

}

abstract class MP3PlayerState implements ISongState {

    MP3Player mp3Player;

    public MP3PlayerState(MP3Player mp3Player) {
        this.mp3Player = mp3Player;
    }

    public void fwdFunction() {
        if (MP3Player.i == mp3Player.songs.size() - 1) {
            MP3Player.i = 0;
        } else {
            MP3Player.i += 1;
        }
        mp3Player.currentSong = mp3Player.songs.get(MP3Player.i);
        mp3Player.mp3PlayerState = new StoppedState(mp3Player);
        mp3Player.state = "ForwardedState";
        System.out.println("Forward...");
    }

    public void rewFunction() {
        if (MP3Player.i == 0) {
            MP3Player.i = mp3Player.songs.size() - 1;
        } else {
            MP3Player.i -= 1;
        }
        mp3Player.currentSong = mp3Player.songs.get(MP3Player.i);
        mp3Player.mp3PlayerState = new StoppedState(mp3Player);
        mp3Player.state = "RewardedState";
        System.out.println("Reward...");
    }

}

class PlayingState extends MP3PlayerState {

    public PlayingState(MP3Player mp3Player) {
        super(mp3Player);
    }

    @Override
    public void play() {
        System.out.println("Song is already playing");
        mp3Player.state = "PlayingState";
    }

    @Override
    public void stop() {
        System.out.printf("Song %d is paused\n", MP3Player.i);
        mp3Player.state = "OnceStoppedState";
        mp3Player.mp3PlayerState = new StoppedState(mp3Player);
    }

    @Override
    public void fwd() {
        fwdFunction();
    }

    @Override
    public void rew() {
        rewFunction();
    }

}

class StoppedState extends MP3PlayerState {

    public StoppedState(MP3Player mp3Player) {
        super(mp3Player);
    }

    @Override
    public void play() {
        System.out.printf("Song %d is playing\n", MP3Player.i);
        mp3Player.mp3PlayerState = new PlayingState(mp3Player);
        mp3Player.state = "PlayingState";
    }

    @Override
    public void stop() {
        if (mp3Player.state.equals("StoppedState")) {
            System.out.println("Songs are already stopped");
        } else {
            MP3Player.i = 0;
            mp3Player.currentSong = mp3Player.songs.get(MP3Player.i);
            System.out.println("Songs are stopped");
        }
        mp3Player.mp3PlayerState = new StoppedState(mp3Player);
        mp3Player.state = "StoppedState";
    }

    @Override
    public void fwd() {
        fwdFunction();
    }

    @Override
    public void rew() {
        rewFunction();
    }

}

class Song {

    String title;
    String artist;

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "Song{" +
                "title=" + title + ", artist=" + artist + '}';
    }

}

class MP3Player {

    List<Song> songs;
    Song currentSong;
    MP3PlayerState mp3PlayerState;
    String state;
    static int i = 0;

    public MP3Player(List<Song> songs) {
        this.songs = songs;
        this.currentSong = songs.get(i);
        this.mp3PlayerState = new StoppedState(this);
        this.state = "StoppedState";
    }

    public void pressPlay() {
        mp3PlayerState.play();
    }

    public void pressStop() {
        mp3PlayerState.stop();
    }

    public void pressFWD() {
        mp3PlayerState.fwd();
    }

    public void pressREW() {
        mp3PlayerState.rew();
    }

    public void printCurrentSong() {
        System.out.println(currentSong);
    }

    @Override
    public String toString() {
        return "MP3Player{" +
                "currentSong = " + MP3Player.i +
                ", songList = " + songs +
                '}';
    }
}

public class PatternTest {
    public static void main(String args[]) {
        List<Song> listSongs = new ArrayList<Song>();
        listSongs.add(new Song("first-title", "first-artist"));
        listSongs.add(new Song("second-title", "second-artist"));
        listSongs.add(new Song("third-title", "third-artist"));
        listSongs.add(new Song("fourth-title", "fourth-artist"));
        listSongs.add(new Song("fifth-title", "fifth-artist"));
        MP3Player player = new MP3Player(listSongs);


        System.out.println(player.toString());
        System.out.println("First test");


        player.pressPlay();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Second test");


        player.pressStop();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Third test");


        player.pressFWD();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
    }
}