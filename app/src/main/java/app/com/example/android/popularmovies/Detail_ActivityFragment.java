package app.com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class Detail_ActivityFragment extends Fragment {


    private final String IMG_URL = "http://image.tmdb.org/t/p/w300/";
    private String imgURL;
    private String title;
    private String overview;
    private String releaseDate;
    private double userRating;

    public Detail_ActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_, container, false);

        Intent intent = getActivity().getIntent();
        if(intent != null && intent.hasExtra("IMAGE") && intent.hasExtra("OVERVIEW")
                && intent.hasExtra("RELEASE_DATE") && intent.hasExtra("USER_RATING")){

            imgURL = intent.getStringExtra("IMAGE");
            title = intent.getStringExtra("TITLE");
            overview = intent.getStringExtra("OVERVIEW");
            releaseDate = intent.getStringExtra("RELEASE_DATE");
            userRating = intent.getDoubleExtra("USER_RATING", 0);

        }

        ImageView imageView = (ImageView) rootView.findViewById(R.id.detail_image_view);
        TextView titleView = (TextView) rootView.findViewById(R.id.detail_title);
        TextView overviewView = (TextView) rootView.findViewById(R.id.detail_overview);
        TextView releaseView = (TextView) rootView.findViewById(R.id.detail_release_date);
        TextView ratingView = (TextView) rootView.findViewById(R.id.detail_vote_average);
        titleView.setText(title);
        overviewView.setText(overview);
        releaseView.setText(releaseDate);
        ratingView.setText(Double.toString(userRating));

        Log.e("Detail_ActivityFragment", imgURL);

        if(!imgURL.equals("null")) {
            Picasso.with(getActivity())
                    .load(IMG_URL + imgURL)
                    .into(imageView);
        }else{
            imageView.setImageResource(R.drawable.w185);
        }
        return rootView;
    }
}
