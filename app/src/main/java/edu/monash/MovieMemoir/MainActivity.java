package edu.monash.MovieMemoir;


import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;



import com.google.android.material.textfield.TextInputLayout;
import com.google.common.hash.Hashing;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.List;

import edu.monash.MovieMemoir.database.PersonDatabase;
import edu.monash.MovieMemoir.entity.Person;

public class MainActivity extends AppCompatActivity {
    PersonDatabase personDb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        personDb = PersonDatabase.getInstance(this);
        CheckUserLoginAsyncTask task = new CheckUserLoginAsyncTask();

        task.execute();
        final TextInputLayout username = findViewById(R.id.outlinedTextField);
        final EditText username1 = username.getEditText();
        final TextInputLayout Password = findViewById(R.id.outlinedPasswordTextField);
        final EditText password = Password.getEditText();
        Button signUpButton = (Button)findViewById(R.id.signUpButton);
        Button signInButton = (Button)findViewById(R.id.signInButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Register.class));
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resource = "credential";
                String methodPath = "userAuthentication/";
                String param = resource + " " + methodPath;
                JSONObject loginPostDataObject = new JSONObject();
                JSONObject loginData = new JSONObject();
                String email = String.valueOf(username1.getText());
                Log.d("username", email);
                String unhashed_password = password.getText().toString();
                Log.d("unhashPassword", unhashed_password);
                String password1 = Hashing.sha256()
                        .hashString(unhashed_password, StandardCharsets.UTF_8)
                        .toString();
                Log.d("hashPassword", password1);
                try{
                    loginData.put("email", email);
                    loginData.put("password", password1);
                    loginPostDataObject.put("resource", resource);
                    loginPostDataObject.put("methodName", methodPath);
                    loginPostDataObject.put("data", loginData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                LoginAsyncTask addAsyncTask = new LoginAsyncTask();

                addAsyncTask.execute(loginPostDataObject);

            }
        });
    }
    private class LoginAsyncTask extends AsyncTask<JSONObject, Void, JSONObject>
    {
        @Override
        protected JSONObject doInBackground (JSONObject...params){
            return HttpRequests.httpPostRequest(params[0]);
        }
        @Override
        protected void onPostExecute (JSONObject response){
            handlePostLoginOperations(response);
        }
    }
    protected void handlePostLoginOperations(JSONObject response){
        try{
            String responseStatus = response.getString("status");
            Log.d("responseStatus", responseStatus);
            if(Integer.parseInt(responseStatus) == 200){
                AddDataInRoomPostLoginAsyncTask add = new AddDataInRoomPostLoginAsyncTask();
                Log.d("handlePostLogin", "handlePostLoginOperations");
                add.execute(response.getJSONObject("data"));
            }
            else{
                String errorMessage = response.getString("message");
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            Log.e("Exception", "handlePostLogin " + e.toString());
        }
    }

    private class AddDataInRoomPostLoginAsyncTask extends AsyncTask<JSONObject, Void, String> {
        @Override
        protected String doInBackground(JSONObject... objectArray) {
            try {
                Log.d("NOW", "AddDataInRoomPostLoginAsyncTask");
                JSONObject object = objectArray[0];
                String firstName = object.getString("firstName");
                String email = object.getString("username");
                String surname = object.getString("surname");
                String gender = object.getString("gender");
                String address = object.getString("address");
                String state = object.getString("state");
                int postcode = object.getInt("postcode");
                String id = object.getString("id");
                Person user = new Person(id, firstName, surname, email, gender, address, state, postcode);
                long userId = personDb.personDao().insert(user);
                Log.d("userId", String.valueOf(userId));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Exception", "AddDataInRoomPostLoginAsyncTask " + e.toString());
            }
            return "done";
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("HEYA", "moveToHomeAfterLogin");
            moveToHomeAfterLogin();
        }
    }
    protected void moveToHomeAfterLogin(){
        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity.this, Home.class));
        finish();
    }

    @SuppressLint("StaticFieldLeak")
    private class CheckUserLoginAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... objectArray) {
            String id = "";
            try {
                List<Person> p = personDb.personDao().getAll();
                if (!p.isEmpty()) {
                    for (Person per : p) {
                        id = per.getId();
                        //Log.d("id", id);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return id;
        }
        @Override
        protected void onPostExecute(String s) {
            if (!s.isEmpty()) {
                String action = getIntent().getAction();
                String mname = getIntent().getStringExtra("Title");
                Intent home_intent = new Intent(MainActivity.this, Home.class);
                if(action != null)
                {
                    home_intent.setAction(action);
                    home_intent.putExtra("Title", mname);
                }
                startActivity(home_intent);
                finish();
            }
        }
    }

}
