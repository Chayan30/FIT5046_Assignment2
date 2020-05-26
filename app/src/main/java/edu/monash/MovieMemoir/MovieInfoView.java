package edu.monash.MovieMemoir;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import edu.monash.MovieMemoir.HttpRequests;
import edu.monash.MovieMemoir.R;
import edu.monash.MovieMemoir.ui.movieSearch.ListAdapter;

public class MovieInfoView extends AppCompatActivity {
    private View root;
    TextView text_genre;
    TextView text_cast;
    TextView text_rel_date;
    TextView text_country;
    TextView text_director;
    TextView text_plot;
    ImageView img_poster;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);
        text_genre = findViewById(R.id.genre_info);
        text_cast = findViewById(R.id.cast_info);
        text_rel_date = findViewById(R.id.rel_date_info);
        text_country = findViewById(R.id.country_info);
        text_director = findViewById(R.id.director_info);
        text_plot = findViewById(R.id.plot_info);
        img_poster = findViewById(R.id.imageView_poster);
        String title = getIntent().getStringExtra("title");
        getSupportActionBar().setTitle(title); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String request = "?t=" + title;
        getMovieInfo movieinfo = new getMovieInfo();
        movieinfo.execute(request);
    }
    private class getMovieInfo extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            return HttpRequests.httpMovieGetRequests(params[0]);
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            //String responseStatus = response.getString("status");
            //if(Integer.parseInt(responseStatus) == 200) {
            String genre = null;
            String ryear = null;
            String poster = null;
            String cast = null;
            String country = null;
            String director = null;
            String plot = null;
            try {
                genre = response.getString("Genre");
                ryear = response.getString("Year");
                poster = response.getString("Poster");
                cast = response.getString("Actors");
                country = response.getString("Country");
                director = response.getString("Director");
                plot = response.getString("Plot");
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
            Picasso.get()
                    .load(poster)
                    .placeholder(R.mipmap.ic_launcher)
                    .resize(200, 200)
                    .centerInside()
                    .into(img_poster);
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
