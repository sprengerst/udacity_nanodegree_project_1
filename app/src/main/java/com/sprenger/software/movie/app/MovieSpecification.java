/*
 * Created by Stefan Sprenger
 */

package com.sprenger.software.movie.app;

import java.io.Serializable;

class MovieSpecification implements Serializable {

    private final String id;
    private final String title;
    private final String posterPath;
    private final String synopsis;
    private final double rating;
    private final String releaseDate;
    private final double popularity;

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
