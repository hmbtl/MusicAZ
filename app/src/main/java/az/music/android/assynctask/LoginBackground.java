package az.music.android.assynctask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import az.music.android.MusicPlayerActivity;
import az.music.android.R;
import az.music.android.StoredData;
import az.music.android.http.DataHTTPAdapter;
import az.music.android.player.Utilities;

/**
 * Created by anar on 6/25/15.
 */

public class LoginBackground extends AsyncTask<String, Void, String[]> {

    private String username,password;
    private Activity activity;

    public LoginBackground(Activity activity){
        this.activity = activity;
    }

    @Override
    protected String[] doInBackground(String... params) {
        DataHTTPAdapter adapter = new DataHTTPAdapter();
        username = params[0];
        password = params[1];
        String[] result = new String[]{"false"};

        try {
            result = adapter.login("994" + username, password);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String[] result) {
        if (result[0].equals("true")) {
            activity.startActivity(new Intent(activity, MusicPlayerActivity.class));
            StoredData.setUserName(username);
            StoredData.setPassword(password);
            StoredData.setLoggedIn(true);
            activity.finish();
        } else {
            Utilities.showDialog(activity, R.string.login, R.string.error_login_message, R.string.ok);
        }
    }
}