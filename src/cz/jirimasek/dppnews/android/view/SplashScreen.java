package cz.jirimasek.dppnews.android.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import cz.jirimasek.dppnews.android.R;
import cz.jirimasek.dppnews.android.view.map.IncidentMapView;

/**
 * <code>SplashScreen</code>
 * 
 * @author Jiří Mašek <email@jirimasek.cz>
 */
public class SplashScreen extends Activity
{

    /**
     * 
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splashscreen);

        new CountDownTimer(5000, 1000) {

            public void onFinish()
            {
                closeScreen();
            }

            @Override
            public void onTick(long millisUntilFinished)
            {
                // Nothing to do
            }
        }.start();
    }

    /**
     * 
     */
    private void closeScreen()
    {
        Intent lIntent = new Intent();
        
        lIntent.setClass(this, IncidentMapView.class);
        startActivity(lIntent);
        
        finish();
    }
}
