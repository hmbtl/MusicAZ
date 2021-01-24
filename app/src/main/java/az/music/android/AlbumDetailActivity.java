package az.music.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import az.music.android.model.Album;
import az.music.android.player.Constants;
import az.music.android.player.PlayerConstants;
import az.music.android.player.Utilities;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by anar on 7/3/15.
 */



public class AlbumDetailActivity extends AppCompatActivity {


    private TextView title, artist;
    private ImageView cover;
    private ListView musicList;

    private SongListAdapter adapter;

    private int albumPosition;

    private Album album = new Album();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_detail_layout);

        albumPosition = getIntent().getIntExtra("albumPosition", 0);

        album = PlayerConstants.ALBUM_LIST.get(albumPosition);

        title = (TextView) findViewById(R.id.album_detail_title);
        artist = (TextView) findViewById(R.id.album_detail_artist);
        cover = (ImageView) findViewById(R.id.album_detail_cover);


        title.setText(album.getTitle());
        artist.setText(album.getArtist());

        Log.e("AlbumART", album.getCover() + "");
        if(album.getCover() == null)
            cover.setImageResource(R.drawable.empty_cover);
        else{
            Drawable img = Drawable.createFromPath(album.getCover());
            cover.setImageDrawable(img);
        }


        Utilities.initSongList(Constants.FROM_ALBUM, album.getAlbumId());

        musicList = (ListView) findViewById(R.id.album_detail_list);
        adapter = new SongListAdapter(this,R.layout.song_item, PlayerConstants.SONG_LIST_FROM_ALBUM);

        musicList.setAdapter(adapter);

        musicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    PlayerConstants.SONG_LIST_PLAYING = PlayerConstants.SONG_LIST_FROM_ALBUM;
                    PlayerConstants.IS_PLAYING_FROM_ALL = false;


                PlayerConstants.SONG_NUMBER = i;
                sendMessage();
            }
        });

    }


    private void sendMessage() {
        Intent intent = new Intent("UPDATE_CONTROLLER");
        intent.putExtra("action", Constants.ACTION_PLAY_SONG);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
