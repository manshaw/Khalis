package pakzarzameen.com.pk.khalis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prefs = getSharedPreferences(PACAKGE_NAME, MODE_PRIVATE);

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        if (!prefs.getBoolean("SignedIn", false)) {
                            // Do first run stuff here then set 'firstrun' as false
                            // using the following line to edit/commit prefs
                            startLoginActivity();
                        } else {
                            if (!prefs.getBoolean("TypeSpecified", false))
                                inDatabase = false;
                            else
                                inDatabase = true;
                            startMainActivity();
                        }

                    }
                }, 2000);
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
            nextActivity = new Intent(SplashActivity.this, LoginActivity.class);

        nextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(nextActivity);
        finish();
    }


}

