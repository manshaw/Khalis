package pakzarzameen.com.pk.khalis;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences prefs = null;
    private static final String PACAKGE_NAME = "pk.com.pakzarzameen.khalis";
    private FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav2);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        navigationView.setCheckedItem(R.id.nav_item_home);
        prefs = getSharedPreferences(PACAKGE_NAME, MODE_PRIVATE);
        if (!prefs.getBoolean("ProfileSpecified", false))
            openNextFragment(new ProfileFragment());
        else
            openHomeFragment();


    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
        else {
            super.onBackPressed();
        }
    }

    public void openHomeFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
                new MainFragment()).commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);
        switch (menuItem.getItemId()) {
            case R.id.nav_item_home:
                openNextFragment(new MainFragment());
                toolbar.setTitle("Home");
                break;
            case R.id.nav_item_profile:
                openNextFragment(new ProfileFragment());
                toolbar.setTitle("Profile");
                break;
            case R.id.nav_item_settings:
                openNextFragment(new SettingsFragment());
                toolbar.setTitle("Settings");
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void openNextFragment(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
