package az.music.android;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import az.music.android.model.Song;
import az.music.android.player.PlayerConstants;
import az.music.android.player.Utilities;

/**
 * Created by anar on 6/30/15.
 */
public class SongListAdapter extends ArrayAdapter<Song> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<Song> songs;
    private ItemsHolder holder;

    public SongListAdapter(Context context, int layoutResourceId, ArrayList<Song> songs){
        super(context,layoutResourceId,songs);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.songs = songs;
    }

    @Override
    public int getCount() {
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

            holder.title = (TextView) convertView.findViewById(R.id.song_title);
            holder.artist = (TextView) convertView.findViewById(R.id.singer);
            holder.duration = (TextView) convertView.findViewById(R.id.duration);
            holder.position = (TextView) convertView.findViewById(R.id.position);
            holder.background = (RelativeLayout) convertView.findViewById(R.id.song_background);

            convertView.setTag(holder);

        } else
            holder = (ItemsHolder) convertView.getTag();

        Song song = songs.get(position);

        holder.position.setText(String.valueOf(position + 1));
        holder.artist.setText(song.getArtist());
        holder.title.setText(song.getTitle());
        holder.duration.setText(Utilities.milliSecondsToTimer(song.getDuration()));

        if( (position % 2) == 0)
             holder.background.setBackgroundResource(R.drawable.song_item_selector_even);
        else
            holder.background.setBackgroundResource(R.drawable.song_item_selector);


        if (song.getSongId() == PlayerConstants.getCurrentSong().getSongId())
            holder.background.setActivated(true);
        else
            holder.background.setActivated(false);


        return convertView;
    }

    class ItemsHolder{
        TextView title, artist, duration, position;
        RelativeLayout background;
    }


    class ItemsHeaderHolder{
        TextView header;
    }

}
