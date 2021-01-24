package az.music.android;

/**
 * Created by anar on 6/25/15.
 */
import android.content.Context;
import android.content.SharedPreferences;

public class StoredData {

    private static SharedPreferences prefs;

    public static int timestamp;

    private static final String PREFS_FILENAME = "az.music.android";
    private static final String PREFS_FIRST_RUN = "firstrun";
    private static final String PREFS_USERNAME = "username";
    private static final String PREFS_PASSWORD = "password";
    private static final String PREFS_PINCODE = "pincode";
    private static final String PREFS_LOGGED_IN = "loggedin";
    private static final String PREFS_CURRENT_SONG = "currentsong";


    private static Context context;


    public static void init(Context con) {

        if (prefs == null) {
            prefs = con.getSharedPreferences(PREFS_FILENAME, con.MODE_PRIVATE);
        }
    }

    public static boolean isFirstRun() {
        return prefs.getBoolean(StoredData.PREFS_FIRST_RUN, true);
    }


    public static void setFirstRun(boolean isFirstRun) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(StoredData.PREFS_FIRST_RUN, isFirstRun);
        edit.commit();
    }


    public static String getUsername() {
        return prefs.getString(PREFS_USERNAME, "");
    }

    public static void setUserName(String username) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(StoredData.PREFS_USERNAME, username);
        edit.commit();
    }


    public static String getPassword() {
        return prefs.getString(PREFS_PASSWORD, "");
    }

    public static void setPassword(String password) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(StoredData.PREFS_PASSWORD, password);
        edit.commit();
    }

    public static String getPinCode() {
        return prefs.getString(PREFS_PINCODE, "");
    }

    public static boolean isLoggedIn() {
        return prefs.getBoolean(PREFS_LOGGED_IN, false);
    }

    public static void setLoggedIn(boolean isLoggedIn) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(StoredData.PREFS_LOGGED_IN, isLoggedIn);
        edit.commit();
    }

    public static void setPinCode(String pincode) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(StoredData.PREFS_PINCODE, pincode);
        edit.commit();
    }

    public static int getCurrentSongPosition() {
        return prefs.getInt(PREFS_CURRENT_SONG, 0);
    }



    public static void setCurrentSongPosition(int songPosition) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt(StoredData.PREFS_CURRENT_SONG, songPosition);
        edit.commit();
    }



}
