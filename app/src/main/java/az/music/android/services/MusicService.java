package az.music.android.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Random;

import az.music.android.MusicPlayerActivity;
import az.music.android.R;
import az.music.android.player.Constants;
import az.music.android.player.PlayerConstants;
import az.music.android.player.Utilities;

/**
 * Created by anar on 6/30/15.
 */
public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private MediaPlayer player;
    //    private MusicDatabase db;

    private final IBinder musicBind = new MusicBinder();
    private String currentSongTitle = "";

    private static final int NOTIFY_ID = 1;

    int NOTIFICATION_ID = 1111;

    public static final String NOTIFY_PREVIOUS = "com.tutorialsface.audioplayer.previous";
    public static final String NOTIFY_DELETE = "com.tutorialsface.audioplayer.delete";
    public static final String NOTIFY_PAUSE = "com.tutorialsface.audioplayer.pause";
    public static final String NOTIFY_PLAY = "com.tutorialsface.audioplayer.play";
    public static final String NOTIFY_NEXT = "com.tutorialsface.audioplayer.next";


    private final String UPDATE_CONTROLLER = "UPDATE_CONTROLLER";
    private final String SET_CONTROLLER = "SET_CONTROLLER";


    private Uri trackUri;

    private Random random;


    @Override
    public void onCreate() {
        super.onCreate();

        player = new MediaPlayer();

        random = new Random();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        initMusicPlayer();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("UPDATE_PLAYER"));


//        newNotification();
        return START_STICKY;
    }

    public void initMusicPlayer() {
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);

    }


    private void sendMessage(int action) {
        Intent intent = new Intent("UPDATE_CONTROLLER");
        intent.putExtra("action", action);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    public void setSong() {
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                PlayerConstants.getCurrentSong().getSongId());
        try {
            player.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
    }

    public void playSong() {
        player.reset();

        Log.e("play song", "now");

        PlayerConstants.FIRST_OPENED = false;

        setSong();

        player.prepareAsync();
    }


    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        mediaPlayer.reset();
        return false;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        Intent notIntent = new Intent(this, MusicPlayerActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.ic_stat_notify_icon)
                .setTicker(currentSongTitle)
                .setOngoing(true)
                .setContentTitle("")
                .setContentText(currentSongTitle);
        Notification notification = builder.build();

        startForeground(NOTIFY_ID, notification);
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.e("This called", "completion");
        playerAction(Constants.PLAYER_NEXT, 0);

    }

    public int getPosition() {
        return player.getCurrentPosition();
    }

    public int getDuration() {
        return player.getDuration();
    }


    public void playerAction(int action, int value) {
        switch (action) {
            case Constants.PLAYER_PAUSE:
                player.pause();
                PlayerConstants.SONG_PAUSED = true;
                sendMessage(Constants.ACTION_UPDATE_CONTROLLER);
                break;
            case Constants.PLAYER_START:
                playSong();
                PlayerConstants.SONG_PAUSED = false;
                sendMessage(Constants.ACTION_SET_CONTROLLER);
                break;
            case Constants.PLAYER_RESUME:
                if (PlayerConstants.FIRST_OPENED)
                    playSong();
                else
                    player.start();
                PlayerConstants.SONG_PAUSED = false;
                sendMessage(Constants.ACTION_UPDATE_CONTROLLER);
                break;
            case Constants.PLAYER_STOP:
                player.stop();
                PlayerConstants.SONG_PAUSED = true;
                sendMessage(Constants.ACTION_UPDATE_CONTROLLER);
                break;
            case Constants.PLAYER_NEXT:
                playNext();
                sendMessage(Constants.ACTION_SET_CONTROLLER);
                break;
            case Constants.PLAYER_PREVIOUS:
                playPrevious();
                sendMessage(Constants.ACTION_SET_CONTROLLER);
                break;
            case Constants.PLAYER_RESTART:
                player.reset();
                PlayerConstants.SONG_PAUSED = true;
                sendMessage(Constants.ACTION_SET_CONTROLLER);
                break;
            case Constants.PLAYER_SEEK_TO:
                seek(value);
                sendMessage(Constants.ACTION_UPDATE_CONTROLLER);
                break;
            case Constants.PLAYER_SET_REPEAT:
                PlayerConstants.REPEAT_STATE = value;
                sendMessage(Constants.ACTION_UPDATE_CONTROLLER);
                break;
            case Constants.PLAYER_TOGGLE_SHUFFLE:
                toggleShuffle();
                sendMessage(Constants.ACTION_UPDATE_CONTROLLER);
                break;
        }

//        Log.e("Action type"," -  " + action);


    }


    private void seek(int posn) {
        player.seekTo(posn);

    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    private void playPrevious() {

        //decrease music position by one
        PlayerConstants.SONG_NUMBER--;


        // if it is first song then go to last song
        if (PlayerConstants.SONG_NUMBER < 0)
            PlayerConstants.SONG_NUMBER = PlayerConstants.SONG_LIST_PLAYING.size() - 1;

        playSong();
    }


    @SuppressLint("NewApi")
    private void newNotification() {
        String songName = PlayerConstants.getCurrentSong().getTitle();
        String albumName = PlayerConstants.getCurrentSong().getAlbum();
        String artistName = PlayerConstants.getCurrentSong().getArtist();

        RemoteViews expandedView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification_layout);


        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);


        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(largeIcon)
                .setContentTitle(songName).build();

        setListeners(expandedView);

        notification.bigContentView = expandedView;


        try {
            long albumId = PlayerConstants.getCurrentSong().getAlbumId();
            Bitmap albumArt = Utilities.getAlbumart(getApplicationContext(), albumId);
            if (albumArt != null) {
                notification.bigContentView.setImageViewBitmap(R.id.notification_cover, albumArt);
            } else {
                notification.bigContentView.setImageViewResource(R.id.notification_cover, R.drawable.empty_cover);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (PlayerConstants.SONG_PAUSED) {

            notification.bigContentView.setImageViewResource(R.id.notification_play, R.drawable.play_btn);
        } else {

            notification.bigContentView.setImageViewResource(R.id.notification_play, R.drawable.pause_btn);
        }

        notification.bigContentView.setTextViewText(R.id.notification_title, songName);
        notification.bigContentView.setTextViewText(R.id.notification_artist, artistName);
        notification.bigContentView.setTextViewText(R.id.notification_album, albumName);

        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        startForeground(NOTIFICATION_ID, notification);
    }


    public void setListeners(RemoteViews view) {
        Intent previous = new Intent(NOTIFY_PREVIOUS);
        Intent delete = new Intent(NOTIFY_DELETE);
        Intent pause = new Intent(NOTIFY_PAUSE);
        Intent next = new Intent(NOTIFY_NEXT);

        PendingIntent pPrevious = PendingIntent.getBroadcast(getApplicationContext(), 0, previous, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.notification_previous, pPrevious);

        PendingIntent pDelete = PendingIntent.getBroadcast(getApplicationContext(), 0, delete, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.notification_exit, pDelete);

        PendingIntent pPause = PendingIntent.getBroadcast(getApplicationContext(), 0, pause, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.notification_play, pPause);

        PendingIntent pNext = PendingIntent.getBroadcast(getApplicationContext(), 0, next, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.notification_next, pNext);


    }


    private void playNext() {

        if (PlayerConstants.REPEAT_STATE == Constants.REPEAT_STATE_SAME_SONG) {
            playSong();
            PlayerConstants.SONG_PAUSED = false;
        }
//        else if (PlayerConstants.REPEAT_STATE == Constants.REPEAT_STATE_STOP_END) {
//            if (PlayerConstants.SONG_NUMBER == PlayerConstants.SONGS_LIST.size()){
//                player.stop();
//                PlayerConstants.SONG_PAUSED = true;
//            } else {
//                play
//            }
//        }
        else {

            if (PlayerConstants.IS_SHUFFLE) {

                // remember current song position
                int nextSongPosition = PlayerConstants.SONG_NUMBER;

                // while no new number is generated repeat the loop
                while (nextSongPosition == PlayerConstants.SONG_NUMBER) {

                    // generate new number
                    nextSongPosition = random.nextInt(PlayerConstants.SONG_LIST_PLAYING.size());
                }

                PlayerConstants.SONG_NUMBER = nextSongPosition;

            } else {
                //increase music position by one
                PlayerConstants.SONG_NUMBER++;

                // if it is in the last song then go to first song
                if (PlayerConstants.SONG_NUMBER >= PlayerConstants.SONG_LIST_PLAYING.size())
                    PlayerConstants.SONG_NUMBER = 0;
            }

            playSong();
            PlayerConstants.SONG_PAUSED = false;

        }

    }

    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }


    private void toggleShuffle() {
        if (PlayerConstants.IS_SHUFFLE)
            PlayerConstants.IS_SHUFFLE = false;
        else
            PlayerConstants.IS_SHUFFLE = true;
    }


    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        stopForeground(true);
    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int action = intent.getIntExtra("action", Constants.PLAYER_PAUSE);
            int value = intent.getIntExtra("value", 0);

            playerAction(action, value);

        }
    };

}
