package com.example.android.sunshine.app;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;


public class ForecastFragment extends Fragment implements FetchWeatherRequest {
    private static String LOG_TAG = ForecastFragment.class.getSimpleName();
    String apiUrl;

    private static String appId = "";
    // Will contain the raw JSON response as a string.
    String forecastJsonStr = null;
    private ArrayAdapter<String> mForecastAdapter;
    ArrayList<String> weekForecast;
    private ListView mListView;
    private static final String postcode="94043";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,
                                    MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            FetchWeatherTask request = new FetchWeatherTask();
            request.response = this;
            request.execute(postcode);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //=== Add fake data
        weekForecast = new ArrayList<>(Arrays.asList(
                "Today - Sunny - 88/63",
                "Tomorrow - Foggy - 70/40",
                "Weds - Cloudy - 72/63",
                "Thurs - Asteroids - 75/65",
                "Fri - Heavy Rain - 65/56",
                "Sat - HELP TRAPPED IN WEATHERSTATION - 60/51",
                "Sun - Sunny - 80/68")
        );

        mForecastAdapter = new ArrayAdapter<>(
                // The current context (this fragment parent activity)
                getActivity(),
                // ID of list item layout
                R.layout.list_item_forecast,
                // ID of the textview to populate
                R.id.list_item_forecast_textview,
                // Data
                weekForecast);

        mListView = (ListView)rootView.findViewById(R.id.listview_forecast);
        mListView.setAdapter(mForecastAdapter);

        // Get OpenWeather API key
        appId = getResources().getString(R.string.APPID);



        // Construct the URL for the OpenWeatherMap query
        // Possible parameters are avaiable at OWM's forecast API page, at
        // http://openweathermap.org/API#forecast
        apiUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7&APPID=" + appId;


        return rootView;
    }

    @Override
    public void requestDone(String jsonResponse) {
        forecastJsonStr = jsonResponse;
        Log.d(Constants.V_LOG_TAG + LOG_TAG,forecastJsonStr);
    }
}
