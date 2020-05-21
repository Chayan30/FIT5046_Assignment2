package edu.monash.fit_tut2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextInputEditText username = findViewById(R.id.usernameTextField);
        final TextInputEditText Password = findViewById(R.id.passwordTextField);
        CheckBox checkBox = findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {
                    Password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    Password.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        });
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
                String methodPath = "findById/1";
                String param = resource + " " + methodPath;
                //String result = HttpRequests.httpGetRequest(resource, methodPath);
                //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                //Log.d("Result", result);
                CoursesAsyncTask addAsyncTask = new CoursesAsyncTask();
                //String strAdd = username.getText().toString();
                //if (!strAdd.isEmpty()) {
                addAsyncTask.execute(param);

                //}

            }
        });
    }
    private class CoursesAsyncTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground (String...params){
            String[] details = params[0].split(" ");
            String result =  HttpRequests.httpGetRequest(details[0], details[1]);
            return result;
        }
        @Override
        protected void onPostExecute (String courses){
            Log.d("Info", courses);
        }
    }


}
