package com.example.android.quakereport;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.quakereport.EarthquakeActivity;
import com.example.android.quakereport.Specification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /** Sample JSON response for a USGS query */
    //private static final String SAMPLE_JSON_RESPONSE = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }
    public static List<Specification> fetchEarthquakeData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<Specification> earthquakes = extractEarthquakes(jsonResponse);

        // Return the list of {@link Earthquake}s
        return earthquakes;
    }


    private static URL createUrl(String StringUrl){
        URL url=null;
        try{
            url=new URL(StringUrl);
        }catch (MalformedURLException exception){
            Log.e(LOG_TAG,"Error with creating URL",exception);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if(url==null)
            return jsonResponse;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if(urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else{
                Log.e(LOG_TAG,"ERROR RESOPNSE CODE: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results", e);
            // TODO: Handle the exception
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<Specification> extractEarthquakes(String earthquakeJSON) {

        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }
        List<Specification> earthquakes = new ArrayList<>();
        try {
            JSONObject jObject = new JSONObject(earthquakeJSON);

            JSONArray jArray= jObject.getJSONArray("features");
            for(int i=0;i< jArray.length();i++){
                JSONObject currentEarthquake=jArray.getJSONObject(i);
                JSONObject properties=currentEarthquake.getJSONObject("properties");
                Double  mag=properties.getDouble("mag");
                String place=properties.getString("place");
                long time=properties.getLong("time");
                String url=properties.getString("url");
                Specification earthquake=new Specification(mag,place,time,url);
                earthquakes.add(earthquake);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }


    /**
     * Return a list of {@link Specification} objects that has been built up from
     * parsing a JSON response.
     */
   /* public static ArrayList<Specification> extractEarthquakes() {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Specification> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject jObject = new JSONObject(SAMPLE_JSON_RESPONSE);
            
            JSONArray jArray= jObject.getJSONArray("features");
            for(int i=0;i< jArray.length();i++){
                JSONObject currentEarthquake=jArray.getJSONObject(i);
                JSONObject properties=currentEarthquake.getJSONObject("properties");
                Double  mag=properties.getDouble("mag");
                String place=properties.getString("place");
                long time=properties.getLong("time");
                String url=properties.getString("url");
                Specification earthquake=new Specification(mag,place,time,url);
                earthquakes.add(earthquake);

            }

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }*/
    }

