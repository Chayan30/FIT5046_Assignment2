package edu.monash.MovieMemoir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class AddToMemoir extends AppCompatActivity {
    DatePicker userWatchDate;
    TimePicker userWatchTime;
    Spinner cinemaSpinner;
    EditText opinionEditText;
    RatingBar userRatingBar;
    Button addMemoirButton;
    String title;
    String rel_year;
    String user_watch_date;
    String opinion;
    float user_rating;
    String cinemaId;
    String personId;
    ArrayList<String> cinemaIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_memoir);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add to Memoir"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences sharedPreferences = getSharedPreferences("PersonId", Context.MODE_PRIVATE);
        personId = sharedPreferences.getString("pid","");
        Log.d("pid",personId);
        title = getIntent().getStringExtra("title");
        rel_year = getIntent().getStringExtra("ryear");
        String poster = getIntent().getStringExtra("poster");
        TextView mname = findViewById(R.id.movie_name_info_text);
        TextView relDate = findViewById(R.id.rel_date_info_text);
        ImageView img_poster = findViewById(R.id.imageView_add_memoir_poster);
        mname.setText(title);
        relDate.setText(rel_year);
        Picasso.get()
                .load(poster)
                .placeholder(R.mipmap.ic_launcher)
                .resize(200, 200)
                .centerInside()
                .into(img_poster);
        userWatchDate = findViewById(R.id.datePickerUserWatchDate);
        userWatchTime = findViewById(R.id.timePickerUserWatchDate);
        cinemaSpinner = findViewById(R.id.spinner_cinema_name);
        opinionEditText = findViewById(R.id.multi_line_text_opinion);
        userRatingBar = findViewById(R.id.userRatingBar);
        addMemoirButton = findViewById(R.id.button_add_to_memoir);
        opinionEditText.setInputType(InputType.TYPE_CLASS_TEXT |
                                        InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                                        InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        fillCinemaSpinner asyncTask = new fillCinemaSpinner();
        asyncTask.execute("");
        addMemoirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resource = "memoir";
                String methodPath = "memoirAddition/";
                JSONObject addToMemoirPostDataObject = new JSONObject();
                JSONObject memoirData = new JSONObject();
                user_rating = userRatingBar.getRating();
                String rating = String.valueOf(user_rating);
                opinion = opinionEditText.getText().toString();
                int cinemaPosition = cinemaSpinner.getSelectedItemPosition();
                cinemaId = cinemaIds.get(cinemaPosition);
                int day = userWatchDate.getDayOfMonth();
                int month = userWatchDate.getMonth() + 1;
                int year = userWatchDate.getYear();
                int hour = userWatchTime.getHour();
                int minutes = userWatchTime.getMinute();
                user_watch_date = ""+year+"-"+month+"-"+day+" "+hour+":"+minutes+":"+"00.000";
                try{
                    memoirData.put("title", title);
                    memoirData.put("rel_year", rel_year);
                    memoirData.put("user_rating", rating);
                    memoirData.put("opinion", opinion);
                    memoirData.put("cinemaId", cinemaId);
                    memoirData.put("user_watch_date", user_watch_date);
                    memoirData.put("personId", personId);
                    addToMemoirPostDataObject.put("resource", resource);
                    addToMemoirPostDataObject.put("methodName", methodPath);
                    addToMemoirPostDataObject.put("data", memoirData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                AddMemoirAsyncTask addAsyncTask =  new AddMemoirAsyncTask();

                addAsyncTask.execute(addToMemoirPostDataObject);
            }
        });
    }
    private class fillCinemaSpinner extends AsyncTask<String, Void, JSONArray>
    {
        @Override
        protected JSONArray doInBackground (String...params){
            return HttpRequests.httpGetRequest("cinema",params[0]);
        }
        @Override
        protected void onPostExecute (JSONArray response){
            String cname = null;
            String postcode = null;
            String cid = null;
            if(response != null) {
                ArrayList<String> arrayList = new ArrayList<>();
                cinemaIds = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {

                    JSONObject temp = null;
                    try {
                        temp = response.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        cname = temp.getString("name");
                        postcode = temp.getString("postcode");
                        cid = temp.getString("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    cinemaIds.add(cid);
                    arrayList.add(cname +" "+ postcode);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_spinner_item, arrayList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cinemaSpinner.setAdapter(arrayAdapter);
            }
        }
    }
    private class AddMemoirAsyncTask extends AsyncTask<JSONObject, Void, JSONObject>
    {
        @Override
        protected JSONObject doInBackground (JSONObject...params){
            return HttpRequests.httpPostRequest(params[0]);
        }
        @Override
        protected void onPostExecute (JSONObject response){
            try {
                String responseStatus = response.getString("status");
                if(Integer.parseInt(responseStatus) == 200) {
                    startActivity(new Intent(AddToMemoir.this, Home.class));
                }
                else{
                    String errorMessage = response.getString("message");
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
