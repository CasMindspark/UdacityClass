package app.com.example.android.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends ArrayAdapter<String> {

    private final String LOG_TAG = ImageAdapter.class.getSimpleName();

    public ImageAdapter(Activity context, List<String> urls){
        super(context, 0, urls);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        String url = getItem(position);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);

        ImageView img = (ImageView) rootView.findViewById(R.id.grid_view_item);

        img.setLayoutParams(
                new GridView.LayoutParams(
                        GridView.AUTO_FIT,
                        GridView.AUTO_FIT));

        if(!url.equals("null")) {
            Picasso.with(getContext())
                    .load(url)
                    .into(img);
        }else img.setImageResource(R.drawable.w185);
        return rootView;
    }
}