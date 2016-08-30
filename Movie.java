package com.application.mays.popularmoviesii;

class Movie {
    String title;
    String poster;
    String plot;
    String voteAverage;
    String releaseDate;

    Movie(String title, String poster, String plot, String voteAverage, String releaseDate) {
        this.title = title;
        this.poster = poster;
        this.plot = plot;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }
}
