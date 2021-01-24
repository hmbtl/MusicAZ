package az.music.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

/**
 * Created by anar on 6/28/15.
 */
public class MusicProgressDialog extends ProgressDialog {

    public MusicProgressDialog(Context context) {
        super(context);
    }

    private AnimationDrawable animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }
}


