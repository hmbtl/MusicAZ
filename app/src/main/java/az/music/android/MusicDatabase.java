package az.music.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import az.music.android.model.Song;

/**
 * Created by anar on 6/29/15.
 */
public class MusicDatabase extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 3;

    private static final String DATABASE_NAME = "MUSICAZDATABASE";

    public MusicDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createCategories = "CREATE TABLE IF NOT EXISTS category (category_id INTEGER PRIMARY KEY, category_name TEXT)";
        String createSongs = "CREATE TABLE IF NOT EXISTS song (song_id INTEGER PRIMARY KEY, song_title TEXT, song_artist TEXT, category_id INTEGER, duration INTEGER)";
        String createPlayList = "CREATE TABLE IF NOT EXISTS playlist (playlist_id INTEGER, song_id INTEGER)";

        db.execSQL(createSongs);
        db.execSQL(createCategories);
        db.execSQL(createPlayList);

    }



    public void addSong(Song song){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("song_id",song.getSongId());
        values.put("song_title",song.getTitle());
        values.put("song_artist",song.getArtist());
        values.put("category_id",song.getCategoryId());
        values.put("duration", song.getDuration());

        db.replace("song", null, values);
        db.close();
    }

    public ArrayList<Song> getSongs(){
        ArrayList<Song> songs = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM song ORDER BY song_title";

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do {
                long songId = cursor.getLong(0);
                String title = cursor.getString(1);
                String artist = cursor.getString(2);
                long categoryId = cursor.getLong(3);
                long duration = cursor.getLong(4);

                songs.add(new Song(songId,title,artist,categoryId,duration));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return songs;
    }

    public Song getSong(int id){

        Song song = new Song();

        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM song WHERE song_id = '" + id + "'";
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
                long songId = cursor.getLong(0);
                String title = cursor.getString(1);
                String artist = cursor.getString(2);
                long categoryId = cursor.getLong(3);
                long duration = cursor.getLong(4);

                song = new Song(songId,title,artist,categoryId,duration);

        }

        cursor.close();
        db.close();

        return song;
    }



}
