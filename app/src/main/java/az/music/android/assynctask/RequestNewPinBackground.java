package az.music.android.assynctask;

import android.os.AsyncTask;

import az.music.android.http.DataHTTPAdapter;

/**
 * Created by anar on 6/25/15.
 */
public class RequestNewPinBackground extends AsyncTask<String, Void, String[]> {
    @Override
    protected String[] doInBackground(String... params) {
        DataHTTPAdapter adapter = new DataHTTPAdapter();
        String[] result = new String[]{"false"};
        try {
            result = adapter.newpin("994" + params[0]);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String[] result) {
 /*      loadingView.dismiss();
        if (result[0].equals("true")) {
            hideAllPanels();
            LinearLayout panel = (LinearLayout) findViewById(R.id.panelLogin);
            panel.setVisibility(View.VISIBLE);
            panelStack.push(R.id.panelRegister);
            Toast.makeText(StartActivity.this, getResources().getString(R.string.sSendingPinSuccess), 800)
                    .show();
        } else {
            Toast.makeText(StartActivity.this, getResources().getString(R.string.sError), Toast.LENGTH_SHORT)
                    .show();
        }*/
    }
}