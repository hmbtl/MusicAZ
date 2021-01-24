package az.music.android.assynctask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import az.music.android.R;
import az.music.android.StartActivity;
import az.music.android.StoredData;
import az.music.android.http.DataHTTPAdapter;
import az.music.android.player.Utilities;

/**
 * Created by anar on 6/25/15.
 */

public class LogoutBackground extends AsyncTask<String, Void, Boolean> {

    private String username, password;
    private Activity activity;

    public LogoutBackground(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        DataHTTPAdapter adapter = new DataHTTPAdapter();

        boolean result = false;

        try {
            result = adapter.logout();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            StoredData.setLoggedIn(false);
            activity.finish();
            activity.startActivity(new Intent(activity, StartActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

        } else {
            Utilities.showDialog(activity, R.string.login, R.string.error_general, R.string.ok);
        }
    }
}