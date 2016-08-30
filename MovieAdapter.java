package com.application.mays.popularmoviesii;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class MovieAdapter extends BaseAdapter {
    ArrayList<Movie> list;
    LayoutInflater inflater;
    Context context;

    MovieAdapter(Context context, ArrayList movieList, ArrayList moviePosterLink, ArrayList plot, ArrayList voteAverage, ArrayList releaseDate) {
        list = new ArrayList<>();
        for (int i = 0; i < movieList.size(); i++) {
            list.add(new Movie(movieList.get(i).toString(), moviePosterLink.get(i).toString(), plot.get(i).toString(), voteAverage.get(i).toString(), releaseDate.get(i).toString()));
        }
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_grid_item, parent, false);
        }
        ImageView image = (ImageView) convertView.findViewById(R.id.imageView);
        Movie temp = list.get(position);
        Picasso.with(context).load("https://image.tmdb.org/t/p/w342/" + temp.poster).into(image);
        image.setContentDescription(temp.title);
        return convertView;
    }
}

