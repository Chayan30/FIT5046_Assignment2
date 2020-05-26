package edu.monash.MovieMemoir;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpRequests {
    private static final String BASE_URL = "http://192.168.43.159:8080/MyMovieMemoir/webresources";
    private static final String credential = "/org.my.movie.memoir.credential/";
    private static final String person = "/org.my.movie.memoir.person/";
    private static final String memoir = "/org.my.movie.memoir.memoir/";
    private static final String cinema = "/org.my.movie.memoir.cinema/";
    private static HttpURLConnection makeHttpConnection(URL url){
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
        } catch (Exception e) {
        e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return conn;
    }
    private static String getResource(String resource)
    {
        String resourcePath = null;
        if (resource == "credential")
        {
            resourcePath = credential;
        }
        if ( resource == "person")
        {
            resourcePath = person;
        }
        if (resource == "memoir")
        {
            resourcePath = memoir;
        }
        if ( resource == "cinema")
        {
            resourcePath = cinema;
        }
        return resourcePath;
    }
    public static JSONArray httpGetRequest(String resource, String methodPath) {
        URL url = null;
        HttpURLConnection conn = null;
        JSONArray Response = null;
        //Making HTTP request
        try {
            String resourcePath = getResource(resource);
            url = new URL(BASE_URL + resourcePath + methodPath);
            Log.d("URL", url.toString());
            conn = HttpRequests.makeHttpConnection(url);
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            BufferedReader in;
            if(responseCode == HttpURLConnection.HTTP_OK) {
                in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
            }
            else
            {
                in  = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            Log.d("response", response.toString());
            Response = new JSONArray(response.toString());
            in.close();
        } catch (Exception e) {
            try {
                Response = new JSONArray("{}");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return Response;
    }
    public static JSONObject httpPostRequest(JSONObject params) {
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        JSONObject Response = null;
        //Making HTTP request
        try {
            String methodName = params.getString("methodName");
            String resource = params.getString("resource");
            String resourcePath = getResource(resource);
            JSONObject data = params.getJSONObject("data");
            url = new URL(BASE_URL + resourcePath + methodName);
            Log.d("URL", url.toString());
            conn = HttpRequests.makeHttpConnection(url);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            Gson gson = new Gson();
            String postDataJson = gson.toJson(data);
            Log.d("postDataJson", postDataJson);
            conn.setFixedLengthStreamingMode(postDataJson.getBytes().length);
            OutputStream out = conn.getOutputStream();
            out.write(postDataJson.getBytes());
            out.flush();
            out.close();
            int responseCode = conn.getResponseCode();
            BufferedReader in;
            if(responseCode == HttpURLConnection.HTTP_OK) {
                in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
            }
            else
            {
                in  = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            Log.d("response", response.toString());
            Response = new JSONObject(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return Response;
    }
    public static JSONObject httpMovieGetRequests(String path) {
        URL url = null;
        HttpURLConnection conn = null;
        JSONObject Response = null;
        String apikey = "&apikey=7769defa";

        try {
            url = new URL("http://www.omdbapi.com/" + path + apikey);

            Log.d("URL", url.toString());
            conn = HttpRequests.makeHttpConnection(url);

            conn.setRequestMethod("GET");

            int responseCode = 0;

            responseCode = conn.getResponseCode();

            BufferedReader in;
            if(responseCode == HttpURLConnection.HTTP_OK) {

                in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));

            }
            else
            {
                in  = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            Log.d("response", response.toString());
            Response = new JSONObject(response.toString());
            in.close();
        } catch (Exception e) {
            try {
                Response = new JSONObject("{}");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return Response;
    }

}
