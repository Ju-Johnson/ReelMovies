package com.example.android.reelmovies;

/**
 * Created by Jujuan on 11/10/2017.
 * A {@link Movie} object contains specific details of a single movie.
 * The data is loaded from the internet database URL, passed through the constructor
 * and assigned to the respective attributes.
 */

public class Movie {

    private int movieId;
    private String movieTitle;
    private float movieRating;
    private boolean movieAdultTag;
    private String releaseDate;
    private String movieImage;


    public Movie(int id, String image, String title, float rating, boolean adultTag, String date){

        movieId = id;
        movieImage = image;
        movieTitle = title;
        movieRating = rating;
        movieAdultTag = adultTag;
        releaseDate = date;
    }

    public int getMovieId(){return movieId;}

    public String getMovieImage(){
        return movieImage;
    }

    public String getMovieTitle(){
        return movieTitle;
    }

    public float getMovieRating(){
        return movieRating/2;
    }


    public String getMovieAdultTag(){
        String adultRated = "";

        if(movieAdultTag == true){
            adultRated = "Rated - R";
        }else{
            adultRated = "Rated - PG";
        }
        return adultRated;
    }

    public String getReleaseDate(){

        String month = releaseDate.substring(5,7);
        String day = releaseDate.substring(8);
        String year = releaseDate.substring(0,4);
        String formattedDate = "";

        switch (month){
            case "01":
                month = "Jan ";
                break;
            case "02":
                month = "Feb ";
                break;
            case "03":
                month = "Mar ";
                break;
            case "04":
                month = "Apr ";
                break;
            case "05":
                month = "May ";
                break;
            case "06":
                month = "June ";
                break;
            case "07":
                month = "July ";
                break;
            case "08":
                month = "Aug ";
                break;
            case "09":
                month = "Sep ";
                break;
            case "10":
                month = "Oct ";
                break;
            case "11":
                month = "Nov ";
                break;
            case "12":
                month = "Dec ";
                break;
        }//end switch

        formattedDate = month + day + ", " + year;

        return formattedDate;
    }

}
