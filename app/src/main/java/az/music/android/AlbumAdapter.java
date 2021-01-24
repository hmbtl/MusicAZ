package az.music.android;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import az.music.android.model.Album;

/**
 * Created by anar on 6/10/15.
 */
public class AlbumAdapter extends BaseAdapter {

    private Context context;
    private List<Album> albums;


    public AlbumAdapter(Context context, List<Album> albums) {
        this.albums = albums;
        this.context = context;
    }


    @Override
    public int getCount() {
        return albums.size();
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        ItemHolder holder;

        if (row == null) {

            holder = new ItemHolder();

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.album_item, parent, false);

            holder.title = (TextView) row.findViewById(R.id.album_title);
            holder.count = (TextView) row.findViewById(R.id.album_count);
            holder.cover = (ImageView) row.findViewById(R.id.album_cover);

            row.setTag(holder);
        } else
            holder = (ItemHolder) row.getTag();

        Album album = albums.get(position);


        holder.title.setText(album.getTitle());
        holder.count.setText(String.valueOf(album.getCount()));
        if(album.getCover() == null)
            holder.cover.setImageResource(R.drawable.empty_cover);
        else{
            Drawable img = Drawable.createFromPath(album.getCover());
            holder.cover.setImageDrawable(img);
        }


        return row;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public Album getItem(int i) {
        return albums.get(i);
    }

    class ItemHolder {
        TextView title, count;
        ImageView cover;
    }
}
