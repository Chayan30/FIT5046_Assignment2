package edu.monash.MovieMemoir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class AddNewCinema extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_cinema);
        getSupportActionBar().setTitle("Add Cinema"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final EditText cinema_name_text = findViewById(R.id.editText_cinema_name);
        final EditText cinema_postcode_text = findViewById(R.id.editText_cinema_postcode);
        Button add_cinema_button = findViewById(R.id.add_cinema_button);
        add_cinema_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resource = "cinema";
                String methodPath = "cinemaAddition/";
                String cinema_name = cinema_name_text.getText().toString();
                Boolean error = false;
                String postcode = cinema_postcode_text.getText().toString();
                int cinema_postcode = Integer.parseInt(cinema_postcode_text.getText().toString());
                JSONObject addCinemaPostDataObject = new JSONObject();
                JSONObject cinemaData = new JSONObject();
                if(cinema_name.isEmpty())
                {
                    cinema_name_text.setError("Cinema Name required");
                    cinema_name_text.requestFocus();
                    error = true;
                }
                if(postcode.isEmpty())
                {
                    cinema_postcode_text.setError("Cinema Postcode required");
                    cinema_postcode_text.requestFocus();
                    error = true;
                }
                if(!error)
                {
                    try {
                        cinemaData.put("cinema_name", cinema_name);
                        cinemaData.put("cinema_postcode", cinema_postcode);
                        addCinemaPostDataObject.put("resource", resource);
                        addCinemaPostDataObject.put("methodName", methodPath);
                        addCinemaPostDataObject.put("data", cinemaData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    CinemaAsyncTask asyncTask = new CinemaAsyncTask();
                    asyncTask.execute(addCinemaPostDataObject);
                }
            }
        });
    }
    private class CinemaAsyncTask extends AsyncTask<JSONObject, Void, JSONObject>
    {
        @Override
        protected JSONObject doInBackground (JSONObject...params){
            return HttpRequests.httpPostRequest(params[0]);
        }
        @Override
        protected void onPostExecute (JSONObject response){
            String responseStatus = null;
            try {
                responseStatus = response.getString("status");
                Log.d("responseStatus", responseStatus);
                if(Integer.parseInt(responseStatus) == 200) {
                    Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddNewCinema.this, AddToMemoir.class));
                }
                else
                {
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
