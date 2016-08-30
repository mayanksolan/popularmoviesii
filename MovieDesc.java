package com.application.mays.popularmoviesii;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class MovieDesc extends AppCompatActivity {
    TextView movie, plot, voteAverage, releaseDate;
    ImageView movieImg;
    protected void onCreate(Bundle savedInstanceState) {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
        }
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_movie_page);
        movie = (TextView) findViewById(R.id.textView);
        movie.setText(getIntent().getStringExtra("Movie String"));
        movieImg = (ImageView) findViewById(R.id.imageView2);
        Picasso.with(MovieDesc.this).load(String.format("https://image.tmdb.org/t/p/w342/%s", getIntent().getStringExtra("Movie Poster Link"))).into(movieImg);
        plot = (TextView) findViewById(R.id.textView2);
        plot.setText(String.format("%s%s", getString(R.string.plot_label), getIntent().getStringExtra("Movie Plot")));
        voteAverage = (TextView) findViewById(R.id.textView3);
        voteAverage.setText(String.format("%s%s%s", getString(R.string.rating_label1), getIntent().getStringExtra("Movie Vote Average"), getString(R.string.rating_label2)));
        releaseDate = (TextView) findViewById(R.id.textView4);
        releaseDate.setText(String.format("%s%s", getString(R.string.release_label), getIntent().getStringExtra("Movie Release Date")));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

