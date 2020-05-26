package edu.monash.MovieMemoir;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import edu.monash.MovieMemoir.database.PersonDatabase;
import edu.monash.MovieMemoir.entity.Person;

public class Home extends AppCompatActivity {
    PersonDatabase personDb = null;
    private AppBarConfiguration mAppBarConfiguration;
    TextView nameText;
    TextView emailText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //setContentView(R.layout.nav_header_home);
        View header = navigationView.getHeaderView(0);


        nameText = (TextView) header.findViewById(R.id.nameTextview);
        emailText = (TextView) header.findViewById(R.id.emailTextView);
        //setContentView(R.layout.activity_home);
        personDb = PersonDatabase.getInstance(this);
        SetNameTextAsyncTask asyncTask = new SetNameTextAsyncTask();
        asyncTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private class SetNameTextAsyncTask extends AsyncTask<Void, Void, Void> {
        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... objectArray) {
            List<Person> p = personDb.personDao().getAll();
            Person p1 = p.get(0);
            String personName = p1.getFirstName();
            String email = p1.getUsername();
            nameText.setText(personName);
            emailText.setText(email);
            return null;
        }
        @Override
        protected void onPostExecute(Void s) {
            Log.d("HEYA", "moveToHomeAfterLogin");

        }
    }
}
