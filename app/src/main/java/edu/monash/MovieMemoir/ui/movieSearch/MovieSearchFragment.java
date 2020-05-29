package edu.monash.MovieMemoir.ui.movieSearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.monash.MovieMemoir.HttpRequests;
import edu.monash.MovieMemoir.MovieInfoView;
import edu.monash.MovieMemoir.R;

public class MovieSearchFragment extends Fragment implements ListAdapter.RecyclerViewClickListener {

    private MovieSearchViewModel movieSearchViewModel;
    private RecyclerView mRecycleview;
    private List<ItemAdapter> mList = new ArrayList<>();
    private List<String> idList;
    private ListAdapter mAdapter;
    private View root;
    private Boolean clear;
    private ProgressBar progressBarWaitMovieList;
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
//        movieSearchViewModel =
//                ViewModelProviders.of(this).get(MovieSearchViewModel.class);
        root = inflater.inflate(R.layout.fragment_moviesearch, container, false);
//        final TextView textView = root.findViewById(R.id.text_gallery);
//        movieSearchViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        init();
        clear = false;
        progressBarWaitMovieList = root.findViewById(R.id.progressBarWaitMovieList);
        final SearchView search = root.findViewById(R.id.search_movie);
        Button searchButton = root.findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarWaitMovieList.setVisibility(View.VISIBLE);
                if (clear)
                    mAdapter.clear();
                idList = new ArrayList<>();
                String name = search.getQuery().toString();
                if(!name.isEmpty()) {
                    showSoftwareKeyboard(false);
                    String sname = "?s=" + name;
                    getMovieList asynMovieList = new getMovieList();
                    asynMovieList.execute(sname);
                    clear = true;
                }
            }
        });

        return root;
    }
    protected void showSoftwareKeyboard(boolean showKeyboard){
        final Activity activity = getActivity();
        final InputMethodManager inputManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), showKeyboard ? InputMethodManager.SHOW_FORCED : InputMethodManager.HIDE_NOT_ALWAYS);
    }
//    @Override
//    public void onDetach() {
//        super.onDetach();
//
//        //hide keyboard when any fragment of this class has been detached
//        if()
//            showSoftwareKeyboard(false);
//    }

    @Override
    public void onItemClick(int item) {
        ItemAdapter item1 = mList.get(item);
        String id = idList.get(item);
        String title = item1.getText();
        Intent intent = new Intent(getActivity(), MovieInfoView.class);
        intent.putExtra("title", title);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private class getMovieList extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            return HttpRequests.httpMovieGetRequests(params[0]);
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            //String responseStatus = response.getString("status");
            //if(Integer.parseInt(responseStatus) == 200) {
            String mname = null;
            String ryear = null;
            String poster = null;
            String id = null;
            try {
                int numResults = response.getInt("totalResults");
                JSONArray searchResult = response.getJSONArray("Search");
                for (int i = 0; i < searchResult.length(); i++) {
                    JSONObject temp = null;
                    temp = searchResult.getJSONObject(i);
                    mname = temp.getString("Title");
                    ryear = temp.getString("Year");
                    poster = temp.getString("Poster");
                    id = temp.getString("imdbID");
                    Log.d("Title", mname);
                    Log.d("Year",ryear);
                    Log.d("Poster",poster);
                    addList(mname,ryear,poster);
                    idList.add(id);
                }
                adapter();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void init(){
        mRecycleview = root.findViewById(R.id.recyclerView);
        mRecycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
    private void addList(String movieName, String releaseYear, String poster){
        ItemAdapter itemAdapter = new ItemAdapter();
        itemAdapter.setImage(poster);
        itemAdapter.setText(movieName);
        itemAdapter.setText2(releaseYear);
        mList.add(itemAdapter);
    }
    private void adapter(){
        mAdapter = new ListAdapter(mList, getActivity(), this);
        mRecycleview.setAdapter(mAdapter);
        progressBarWaitMovieList.setVisibility(View.INVISIBLE);
        mAdapter.notifyDataSetChanged();
    }

}
