package az.music.android.player;

/**
 * Created by anar on 7/2/15.
 */

import java.util.ArrayList;

import az.music.android.model.Album;
import az.music.android.model.Song;

public class PlayerConstants {
    //List of Songs
    public static ArrayList<Song> SONG_LIST_PLAYING = new ArrayList<Song>();

    public static ArrayList<Album> ALBUM_LIST = new ArrayList<Album>();

    public static int SONG_NUMBER = 0;


    public static ArrayList<Song> SONG_LIST_FROM_SEARCH = new ArrayList<Song>();

    public static ArrayList<Song> SONG_LIST_GENERAL = new ArrayList<>();

    public static ArrayList<Song> SONG_LIST_FROM_ALBUM = new ArrayList<Song>();

    //song is playing or paused
    public static boolean SONG_PAUSED = true;


    public static boolean IS_PLAYING_FROM_ALL = true;

    //song changed (next, previous)
    public static boolean FIRST_OPENED = true;

    //player is shuffle
    public static boolean IS_SHUFFLE  = false;

    //player repeat state
    public static int REPEAT_STATE = 0;



    public static Song getCurrentSong(){
        return PlayerConstants.SONG_LIST_PLAYING.get(PlayerConstants.SONG_NUMBER);
    }


    public static void setSongsList(ArrayList<Song> songList){
        PlayerConstants.SONG_LIST_PLAYING = songList;
    }

    public static void setSongsListGeneral(ArrayList<Song> songList){
        PlayerConstants.SONG_LIST_GENERAL = songList;
    }


    public static void setAlbumList(ArrayList<Album> albumList){
        PlayerConstants.ALBUM_LIST = albumList;
    }

    public static void setSongListFromAlbum(ArrayList<Song> songListFromAlbum){
        PlayerConstants.SONG_LIST_FROM_ALBUM = songListFromAlbum;
    }
    public static void setSongListFromSearch(ArrayList<Song> songListFromSearch){
        PlayerConstants.SONG_LIST_FROM_SEARCH = songListFromSearch;
    }


}
