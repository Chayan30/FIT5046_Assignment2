package edu.monash.MovieMemoir.ui.watchlist;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.monash.MovieMemoir.HttpRequests;
import edu.monash.MovieMemoir.MovieInfoView;
import edu.monash.MovieMemoir.R;
import edu.monash.MovieMemoir.database.WatchlistDatabase;
import edu.monash.MovieMemoir.entity.Watchlist;
import edu.monash.MovieMemoir.ui.movieSearch.ItemAdapter;
import edu.monash.MovieMemoir.ui.watchlist.ListAdapter;

public class WatchlistFragment extends Fragment implements ListAdapter.RecyclerViewClickListener{
    private WatchlistDatabase watchlistDb = null;
    private WatchlistViewModel watchlistViewModel;
    private RecyclerView movieList;
    private List<ItemAdapter> mList = new ArrayList<>();
    private ListAdapter mAdapter;
    private View root;
    private Button deleteButton;
    private Button viewButton;
    private Boolean clear;
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_watchlist, container, false);
        clear = false;
//        watchlistViewModel =
//                ViewModelProviders.of(this).get(WatchlistViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_watchlist, container, false);
//        final TextView textView = root.findViewById(R.id.text_slideshow);
//        watchlistViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        watchlistDb = WatchlistDatabase.getInstance(getActivity());
        movieList = root.findViewById(R.id.watchlist_list);
        movieList.setLayoutManager(new LinearLayoutManager(getActivity()));
        deleteButton = root.findViewById(R.id.delete_button);
        viewButton = root.findViewById(R.id.view_button);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteWatchList asyncTask = new deleteWatchList();
                asyncTask.execute();
            }
        });
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewWatchList asyncTask = new viewWatchList();
                asyncTask.execute();
            }
        });
        getWatchlist asyncTask = new getWatchlist();
        asyncTask.execute();
        return root;
    }

    @Override
    public void onItemClick(int item) {
        ItemAdapter item1 = mList.get(item);
    }

    @SuppressLint("StaticFieldLeak")
    private class getWatchlist extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground (Void...params){

            return null;
        }
        @Override
        protected void onPostExecute(Void params){
            watchlistDb.watchlistDao().getAll().observe(getActivity(), new Observer<List<Watchlist>>() {
                @Override
                public void onChanged(List<Watchlist> watchlists) {
                    if(watchlists != null) {
                        String mname = null;
                        String uadddate = null;
                        String rdate = null;
                        for (int i = 0; i < watchlists.size(); i++) {
                            HashMap<String, String> map = new HashMap<>();
                            Watchlist temp = watchlists.get(i);
                            mname = temp.getId();
                            uadddate = temp.getAdd_date();
                            rdate = temp.getRel_date();
                            map.put("Movie Name", mname);
                            map.put("User Add Date", uadddate);
                            map.put("Release Date", rdate);
                            addList(mname,rdate,uadddate);
                        }
                        adapter();
                    }
                }
            });
        }
    }
    private void addList(String movieName, String releaseYear, String poster){
        Log.d("addList", "Added");
        ItemAdapter itemAdapter = new ItemAdapter();
        itemAdapter.setImage(poster);
        itemAdapter.setText(movieName);
        itemAdapter.setText2(releaseYear);
        mList.add(itemAdapter);
    }
    private void adapter(){
        Log.d("adapter", "Added");
        mAdapter = new ListAdapter(mList, getActivity(), this);
        movieList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
    @SuppressLint("StaticFieldLeak")
    private class deleteWatchList extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground (Void...params){
            clear = true;
            for (ItemAdapter item : mList) {
                if (item.isSelected()) {
                    watchlistDb.watchlistDao().deleteById(item.getText());
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void params)
        {
            if (clear)
                mAdapter.clear();
        }
    }
    @SuppressLint("StaticFieldLeak")
    private class viewWatchList extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground (Void...params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void params)
        {
            String title = null;
            int selected = 0;
            for (ItemAdapter item : mList) {
                if (item.isSelected()) {
                    selected++;
                    title = item.getText();
                }
            }
            if(selected == 1)
            {
                Intent intent = new Intent(getActivity(), MovieInfoView.class);
                intent.putExtra("title", title);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(root.getContext(), "Please Select only 1 item to view", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
