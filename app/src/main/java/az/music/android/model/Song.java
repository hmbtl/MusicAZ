package az.music.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anar on 6/30/15.
 */
public class Song implements Parcelable {

    private long songId, categoryId, duration, albumId;
    private String title, artist, album, data, composer;

    public Song() {

    }

    public Song(long songId, String title, String artist, long categoryId, long duration) {
        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
    }


    public Song(long songId, String title, String artist, String album, String composer, String data, long albumId, long duration) {
        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.composer = composer;
        this.data = data;
        this.albumId = albumId;
        this.duration = duration;
    }


    public void setSongId(long songId) {
        this.songId = songId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public long getSongId() {
        return songId;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }


    public long getCategoryId() {
        return categoryId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
