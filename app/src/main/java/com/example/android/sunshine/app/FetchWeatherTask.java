package com.example.android.sunshine.app;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class FetchWeatherTask extends AsyncTask<String, Integer, String> {
    private static String LOG_TAG = FetchWeatherTask.class.getSimpleName();
    private static final String MSG_FETCHING_DATA = "Fetching data...";
    private static final String METHOD_GET = "GET";

    // These two need to be declared outside the try/catch
    // so that they can be closed in the finally block.
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    // Will contain the raw JSON response as a string.
    String forecastJsonStr = null;
    public FetchWeatherRequest response = null;

    protected void onPreExecute() {
        Log.d(Constants.V_LOG_TAG + LOG_TAG, MSG_FETCHING_DATA);
    }

    protected String doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(METHOD_GET);
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            forecastJsonStr = buffer.toString();
        } catch (Exception e) {
            Log.e(LOG_TAG,"Error ",e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if(reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return forecastJsonStr;
    }

    protected void onPostExecute(String result) {
        response.requestDone(result);
    }
}
