package edu.monash.fit_tut2;

import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HttpRequests {
    private static final String BASE_URL = "http://192.168.1.10:8080/MyMovieMemoir/webresources";
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
    public static String httpGetRequest(String resource, String methodPath) {
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //Making HTTP request
        try {
            url = new URL(BASE_URL + credential + methodPath);
            Log.d("URL", url.toString());
            conn = HttpRequests.makeHttpConnection(url);
            conn.setRequestMethod("GET");
            Scanner inStream = new Scanner(conn.getInputStream());
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return textResult;
    }

}
