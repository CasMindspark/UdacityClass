package app.com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private String jsonCode;
    private ImageAdapter adapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        List<String> weekForecast = new ArrayList<String>();

        adapter = new ImageAdapter(
                getActivity(),
                weekForecast
        );

        GridView gridView = (GridView) rootView.findViewById(R.id.main_grid);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String imageURL = getStringFromJson(jsonCode, position, "poster_path");
                String title = getStringFromJson(jsonCode,position, "title");
                String overview = getStringFromJson(jsonCode, position, "overview");
                String releaseDate = getStringFromJson(jsonCode, position, "release_date");
                double userRating = getDoubleFromJson(jsonCode, position, "vote_average");

                Intent intent = new Intent(getActivity(), Detail_Activity.class)
                        .putExtra("IMAGE", imageURL)
                        .putExtra("TITLE", title)
                        .putExtra("OVERVIEW", overview)
                        .putExtra("RELEASE_DATE", releaseDate)
                        .putExtra("USER_RATING", userRating);
                startActivity(intent);
            }
        });
        updateData();

        return rootView;
    }

    private void updateData(){
        FetchDataClass dataClass = new FetchDataClass();
        String sortOrder = null;
        if(getArguments() != null) {
            sortOrder = getArguments().getString("SORT_ORDER");
        }else sortOrder = "popularity.desc";
        dataClass.execute(sortOrder);
    }

    private String[] getStringArrayFromJson(String json, String name){
        int resultLength = getJSONResults(json).length();
        String[] strArray = new String[resultLength];

        for(int i = 0; i < resultLength; i++){
            strArray[i] = getStringFromJson(json, i, name);
        }

        return strArray;
    }

    private String getStringFromJson(String json, int index, String name){
        String jsonStr = null;
        try {
            jsonStr = getJSONResults(json)
                    .getJSONObject(index)
                    .getString(name);
        }catch (JSONException e){
            return null;
        }
        if(jsonStr == "null" && name != "poster_path"){
            jsonStr = "Not avaliable.";
        }
        return jsonStr;
    }

    private double getDoubleFromJson(String json, int index, String name){
        double jsonInt = 0;

        try {
            jsonInt = getJSONResults(json)
                    .getJSONObject(index)
                    .getDouble(name);
        }catch (JSONException e){
            return 0;
        }
        return jsonInt;
    }

    private JSONArray getJSONResults(String json){
        final String JSON_RESULTS = "results";
        JSONArray results = null;

        try {
            JSONObject jsonObject = new JSONObject(json);
            results = jsonObject.getJSONArray(JSON_RESULTS);
        }catch (JSONException e){
            return null;
        }
        return results;
    }

    public class FetchDataClass extends AsyncTask<String, Void, String[]>{

        private final String API_KEY = "ee0c39177c39eebeffceca966fb4abde";
        private final String IMG_URL = "http://image.tmdb.org/t/p/w185/";
        private final String JSON_PATH_ITEM = "poster_path";

        @Override
        protected String[] doInBackground(String... params){
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonDataStr = null;
            try{
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("discover")
                        .appendPath("movie")
                        .appendQueryParameter("sort_by", params[0])
                        .appendQueryParameter("api_key", API_KEY);

                String myURL = builder.build().toString();
                URL url = new URL(myURL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if(inputStream == null){
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null){
                    String s = line + "\n";
                    buffer.append(s);
                }
                if(buffer.length() == 0){
                    return null;
                }
                jsonDataStr = buffer.toString();
            }catch (IOException e){
                return null;
            }finally {
                if(urlConnection == null){
                    urlConnection.disconnect();
                }
                if(reader != null){
                    try {
                        reader.close();
                    }catch (final IOException e){
                        return null;
                    }
                }
            }
            jsonCode = jsonDataStr;
            return getStringArrayFromJson(jsonCode, JSON_PATH_ITEM);
        }

        @Override
        protected void onPostExecute(String[] result){
            if(result != null){
                adapter.clear();
                for(String dataStr : result) {
                    String imgURL = dataStr;
                    if(dataStr != "null") {
                        imgURL = IMG_URL + dataStr;
                    }
                    adapter.add(imgURL);
                }
            }
        }
    }
}