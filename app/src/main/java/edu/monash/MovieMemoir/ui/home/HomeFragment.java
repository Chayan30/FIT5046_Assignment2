package edu.monash.MovieMemoir.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import edu.monash.MovieMemoir.HttpRequests;
import edu.monash.MovieMemoir.MainActivity;
import edu.monash.MovieMemoir.R;
import edu.monash.MovieMemoir.Register;
import edu.monash.MovieMemoir.database.PersonDatabase;
import edu.monash.MovieMemoir.entity.Person;
import edu.monash.MovieMemoir.ui.watchlist.WatchlistFragment;

public class HomeFragment extends Fragment {

    //private HomeViewModel homeViewModel;
    PersonDatabase personDb = null;
    TextView welcomeText;
    String uid = "1";
    List<HashMap<String, String>> movieListArray;
    SimpleAdapter myListAdapter;
    ListView movieList;
    String[] colHEAD = new String[] {"Movie Name","User Rating","Release Date"};
    int[] dataCell = new int[] {R.id.MovieName,R.id.UserRating,R.id.ReleaseDate};

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel =
//                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        personDb = PersonDatabase.getInstance(getActivity());
        welcomeText = root.findViewById(R.id.text_home);
        movieList = root.findViewById(R.id.listView);
        movieListArray = new ArrayList<HashMap<String, String>>();
        TextView currentDate = root.findViewById(R.id.dateTextView);
        Date curDate = new Date();
        LocalDateTime ldt = LocalDateTime.now().plusDays(1);
        DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);
        currentDate.setText(formmat1.format(ldt));
        SetNameTextAsyncTask asyncTask = new SetNameTextAsyncTask();
        asyncTask.execute();
        String intentActionType = getActivity().getIntent().getAction();
        AlertDialog.Builder watchlistDialog;
        if (intentActionType != null) {
            switch (intentActionType) {
                case "OpenWatchList":
                    String movieName = getActivity().getIntent().getStringExtra("Title");
                    watchlistDialog = new AlertDialog.Builder(getActivity());
                    watchlistDialog.setTitle("Watchlist Alert");
                    watchlistDialog.setMessage("You added this movie 7 days ago. Want to check it out?");
                    watchlistDialog.setPositiveButton("Ignore", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    watchlistDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            WatchlistFragment watchlistFragment = new WatchlistFragment();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(getId(), watchlistFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    });
                    watchlistDialog.show();
            }
        }

//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
    private class SetNameTextAsyncTask extends AsyncTask<Void, Void, Void> {
        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... objectArray) {
            List<Person> p = personDb.personDao().getAll();
            Person p1 = p.get(0);
            String personName = p1.getFirstName();
            uid = p1.getId();
            welcomeText.setText("Hi "+personName);
            return null;
        }
        @Override
        protected void onPostExecute(Void s) {
            String methodName = "findByPersonIdandSameReleaseYearandUserWatchYear/"+uid;
            getMemoirUser asyncTask2 = new getMemoirUser();
            asyncTask2.execute(methodName);
        }
    }
    private class getMemoirUser extends AsyncTask<String, Void, JSONArray>
    {
        @Override
        protected JSONArray doInBackground (String...params){
            return HttpRequests.httpGetRequest("memoir", params[0]);
        }
        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute (JSONArray response){
            //String responseStatus = response.getString("status");
            //if(Integer.parseInt(responseStatus) == 200) {
            String mname = null;
            String urating = null;
            String rdate = null;
            if(response != null) {
                for (int i = 0; i < Integer.min(response.length(),5); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject temp = null;
                    try {
                        temp = response.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        mname = temp.getString("MovieName");
                        urating = temp.getString("UserRating");
                        rdate = temp.getString("ReleaseDate");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    map.put("Movie Name", mname);
                    map.put("User Rating", urating);
                    map.put("Release Date", rdate);
                    movieListArray.add(map);
                }
                myListAdapter = new
                        SimpleAdapter(getActivity(), movieListArray, R.layout.list_row, colHEAD, dataCell);
                movieList.setAdapter(myListAdapter);
            }

            //}
            //else{
            //String errorMessage = response.getString("message");
            //}
        }
    }
}
