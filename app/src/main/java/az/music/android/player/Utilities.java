package az.music.android.player;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import az.music.android.R;
import az.music.android.StoredData;
import az.music.android.model.Album;
import az.music.android.model.Song;

public class Utilities {

    /**
     * Function to convert milliseconds time to
     * Timer Format
     * Hours:Minutes:Seconds
     */
    public static String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }


    public static double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static Bitmap getAlbumart(Context context, Long album_id) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
            ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
            if (pfd != null) {
                FileDescriptor fd = pfd.getFileDescriptor();

                bm = BitmapFactory.decodeFileDescriptor(fd, null, options);
                pfd = null;
                fd = null;
            }
        } catch (Error ee) {
        } catch (Exception e) {
        }
        return bm;
    }


    public static ArrayList<Song> findSongs(String keyword) {
        ArrayList<Song> songList = new ArrayList<>();

        for (int i = 0; i < PlayerConstants.SONG_LIST_PLAYING.size(); i++) {
            Song song = PlayerConstants.SONG_LIST_PLAYING.get(i);

            if (song.getTitle().toLowerCase().contains("keyword".toLowerCase()))
                songList.add(song);
            else if (song.getArtist().toLowerCase().contains("keyword".toLowerCase()))
                songList.add(song);
            else if (song.getAlbum().toLowerCase().contains("keyword".toLowerCase()))
                songList.add(song);

        }

        return songList;
    }


    public static void initSongList(int source, long value) {

        ArrayList<Song> songList = new ArrayList<>();

        for (int i = 0; i < PlayerConstants.SONG_LIST_GENERAL.size(); i++) {
            Song song = PlayerConstants.SONG_LIST_GENERAL.get(i);

            if (song.getAlbumId() == value)
                songList.add(song);
        }

        PlayerConstants.setSongListFromAlbum(songList);
    }


    public static void initSongList(Context context) {
        //retrieve song info


        if (PlayerConstants.SONG_LIST_PLAYING.size() <= StoredData.getCurrentSongPosition())
            PlayerConstants.SONG_NUMBER = StoredData.getCurrentSongPosition();

        ArrayList<Song> songs = new ArrayList<>();

        ContentResolver musicResolver = context.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, MediaStore.Audio.Media.IS_MUSIC + " != 0", null, null);


        if (musicCursor.moveToFirst()) {
            // get columns
            int titleColumn = musicCursor.getColumnIndex(MediaStore.MediaColumns.TITLE);
            int idColumn = musicCursor.getColumnIndex(BaseColumns._ID);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST);
            int durationColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int dataColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int albumColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int albumIdColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int composerColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.COMPOSER);

            // add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                long thisDuration = musicCursor.getLong(durationColumn);
                String thisData = musicCursor.getString(dataColumn);
                String thisAlbum = musicCursor.getString(albumColumn);
                long thisAlbumId = musicCursor.getLong(albumIdColumn);
                String thisComposer = musicCursor.getString(composerColumn);

                Log.e("song", thisTitle);

                songs.add(new Song(thisId, thisTitle, thisArtist, thisAlbum, thisComposer, thisData, thisAlbumId, thisDuration));
            } while (musicCursor.moveToNext());

        }

        Collections.sort(songs, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });


        PlayerConstants.setSongsListGeneral(songs);
        PlayerConstants.setSongsList(songs);
        Log.e("Song count", PlayerConstants.SONG_LIST_PLAYING.size() + "");

        PlayerConstants.IS_PLAYING_FROM_ALL = true;

    }


    public static void initAlbumList(Context context) {


        ArrayList<Album> albums = new ArrayList<>();

        ContentResolver musicResolver = context.getContentResolver();
        Uri musicUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        String sortOrder = MediaStore.Audio.Albums.ALBUM + " ASC";
        Cursor albumCursor = musicResolver.query(musicUri, null, null, null, sortOrder);


        if (albumCursor.moveToFirst()) {
            // get columns
            int titleColumn = albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            int idColumn = albumCursor.getColumnIndex(MediaStore.Audio.Albums._ID);
            int artistColumn = albumCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST);
            int artColumn = albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
            int countColumn = albumCursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS);


            // add songs to list
            do {
                long thisId = albumCursor.getLong(idColumn);
                String thisTitle = albumCursor.getString(titleColumn);
                String thisArtist = albumCursor.getString(artistColumn);
                int thisCount = albumCursor.getInt(countColumn);
                String thisArt = albumCursor.getString(artColumn);

                albums.add(new Album(thisId, thisTitle, thisArtist, thisArt, thisCount));

                Log.e("albumart",thisArt+" ");

            } while (albumCursor.moveToNext());

        }

        Collections.sort(albums, new Comparator<Album>() {
            public int compare(Album a, Album b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });


        PlayerConstants.setAlbumList(albums);




        Log.e("countAlbun", PlayerConstants.ALBUM_LIST.size() + "");

    }


    /**
     * Function to get Progress percentage
     *
     * @param currentDuration
     * @param totalDuration
     */


    public static int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;




        // return percentage
        return percentage.intValue();
    }

    /**
     * Function to change progress to timer
     *
     * @param progress      -
     * @param totalDuration returns current duration in milliseconds
     */
    public static int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }


    public static void findMusic(String key){
        ArrayList<Song> songList = new ArrayList<>();

        for (int i = 0; i < PlayerConstants.SONG_LIST_GENERAL.size(); i++) {
            Song song = PlayerConstants.SONG_LIST_GENERAL.get(i);

            if (song.getTitle().toLowerCase().contains(key.toLowerCase()) ||
                    song.getAlbum().toLowerCase().contains(key.toLowerCase()) ||
                    song.getArtist().toLowerCase().contains(key.toLowerCase()))
                songList.add(song);

        }

        Log.e("count",songList.size()+"");
        Log.e("Key",key);

        PlayerConstants.setSongListFromSearch(songList);
    }


    public static void showDialog(Context context,Integer ... args){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        builder.setTitle(args[0]);
        builder.setMessage(args[1]);
        builder.setPositiveButton(args[2], null);

        if (args.length == 4){
            builder.setNegativeButton(args[3], null);
        }

        builder.show();
    }
}
