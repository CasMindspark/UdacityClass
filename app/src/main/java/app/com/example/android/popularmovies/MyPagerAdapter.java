package app.com.example.android.popularmovies;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter{


    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();
        Bundle bundle = new Bundle();
        if(position == 0){
            bundle.putString("SORT_ORDER", "popularity.desc");
            fragment = new MainActivityFragment();
            fragment.setArguments(bundle);
        }
        if (position == 1){
            bundle.putString("SORT_ORDER", "vote_average.desc");
            fragment = new MainActivityFragment();
            fragment.setArguments(bundle);
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position){
        String title = null;
        if(position == 0){
            title = "Popularity";
        }
        if(position == 1){
            title = "Highest Rated";
        }
        return title;
    }

}
