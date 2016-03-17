package com.example.android.sunshine.app;

import java.io.Serializable;

/**
 *
 * Created by stefa on 17.03.2016.
 *
 */
public class MovieSpecification implements Serializable {

    private String id;
    private String title;
    private String posterPath;
    private String synopsis;
    private String rating;
    private String releaseDate;
    private String popularity;


    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "MovieSpecification{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", rating='" + rating + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", popularity='" + popularity + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public MovieSpecification(String id, String title, String posterPath, String synopsis, String rating, String releaseDate, String popularity) {
        this.title = title;
        this.popularity = popularity;
        this.posterPath = "http://image.tmdb.org/t/p/w185/"+posterPath;
        this.synopsis = synopsis;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.id = id;

    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getPopularity() {
        return popularity;
    }
}
