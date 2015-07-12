package de.simontenbeitel.regelfragen.network;

import java.util.List;

import retrofit.http.GET;

/**
 * @author Simon Tenbeitel
 */
public interface RegelfragenApi {

    public String URL = "http://regelfragen.simon-tenbeitel.de/api";

    @GET("/questions")
    List<RegelfragenApiJsonObjects.Question> getQuestions();

}
