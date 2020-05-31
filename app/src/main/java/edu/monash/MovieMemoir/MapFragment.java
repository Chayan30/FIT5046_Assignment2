package edu.monash.MovieMemoir;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import edu.monash.MovieMemoir.database.CinemaDatabase;
import edu.monash.MovieMemoir.database.PersonDatabase;
import edu.monash.MovieMemoir.entity.Cinema;
import edu.monash.MovieMemoir.entity.Person;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    public MapFragment() {
        // Required empty public constructor
    }

    View root;
    SupportMapFragment supportMapFragment;
    private LatLng currentlocation;
    private GoogleMap mMap;
    PersonDatabase personDatabase;
    CinemaDatabase cinemaDatabase;
    List<Cinema> cinemaList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_map, container, false);
        currentlocation = new LatLng(-37.8771, 145.04493);
        personDatabase = PersonDatabase.getInstance(getContext());
        cinemaDatabase = CinemaDatabase.getInstance(getContext());
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(supportMapFragment == null)
        {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            supportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map, supportMapFragment).commit();
        }
        supportMapFragment.getMapAsync(this);
        return root;
    }
    private class GetCoordinates extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... strings) {
            String address = " ";
            String postcode= " ";
            String state= " ";
            try {
                List<Person> p = personDatabase.personDao().getAll();
                if (!p.isEmpty()) {
                    for (Person per : p) {
                        address = per.getAddress();
                        postcode = String.valueOf(per.getPostcode());
                        state = per.getState();
                        //Log.d("id", id);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String url = address + " , "+postcode +" , "+ state + " , australia";
            String response = HttpRequests.getHTTPData(url);
            return response;
        }
        @Override
        protected void onPostExecute(String response) {
            String lat= "", lng= "";
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                Log.d("info", jsonObject.toString());
                lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lat").toString();
                lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lng").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            currentlocation = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            if(mMap != null) {
                mMap.addMarker(new MarkerOptions().position(currentlocation).title("My location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                float zoomLevel = (float) 10.0;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentlocation,
                        zoomLevel));
            }
            new GetCinemaCoordinates().execute();
        }
    }
    private class GetCinemaCoordinates extends AsyncTask<Void,Void,List<Cinema>> {

        @Override
        protected List<Cinema> doInBackground(Void... strings) {
            cinemaList = cinemaDatabase.cinemaDAO().getAll();
            return cinemaList;
        }
        @Override
        protected void onPostExecute(List<Cinema> cinemaList)
        {
            if(cinemaList != null) {
                for (int i = 0; i < cinemaList.size(); i++) {
                    Cinema cinema = cinemaList.get(i);
                    String cname = cinema.getCinemaName();
                    String lat = cinema.getLat();
                    String lng = cinema.getLng();
                    currentlocation = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                    if(mMap != null) {
                        mMap.addMarker(new MarkerOptions().position(currentlocation).title(cname));
                    }

                }
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        new GetCoordinates().execute();
    }
}
