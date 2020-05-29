package edu.monash.MovieMemoir;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import edu.monash.MovieMemoir.ui.movieSearch.ItemAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class MemoirFragment extends Fragment implements MemoirListAdapter.RecyclerViewClickListener{
    private RecyclerView mRecycleview;
    private List<MemoirItemAdapter> mList = new ArrayList<>();
    private List<String> IdList;
    private MemoirListAdapter mAdapter;
    private View root;
    private Boolean clear;
    private Button sortButton;
    public MemoirFragment() {
        // Required empty public constructor
    }
    private ProgressBar progressBar;
    private String mname = null;
    private String releaseDate = null;
    private String postcode = null;
    private String useropinion = null;
    private float userrating = 0;
    private String userwatchdate = null;
    private String personId;
    String genre = null;
    String poster = null;
    private float publicrating = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_memoir, container, false);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("PersonId", Context.MODE_PRIVATE);
        personId = sharedPreferences.getString("pid","");
        init();
        getUserMemoirs getasyncTask = new getUserMemoirs();
        getasyncTask.execute("findByPersonId/"+personId);
        sortButton = root.findViewById(R.id.button_sort);
        progressBar = root.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        final Spinner sortSpinner = root.findViewById(R.id.spinner_sort);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortSpinner.getSelectedItemPosition() == 0) {
                    Collections.sort(mList, new Comparator<MemoirItemAdapter>() {
                        @Override
                        public int compare(MemoirItemAdapter lhs, MemoirItemAdapter rhs) {
                            // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                            return Float.compare(rhs.getUserRating(), lhs.getUserRating());
                        }

                    });
                    progressBar.setVisibility(View.VISIBLE);
                    adapter();
                }
                else if (sortSpinner.getSelectedItemPosition() == 1) {
                    Collections.sort(mList, new Comparator<MemoirItemAdapter>() {
                        @Override
                        public int compare(MemoirItemAdapter lhs, MemoirItemAdapter rhs) {
                            // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                            return Float.compare(rhs.getPublicRating(), lhs.getPublicRating());
                        }

                    });
                    progressBar.setVisibility(View.VISIBLE);
                    adapter();
                }
                else if (sortSpinner.getSelectedItemPosition() == 2) {
                    Collections.sort(mList, new Comparator<MemoirItemAdapter>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public int compare(MemoirItemAdapter lhs, MemoirItemAdapter rhs) {
                            // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                            LocalDateTime lhsDate = LocalDateTime.parse(lhs.getUaddDate());
                            LocalDateTime  rhsDate = LocalDateTime.parse(rhs.getUaddDate());
                            return lhsDate.compareTo(rhsDate) > 0 ? -1 : (rhsDate.compareTo(lhsDate) > 0 ) ? 1 : 0;
                        }

                    });
                    progressBar.setVisibility(View.VISIBLE);
                    adapter();
                }
            }
        });
        return root;

    }
    private class getUserMemoirs extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground (String...params){
            JSONArray response =  HttpRequests.httpGetRequest("memoir",params[0]);
            if(response != null) {

                IdList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {

                    JSONObject temp = null;
                    try {
                        temp = response.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        mname = temp.getString("moviename");
                        JSONObject cid = temp.getJSONObject("cinemaid");
                        releaseDate = temp.getString("releasedate");
                        releaseDate = releaseDate.substring(0,Math.min(releaseDate.length(),10));
                        postcode = cid.getString("postcode");
                        useropinion = temp.getString("useropinion");
                        userrating = (float)temp.getDouble("userrating");
                        userwatchdate = temp.getString("userwatchdate");
                        userwatchdate = userwatchdate.substring(0, Math.min(userwatchdate.length(),19));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String request = "?t=" + mname;
                    JSONObject response1 = HttpRequests.httpMovieGetRequests(request);
                    String rating1 = null;
                    try {
                        genre = response1.getString("Genre");
                        poster = response1.getString("Poster");
                        rating1 = response1.getString("imdbRating");
                        String id = response1.getString("imdbID");
                        Log.d("id", id);
                        IdList.add(id);
                        publicrating = Float.parseFloat(rating1);
                        publicrating = getRating(publicrating);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    addList();
                }

            }
            return null;
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
        protected void onPostExecute (Void response){
            adapter();
        }
    }

    @Override
    public void onItemClick(int item) {
        MemoirItemAdapter item1 = mList.get(item);
        String id = IdList.get(item);
        String title = item1.getMname();
        Intent intent = new Intent(getActivity(), MovieInfoView.class);
        intent.putExtra("title", title);
        intent.putExtra("id", id);
        startActivity(intent);
    }
    private void init(){
        mRecycleview = root.findViewById(R.id.memoir_recycler);
        mRecycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
    private void addList(){
        MemoirItemAdapter itemAdapter = new MemoirItemAdapter();
        Log.d("memoirmname", mname);
        itemAdapter.setImage(poster);
        itemAdapter.setMname(mname);
        itemAdapter.setRyear(releaseDate);
        itemAdapter.setCpostcode(postcode);
        itemAdapter.setGenre(genre);
        itemAdapter.setPublicRating(publicrating);
        itemAdapter.setUaddDate(userwatchdate);
        itemAdapter.setUserRating(userrating);
        itemAdapter.setUopinion(useropinion);
        mList.add(itemAdapter);
    }
    private void adapter(){
        Log.d("Adapter", "Reached Memoir adapter");
        mAdapter = new MemoirListAdapter(mList, getActivity(), this);
        mRecycleview.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.INVISIBLE);
    }
}
