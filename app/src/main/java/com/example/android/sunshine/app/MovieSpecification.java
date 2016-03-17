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


    public String getId() {
        return id;
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

    public MovieSpecification(String id,String title, String posterPath, String synopsis, String rating, String releaseDate) {
        this.title = title;
        this.posterPath = "http://image.tmdb.org/t/p/w185/"+posterPath;
        this.synopsis = synopsis;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.id = id;

    }

    @Override
    public String toString() {
        return "MovieSpecification{" +
                "id='" + id + '\'' +
                "title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", rating='" + rating + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }

    public String getPosterPath() {
        return posterPath;
    }

}
