package az.music.android;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import az.music.android.assynctask.LogoutBackground;
import az.music.android.player.Constants;
import az.music.android.player.PlayerConstants;
import az.music.android.player.Utilities;
import az.music.android.services.MusicService;
import az.music.android.services.MusicService.MusicBinder;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by anar on 6/28/15.
 */
public class MusicPlayerActivity extends FragmentActivity implements SeekBar.OnSeekBarChangeListener {

//    private MusicDatabase db;


    private RelativeLayout controls, rightButtonsView;
    private ImageButton playPauseButton, prevButton, nextButton, shuffleButton, searchCancel;
    private ImageButton playPauseButtonBig, prevButtonBig, nextButtonBig, shuffleButtonBig, repeatButtonBig;
    private ImageView musicCover;

    private Handler mHandler = new Handler();

    private SeekBar songProgressBar;

    private ViewPager pager;
    private PagerSlidingTabStrip slidingTabs;

    private TextView logoutButton, songTitle, songArtist, totalDuration, currentDuration;
    private TextView songTitleBig, songArtistBig;
    DrawerLayout drawerLayout;

    private SlidingUpPanelLayout slidingUpPanelLayout;

    private MusicService musicService;
    private Intent playIntent;

    private EditText searchEditText;
    private ListView songSearchList;
    private Drawable searchDrawable;
    private SearchAdapter searchAdapter;

    private boolean isMusicBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTheme(R.style.AppTheme);
        initViewElements();

//        db = new MusicDatabase(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        logoutButton = (TextView) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LogoutBackground(MusicPlayerActivity.this).execute();
            }
        });



        pager = (ViewPager) findViewById(R.id.pager);

        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue-Condensed.ttf");

        slidingTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        slidingTabs.setTypeface(typeFace, 0);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager()));

        slidingTabs.setViewPager(pager);

        Utilities.initAlbumList(this);
        Utilities.initSongList(this);

        PlayerConstants.SONG_PAUSED = true;
        PlayerConstants.SONG_NUMBER = StoredData.getCurrentSongPosition();

        setController();
        Log.e("created", "d");


        searchAdapter = new SearchAdapter(this, R.layout.search_item);
        songSearchList.setAdapter(searchAdapter);

    }


    private void initViewElements() {

        // Music control buttons
        controls = (RelativeLayout) findViewById(R.id.controls);
        playPauseButton = (ImageButton) findViewById(R.id.playerPlay);
        prevButton = (ImageButton) findViewById(R.id.playerPrev);
        nextButton = (ImageButton) findViewById(R.id.playerNext);
        shuffleButton = (ImageButton) findViewById(R.id.shuffle_button);
        songProgressBar = (SeekBar) findViewById(R.id.seekProgress);


        // Right drawer search items
        songSearchList = (ListView) findViewById(R.id.song_list_search);
        rightButtonsView = (RelativeLayout) findViewById(R.id.navigation_buttons);
        searchEditText = (EditText) findViewById(R.id.search_edit);
        searchCancel = (ImageButton) findViewById(R.id.search_edit_cancel);
        searchEditText.addTextChangedListener(textWatcher);
        searchDrawable = ContextCompat.getDrawable(this, R.drawable.search);
        searchCancel.setOnClickListener(onClick);
        songSearchList.setVisibility(View.GONE);
        rightButtonsView.requestFocus();
        searchCancel.setVisibility(View.GONE);



        totalDuration = (TextView) findViewById(R.id.total_duration);
        currentDuration = (TextView) findViewById(R.id.current_duration);

        songTitle = (TextView) findViewById(R.id.music_title);
        songArtist = (TextView) findViewById(R.id.music_subtitle);

        musicCover = (ImageView) findViewById(R.id.music_cover);

        playPauseButtonBig = (ImageButton) findViewById(R.id.playerPlay_main);
        prevButtonBig = (ImageButton) findViewById(R.id.playerPrev_main);
        nextButtonBig = (ImageButton) findViewById(R.id.playerNext_main);
        shuffleButtonBig = (ImageButton) findViewById(R.id.shuffle_button_main);
        repeatButtonBig = (ImageButton) findViewById(R.id.repeat_main);

        songTitleBig = (TextView) findViewById(R.id.music_title_main);
        songArtistBig = (TextView) findViewById(R.id.music_artist_main);


        songProgressBar.setOnSeekBarChangeListener(this);

        playPauseButton.setOnClickListener(onClick);
        prevButton.setOnClickListener(onClick);
        nextButton.setOnClickListener(onClick);
        shuffleButton.setOnClickListener(onClick);

        playPauseButtonBig.setOnClickListener(onClick);
        prevButtonBig.setOnClickListener(onClick);
        nextButtonBig.setOnClickListener(onClick);
        shuffleButtonBig.setOnClickListener(onClick);

        pager = (ViewPager) findViewById(R.id.pager);
        slidingTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
                controls.setAlpha(Math.abs(1 - v));
//                Log.e("OFFSET ", "onPanelSlide, offset " + v);
            }

            @Override
            public void onPanelCollapsed(View view) {
                songTitle.setSelected(false);
            }

            @Override
            public void onPanelExpanded(View view) {
                songTitle.setSelected(true);
            }

            @Override
            public void onPanelAnchored(View view) {

            }

            @Override
            public void onPanelHidden(View view) {
            }
        });


    }


    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == shuffleButton || view == shuffleButtonBig) {
                musicService.playerAction(Constants.PLAYER_TOGGLE_SHUFFLE, 0);

            } else if (view == playPauseButton || view == playPauseButtonBig) {

                if (PlayerConstants.SONG_PAUSED)
                    musicService.playerAction(Constants.PLAYER_RESUME, 0);
                else
                    musicService.playerAction(Constants.PLAYER_PAUSE, 0);

            } else if (view == nextButton || view == nextButtonBig)
                musicService.playerAction(Constants.PLAYER_NEXT, 0);

            else if (view == prevButton || view == prevButtonBig)
                musicService.playerAction(Constants.PLAYER_PREVIOUS, 0);

            else if (view == searchCancel){
                searchEditText.setText("");
                searchEditText.setCompoundDrawablesWithIntrinsicBounds(null,null, searchDrawable,null);
                searchCancel.setVisibility(View.GONE);
                songSearchList.setVisibility(View.GONE);
                rightButtonsView.setVisibility(View.VISIBLE);
                rightButtonsView.requestFocus();
            }
        }
    };


    private void updateController() {

        if (PlayerConstants.IS_SHUFFLE) {
            shuffleButton.setSelected(true);
            shuffleButtonBig.setSelected(true);
        } else {
            shuffleButton.setSelected(false);
            shuffleButtonBig.setSelected(false);
        }

        if (PlayerConstants.SONG_PAUSED) {
            playPauseButton.setImageResource(R.drawable.play_button);
            playPauseButtonBig.setImageResource(R.drawable.play_button);
        } else {
            playPauseButton.setImageResource(R.drawable.pause_button);
            playPauseButtonBig.setImageResource(R.drawable.pause_button);
        }

    }


    private void setController() {

        Log.e("status", String.valueOf(PlayerConstants.SONG_PAUSED));

        songTitle.setText(PlayerConstants.getCurrentSong().getTitle());
        songArtist.setText(PlayerConstants.getCurrentSong().getArtist());

        songTitleBig.setText(PlayerConstants.getCurrentSong().getTitle());
        songArtistBig.setText(PlayerConstants.getCurrentSong().getArtist());

        songProgressBar.setMax((int) PlayerConstants.getCurrentSong().getDuration());

        currentDuration.setText("0:00");

        totalDuration.setText(Utilities.milliSecondsToTimer(PlayerConstants.getCurrentSong().getDuration()));

        Bitmap bitmap = Utilities.getAlbumart(this, PlayerConstants.getCurrentSong().getAlbumId());

        if (bitmap == null)
            musicCover.setImageResource(R.drawable.empty_cover);
        else
            musicCover.setImageBitmap(bitmap);

        songProgressBar.setProgress(0);




        updateController();

    }


    public void playSong() {
        musicService.playerAction(Constants.PLAYER_START, 0);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            musicService.playerAction(Constants.PLAYER_SEEK_TO, progress);
            currentDuration.setText(Utilities.milliSecondsToTimer(progress));
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
            LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                    new IntentFilter("UPDATE_CONTROLLER"));



            mHandler.post(seekProgress);
        }

        Log.e("started", "d");

    }


    @Override
    protected void onDestroy() {
        StoredData.setCurrentSongPosition(PlayerConstants.SONG_NUMBER);
        stopService(playIntent);
        musicService = null;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        Log.e("Destroyer", "d");
        super.onDestroy();
    }


    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder) service;
            //get service
            musicService = binder.getService();
            //pass list
            isMusicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isMusicBound = false;
        }
    };


    class PagerAdapter extends FragmentPagerAdapter {

        private String tabTitles[] = new String[]{"Yerli", "Xarici", "Türk", "Album"};

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 3)
                return AlbumListFragment.newInstance(position);
            else
                return SongListFragment.newInstance(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }


    @Override
    public void onBackPressed() {
        if (slidingUpPanelLayout != null &&
                (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {

            if (PlayerConstants.SONG_PAUSED)
                super.onBackPressed();
            else
                moveTaskToBack(true);
        }
    }


    Thread seekProgress = new Thread(new Runnable() {
        @Override
        public void run() {
            if (!PlayerConstants.SONG_PAUSED) {
                int duration = musicService.getCurrentPosition();
                songProgressBar.setProgress(duration);
                currentDuration.setText(Utilities.milliSecondsToTimer(duration));
            }
            mHandler.postDelayed(this, 1000);
        }
    });

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }





    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            searchEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            searchCancel.setVisibility(View.VISIBLE);
            rightButtonsView.setVisibility(View.GONE);
            songSearchList.setVisibility(View.VISIBLE);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            Utilities.findMusic(searchEditText.getText().toString());
            searchAdapter.notifyDataSetChanged();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };



    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int action = intent.getIntExtra("action", Constants.ACTION_UPDATE_CONTROLLER);

            switch (action) {
                case Constants.ACTION_PLAY_SONG:
                    playSong();
                    break;
                case Constants.ACTION_UPDATE_CONTROLLER:
                    updateController();
                    break;
                case Constants.ACTION_SET_CONTROLLER:
                    setController();
                    break;
            }

        }
    };

}
