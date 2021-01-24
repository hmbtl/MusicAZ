package az.music.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;

import az.music.android.player.Constants;
import az.music.android.player.PlayerConstants;
import az.music.android.services.MusicService;

public class NotificationBroadcast extends BroadcastReceiver {

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) {
            KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(Intent.EXTRA_KEY_EVENT);
            if (keyEvent.getAction() != KeyEvent.ACTION_DOWN)
                return;

            switch (keyEvent.getKeyCode()) {
                case KeyEvent.KEYCODE_HEADSETHOOK:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    if (!PlayerConstants.SONG_PAUSED) {
                        sendMessage(Constants.PLAYER_PAUSE, 0);
                    } else {
                        sendMessage(Constants.PLAYER_START, 0);
                    }
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    break;
                case KeyEvent.KEYCODE_MEDIA_STOP:
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    Log.d("TAG", "TAG: KEYCODE_MEDIA_NEXT");
                    sendMessage(Constants.PLAYER_NEXT, 0);
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    Log.d("TAG", "TAG: KEYCODE_MEDIA_PREVIOUS");
                    sendMessage(Constants.PLAYER_PREVIOUS, 0);
                    break;
            }
        } else {
            if (intent.getAction().equals(MusicService.NOTIFY_PLAY)) {
                if (!PlayerConstants.SONG_PAUSED) {
                    sendMessage(Constants.PLAYER_PAUSE, 0);
                } else {
                    sendMessage(Constants.PLAYER_START, 0);
                }

            }else if (intent.getAction().equals(MusicService.NOTIFY_NEXT)) {
                sendMessage(Constants.PLAYER_NEXT, 0);
            } else if (intent.getAction().equals(MusicService.NOTIFY_DELETE)) {
                Intent i = new Intent(context, MusicService.class);
                context.stopService(i);
            } else if (intent.getAction().equals(MusicService.NOTIFY_PREVIOUS)) {
                sendMessage(Constants.PLAYER_PREVIOUS, 0);
            }
        }
    }

    private void sendMessage(int action, int value) {
        Intent intent = new Intent("UPDATE_PLAYER");
        intent.putExtra("action", action);
        intent.putExtra("value", value);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public String ComponentName() {
        return this.getClass().getName();
    }
}
