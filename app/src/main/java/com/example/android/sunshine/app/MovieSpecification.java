package com.example.android.sunshine.app;

import java.io.Serializable;

public class MovieSpecification implements Serializable {

    private String id;
    private String title;
    private String posterPath;
    private String synopsis;
    private double rating;
    private String releaseDate;
    private double popularity;

    public MovieSpecification(String id, String title, String posterPath, String synopsis, double rating, String releaseDate, double popularity) {
        this.title = title;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.synopsis = synopsis;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "MovieSpecification{" +
                "id='" + id + '\'' +
                ", popularity='" + popularity + '\'' +
                ", rating='" + rating + '\'' +
                ", title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public double getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public double getPopularity() {
        return popularity;
    }
}
