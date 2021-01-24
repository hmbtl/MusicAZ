package az.music.android.model;

/**
 * Created by anar on 7/3/15.
 */
public class Album {

    private long albumId;
    private String title,cover,artist;
    private int count;

    public Album(){

    }

    public Album(long albumId,String title,String artist, String cover,int count ){
        this.albumId = albumId;
        this.title = title;
        this.artist = artist;
        this.cover = cover;
        this.count = count;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getTitle() {
        return title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

