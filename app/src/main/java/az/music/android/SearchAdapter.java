package az.music.android;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import az.music.android.model.Song;
import az.music.android.player.PlayerConstants;

/**
 * Created by anar on 7/16/15.
 */
public class SearchAdapter extends ArrayAdapter<Song> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<Song> songs;
    private ItemsHolder holder;

    public SearchAdapter(Context context, int layoutResourceId){
        super(context,layoutResourceId);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.songs = PlayerConstants.SONG_LIST_FROM_SEARCH;
    }

    @Override
    public int getCount() {
        Log.e("GetCount", songs.size() + "");
        return songs.size();


    }

    @Override
    public Song getItem(int i) {
        return songs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return songs.get(i).getSongId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);

            holder = new ItemsHolder();

            holder.title = (TextView) convertView.findViewById(R.id.search_title);
            holder.artist = (TextView) convertView.findViewById(R.id.search_artist);


            convertView.setTag(holder);

        } else
            holder = (ItemsHolder) convertView.getTag();

        Song song = songs.get(position);

        holder.artist.setText(song.getArtist());
        holder.title.setText(song.getTitle());

        return convertView;
    }

    class ItemsHolder{
        TextView title, artist;
    }


}
