package pakzarzameen.com.pk.khalis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class SplashActivity extends AppCompatActivity {
    FirebaseAuth auth;
    private String userID;
    private DatabaseReference database;
    boolean inDatabase = false;
    private static final String PACAKGE_NAME = "pk.com.pakzarzameen.khalis";
    SharedPreferences prefs = null;
    private AlphaAnimation alphaUp;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prefs = getSharedPreferences(PACAKGE_NAME, MODE_PRIVATE);
        logo = (ImageView) findViewById(R.id.logo);
        alphaUp = new AlphaAnimation(0.0f, 1.0f);
        alphaUp.setDuration(2500);
        logo.startAnimation(alphaUp);
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        if (!prefs.getBoolean("SignedIn", false)) {
                            // Do first run stuff here then set 'firstrun' as false
                            // using the following line to edit/commit prefs
                            startLoginActivity();
                        } else {
                            if (!prefs.getBoolean("ProfileSpecified", false))
                                inDatabase = false;
                            else
                                inDatabase = true;
                            startMainActivity();
                        }

                    }
                }, 3000);
    }

    private void startLoginActivity() {

        Intent nextActivity = new Intent(SplashActivity.this, LoginActivity.class);
        nextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(nextActivity);
        finish();

    }

    private void startMainActivity() {
        Intent nextActivity;
        if (inDatabase)
            nextActivity = new Intent(SplashActivity.this, MainActivity.class);
        else
            nextActivity = new Intent(SplashActivity.this, MainActivity.class);

        nextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(nextActivity);
        finish();
    }


}

