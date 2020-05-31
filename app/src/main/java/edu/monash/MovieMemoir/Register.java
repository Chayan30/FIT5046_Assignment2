package edu.monash.MovieMemoir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.common.hash.Hashing;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.monash.MovieMemoir.R;

public class Register extends AppCompatActivity {
    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
    public boolean validatePassword(String password){
        Log.d("validatePassowrd", password);
        boolean doesMatch;
        Pattern pattern;
        Matcher matcher;
        //final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        final String PASSWORD_PATTERN = "(^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@!#$%^&+=])(?=\\S+$).{6,}$)";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        return pattern.matcher(password).matches();
    }
    EditText firstName;
    private String email;
    String password1;
    String firstname;
    String surname1;
    String address1;
    String gender;
    String state;
    String dateOfBirth;
    int postcode1;
    EditText surname;
    EditText username;
    EditText password;
    RadioGroup genderGroup;
    EditText address;
    EditText postcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Register User"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final TextInputLayout FistName = findViewById(R.id.FistNameTextField);
        firstName = FistName.getEditText();
        final TextInputLayout SurName = findViewById(R.id.SurNameTextField);
        surname = SurName.getEditText();
        final TextInputLayout Username = findViewById(R.id.UserNameTextField);
        username = Username.getEditText();
        final TextInputLayout Password = findViewById(R.id.PasswordTextField);
        password = Password.getEditText();
        final TextInputLayout Address = findViewById(R.id.AddressTextField);
        address = Address.getEditText();
        genderGroup = findViewById(R.id.GenderRadioGroup);
        final Spinner stateSpinner = findViewById(R.id.spinnerState);
        final TextInputLayout Postcode = findViewById(R.id.PostcodeTextField);
        postcode = Postcode.getEditText();
        final DatePicker dobDatepicker = findViewById(R.id.datePickerDob);
        dobDatepicker.setMaxDate(new Date().getTime());
        final Button register = findViewById(R.id.buttonRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resource = "credential";
                String methodPath = "userRegistration/";
                JSONObject registerPostDataObject = new JSONObject();
                JSONObject registerData = new JSONObject();
                email = String.valueOf(username.getText());
                String unhashed_password = password.getText().toString();
                password1 = Hashing.sha256()
                        .hashString(unhashed_password, StandardCharsets.UTF_8)
                        .toString();
                firstname = firstName.getText().toString();
                surname1 = surname.getText().toString();
                address1 = address.getText().toString();
                int genderid = genderGroup.getCheckedRadioButtonId();
                gender = ((RadioButton)findViewById(genderid)).getText().toString();
                postcode1 = Integer.parseInt(postcode.getText().toString());
                state = stateSpinner.getSelectedItem().toString();
                int day = dobDatepicker.getDayOfMonth();
                int month = dobDatepicker.getMonth() + 1;
                int year = dobDatepicker.getYear();
                dateOfBirth = ""+day+"-"+month+"-"+year;
                boolean error = checkDataEntered();
                try{
                    registerData.put("email", email);
                    registerData.put("password", password1);
                    registerPostDataObject.put("resource", resource);
                    registerPostDataObject.put("methodName", methodPath);
                    registerPostDataObject.put("data", registerData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(error == false) {
                    Register.RegisterAsyncTask addAsyncTask = new Register.RegisterAsyncTask();
                    addAsyncTask.execute(registerPostDataObject);
                }
            }
        });
    }
    private class RegisterAsyncTask extends AsyncTask<JSONObject, Void, JSONObject>
    {
        @Override
        protected JSONObject doInBackground (JSONObject...params){
            return HttpRequests.httpPostRequest(params[0]);
        }
        @Override
        protected void onPostExecute (JSONObject response){
            handlePostRegistrationOperations(response);
        }
    }
    protected void handlePostRegistrationOperations(JSONObject response){
        try{
            String responseStatus = response.getString("status");

            Log.d("responseStatus", responseStatus);
            if(Integer.parseInt(responseStatus) == 200){
                String credid = response.getString("credid");
                JSONObject registerData = new JSONObject();
                JSONObject registerPostDataObject = new JSONObject();
                String resource = "person";
                String methodPath = "userRegistration/";
                try{
                    registerData.put("credid", credid);
                    registerData.put("firstname", firstname);
                    registerData.put("surname", surname1);
                    registerData.put("address", address1);
                    registerData.put("gender", gender);
                    registerData.put("postcode", postcode1);
                    registerData.put("state", state);
                    registerData.put("dob", dateOfBirth);
                    registerPostDataObject.put("resource", resource);
                    registerPostDataObject.put("methodName", methodPath);
                    registerPostDataObject.put("data", registerData);
                    Register.RegisterAsyncTask2 addAsyncTask =  new Register.RegisterAsyncTask2();
                    addAsyncTask.execute(registerPostDataObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else{
                String errorMessage = response.getString("message");
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            Log.e("Exception", "handlePostLogin " + e.toString());
        }
    }
    private class RegisterAsyncTask2 extends AsyncTask<JSONObject, Void, JSONObject>
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
                    startActivity(new Intent(Register.this, MainActivity.class));
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
    boolean checkDataEntered()
    {
        boolean error = false;
        if (isEmpty(firstName)) {
            //Toast t = Toast.makeText(getApplicationContext(), "You must enter first name to register!", Toast.LENGTH_SHORT);
            //t.show();
            firstName.setError("First Name is Required");
            firstName.requestFocus();
            error = true;
        }
        if (isEmpty(surname)) {
            surname.setError("Surname is required!");
            surname.requestFocus();
            error = true;
        }
        if (!isEmail(username)) {
            username.setError("Enter valid email!");
            username.requestFocus();
            error = true;
        }
        if (isEmpty(password)) {
            password.setError("Password cannot be empty");
            password.requestFocus();
            error = true;
        } else if (password.getText().length() < 6) {
            password.setError("Password too short, at least 6");
            password.requestFocus();
            error = true;
        } else  if(!(validatePassword(password.getText().toString().trim()))){
            password.setError("Password Must Contain a lowercase, uppercase, Numeric and a special character");
            password.requestFocus();
            error = true;
        }
        if (genderGroup.getCheckedRadioButtonId() == -1)
        {
            // no radio buttons are checked
            Toast.makeText(getApplicationContext(), "Please select Gender", Toast.LENGTH_SHORT).show();
            genderGroup.requestFocus();
            error = true;
        }
        if(isEmpty(address))
        {
            address.setError("Address is Required");
            address.requestFocus();
            error = true;
        }
        if(isEmpty(postcode))
        {
            postcode.setError("Postcode is Required");
            postcode.requestFocus();
            error = true;
        }
    return error;

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
