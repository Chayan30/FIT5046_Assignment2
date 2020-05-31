package edu.monash.MovieMemoir;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Objects;

import edu.monash.MovieMemoir.HttpRequests;
import edu.monash.MovieMemoir.R;
import edu.monash.MovieMemoir.database.WatchlistDatabase;
import edu.monash.MovieMemoir.entity.Watchlist;
import edu.monash.MovieMemoir.ui.movieSearch.ListAdapter;

import static edu.monash.MovieMemoir.R.*;

public class MovieInfoView extends AppCompatActivity {
    private View root;
    TextView text_genre;
    TextView text_cast;
    TextView text_rel_date;
    TextView text_country;
    TextView text_director;
    TextView text_plot;
    ImageView img_poster;
    RatingBar ratingBar;
    Button addWatchlist;
    String title;
    String ryear;
    String poster;
    String Id;
    Button addMemoir;
    ProgressBar progressBarWaitMovieInfo;
    WatchlistDatabase watchlistDatabase=null;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_movie_info);
        watchlistDatabase = WatchlistDatabase.getInstance(this);
        addWatchlist = findViewById(R.id.button_add_watch);
        addMemoir = findViewById(R.id.button_add_memoir);
        text_genre = findViewById(id.genre_info);
        text_cast = findViewById(id.cast_info);
        text_rel_date = findViewById(id.rel_date_info);
        text_country = findViewById(id.country_info);
        text_director = findViewById(id.director_info);
        text_plot = findViewById(id.plot_info);
        img_poster = findViewById(id.imageView_poster);
        ratingBar = findViewById(id.ratingBar);
        ratingBar.setEnabled(false);
        title = getIntent().getStringExtra("title");
        progressBarWaitMovieInfo = findViewById(id.progressBarWaitMovieInfo);
        progressBarWaitMovieInfo.setVisibility(View.VISIBLE);
        Id = getIntent().getStringExtra("id");
        checkWatchlist checkasyncTask = new checkWatchlist();
        checkasyncTask.execute();
        getSupportActionBar().setTitle(title); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String request = "?i=" + Id;
        getMovieInfo movieinfo = new getMovieInfo();
        movieinfo.execute(request);

        addWatchlist.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                addWatchlist asyntask = new addWatchlist();
                asyntask.execute();
                addWatchlist.setClickable(false);
                addWatchlist.setText(string.added_watchlist);
                addWatchlist.setBackgroundColor(color.colorPrimary);
            }
        });
        addMemoir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToMemoir asynctask = new addToMemoir();
                asynctask.execute();
            }
        });
    }
    private class getMovieInfo extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            return HttpRequests.httpMovieGetRequests(params[0]);
        }

        private float getRating(float rating)
        {
            float ratings = 0;
            if(rating < 1)
                ratings = 0;
            else if (rating < 1.9)
                ratings = (float) 0.5;
            else if (rating < 2.8)
                ratings = 1;
            else if (rating < 3.7)
                ratings = (float) 1.5;
            else if (rating < 4.6)
                ratings = 2;
            else if (rating < 5.5)
                ratings = (float) 2.5;
            else if (rating < 6.4)
                ratings = 3;
            else if (rating < 7.3)
                ratings = (float) 3.5;
            else if (rating < 8.2)
                ratings = 4;
            else if (rating < 9.1)
                ratings = (float) 4.5;
            else
                ratings = 5;
            return ratings;
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            //String responseStatus = response.getString("status");
            //if(Integer.parseInt(responseStatus) == 200) {
            String genre = null;
            String cast = null;
            String country = null;
            String director = null;
            String plot = null;
            String rating1 = null;
            float rating = 0;
            try {
                genre = response.getString("Genre");
                ryear = response.getString("Released");
                poster = response.getString("Poster");
                cast = response.getString("Actors");
                country = response.getString("Country");
                director = response.getString("Director");
                plot = response.getString("Plot");
                rating1 = response.getString("imdbRating");
                if(!rating1.equalsIgnoreCase("n/a")) {
                    rating = Float.parseFloat(rating1);
                    rating = getRating(rating);
                }
                Log.d("Genre", genre);
                Log.d("Year", ryear);
                Log.d("Poster", poster);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            text_genre.setText(genre);
            text_cast.setText(cast);
            text_country.setText(country);
            text_director.setText(director);
            text_rel_date.setText(ryear);
            text_plot.setText(plot);
            ratingBar.setRating(rating);
            Picasso.get()
                    .load(poster)
                    .placeholder(mipmap.ic_launcher)
                    .resize(200, 200)
                    .centerInside()
                    .into(img_poster);
            progressBarWaitMovieInfo.setVisibility(View.INVISIBLE);
        }
    }
    private class addWatchlist extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Date cur_date = new Date();
            Watchlist watchlist = new Watchlist(title,ryear,cur_date.toString(), Id);
            long watchId = watchlistDatabase.watchlistDao().insert(watchlist);
            return null;
        }

        @Override
        protected void onPostExecute(Void response) {
        }
    }
    private class addToMemoir extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void response) {
            Intent intent = new Intent(MovieInfoView.this, AddToMemoir.class);
            intent.putExtra("title", title);
            intent.putExtra("ryear", ryear);
            intent.putExtra("poster", poster);
            startActivity(intent);
        }
    }
    private class checkWatchlist extends AsyncTask<Void, Void, Void> {
        @SuppressLint({"WrongThread", "ResourceAsColor"})
        @Override
        protected Void doInBackground(Void... params) {
            Date cur_date = new Date();
            Watchlist watchlist =  watchlistDatabase.watchlistDao().findByID(Id);
            if(watchlist != null)
            {
                addWatchlist.setClickable(false);
                addWatchlist.setText(string.added_watchlist);
                addWatchlist.setBackgroundColor(color.colorPrimary);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void response) {
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
