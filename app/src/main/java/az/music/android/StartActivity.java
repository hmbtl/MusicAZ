package az.music.android;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.beardedhen.androidbootstrap.BootstrapButton;

import az.music.android.assynctask.LoginBackground;
import az.music.android.http.DataHTTPAdapter;
import az.music.android.player.Utilities;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class StartActivity extends Activity {

    private LinearLayout forgetPanel, registerPanel;
    private RelativeLayout loginPanel;

    private final static int ACTIVITY_REGISTER = 0;
    private final static int ACTIVITY_FORGET = 1;
    private final static int ACTIVITY_LOGIN = 2;

    private String regExCorrectPhone = "\\s*?(((50|51|55|70|77)([- ])?([2-9])))(\\d{2})([- ])?(\\d{2})([- ])?\\d{2}\\s*?$";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        setTheme(R.style.LoginAppTheme);
        // Initialize all settings
        StoredData.init(this);


//        String expired = getIntent().getStringExtra("expired");
//        if (!SID.SID.equals("dev")) {
//
//            return;
//        }
//

        Log.e("Step 1", "Passed");

        Log.e("IsLoggedIn", String.valueOf(StoredData.isLoggedIn()));


        // Initialise all the panel inside view
        initPanels();


        // if user already logged in then start music application
        if (StoredData.isLoggedIn()) {
            new LoginBackground(this).execute(StoredData.getUsername(), StoredData.getPassword());
        } else {
            initLoginPanel();
            Log.e("Step 2", "Passed");
        }

        Log.e("Step 3", "Passed");

    }


    private void initPanels() {
        registerPanel = (LinearLayout) findViewById(R.id.panelRegister);
        loginPanel = (RelativeLayout) findViewById(R.id.panelLogin);
        forgetPanel = (LinearLayout) findViewById(R.id.panelForgotPassword);

        hideAllPanels();

    }


    private void hideAllPanels() {
        registerPanel.setVisibility(View.GONE);
        loginPanel.setVisibility(View.GONE);
        forgetPanel.setVisibility(View.GONE);
    }


    private void initRegistrationPanel() {

        registerPanel.setVisibility(View.VISIBLE);


        final EditText phoneNumber = (EditText) findViewById(R.id.registerUsername);
        final BootstrapButton registerButton = (BootstrapButton) findViewById(R.id.sendpin);

        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPhone(phoneNumber)) {
                    Utilities.showDialog(StartActivity.this, R.string.login, R.string.error_wrong_number, R.string.ok);
                } else {
                    new BackgroundHttpRequest(ACTIVITY_REGISTER).execute(phoneNumber.getText().toString());
                }
            }
        });

    }


    private void initLoginPanel() {

        loginPanel.setVisibility(View.VISIBLE);

        final EditText phoneNumber = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);

        final BootstrapButton loginButton = (BootstrapButton) findViewById(R.id.login);
        final BootstrapButton register = (BootstrapButton) findViewById(R.id.register);
        final Button forgetPasswordButton = (Button) findViewById(R.id.password_reset);

        phoneNumber.setText(StoredData.getUsername());

        View.OnClickListener onButtonClick = new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view == loginButton) {

                    if (!checkPhone(phoneNumber))
                        Utilities.showDialog(StartActivity.this, R.string.login, R.string.error_wrong_number, R.string.ok);
                    else if (password.getText().toString().matches(""))
                        Utilities.showDialog(StartActivity.this, R.string.login, R.string.error_empty_password, R.string.ok);
                    else
                        new LoginBackground(StartActivity.this).execute(phoneNumber.getText().toString(),
                                password.getText().toString());


                } else if (view == register) {
                    animateView(loginPanel, ACTIVITY_REGISTER);

                } else if (view == forgetPasswordButton) {
                    animateView(loginPanel, ACTIVITY_FORGET);
                }

            }
        };

        loginButton.setOnClickListener(onButtonClick);
        register.setOnClickListener(onButtonClick);
        forgetPasswordButton.setOnClickListener(onButtonClick);

    }


    private void animateView(final View hidingPanel, final int showingPanel) {

        hidingPanel.setAlpha(1.0f);

        hidingPanel.animate()
                .translationY(100)
                .setDuration(350)
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        hidingPanel.setVisibility(View.GONE);
                        switch (showingPanel) {
                            case ACTIVITY_FORGET:
                                initForgetPasswordPanel();
                                break;
                            case ACTIVITY_LOGIN:
                                loginPanel.setVisibility(View.VISIBLE);
                                break;
                            case ACTIVITY_REGISTER:
                                initRegistrationPanel();
                                break;
                        }
                        hidingPanel.setAlpha(1.0f);
                        hidingPanel.setTranslationY(0);
                    }
                });

    }


    private void initForgetPasswordPanel() {

        forgetPanel.setVisibility(View.VISIBLE);


        final EditText phoneNumber = (EditText) findViewById(R.id.forgotUsername);
        final BootstrapButton resetButton = (BootstrapButton) findViewById(R.id.forgotSendPin);

        resetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == resetButton) {
                    if (checkPhone(phoneNumber))
                        new BackgroundHttpRequest(ACTIVITY_FORGET).execute(phoneNumber.getText().toString());
                    else
                        Utilities.showDialog(StartActivity.this, R.string.login, R.string.error_wrong_number, R.string.ok);

                }
            }
        });

        if (registerPanel.getVisibility() == View.VISIBLE)
            Log.e("MUSIC", "SOMETHING IS WRONG");


    }


    private boolean checkPhone(EditText phone) {

        if (phone.getText().toString().matches(regExCorrectPhone))
            return true;

        return false;
    }


    @Override
    public void onBackPressed() {

        if (forgetPanel.getVisibility() == View.VISIBLE) {
            animateView(forgetPanel, ACTIVITY_LOGIN);
            Log.d("MUSIC AZ", "LOGIN SHOULD BE VISIBLE FROM FORGET");
        } else if (loginPanel.getVisibility() == View.VISIBLE) {
            super.onBackPressed();
            Log.d("MUSIC AZ", "SHOULD EXIT");
        } else if (registerPanel.getVisibility() == View.VISIBLE) {
            animateView(registerPanel, ACTIVITY_LOGIN);
            Log.d("MUSIC AZ", "LOGIN SHOULD BE VISIBLE");
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    class BackgroundHttpRequest extends AsyncTask<String, Void, String[]> {

        private int requestType;

        public BackgroundHttpRequest(int requestType) {
            this.requestType = requestType;
        }

        @Override
        protected String[] doInBackground(String... params) {

            DataHTTPAdapter adapter = new DataHTTPAdapter();
            String[] result = new String[]{"false"};

            try {
                if (requestType == ACTIVITY_FORGET)
                    result = adapter.newpin("994" + params[0]);
                else if (requestType == ACTIVITY_REGISTER)
                    result = adapter.register("994" + params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);

            if (result[0].equals("true")) {
                if (requestType == ACTIVITY_FORGET)
                    animateView(forgetPanel, ACTIVITY_LOGIN);
                else if (requestType == ACTIVITY_REGISTER)
                    animateView(registerPanel, ACTIVITY_LOGIN);
            } else
                Utilities.showDialog(StartActivity.this, R.string.login, R.string.error_general, R.string.ok);

        }
    }

}
