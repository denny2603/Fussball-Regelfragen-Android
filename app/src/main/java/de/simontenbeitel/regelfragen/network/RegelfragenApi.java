package de.simontenbeitel.regelfragen.network;

import retrofit.http.GET;

/**
 * @author Simon Tenbeitel
 */
public interface RegelfragenApi {

    public String URL = "http://regelfragen.simon-tenbeitel.de/api";

    @GET("/questions")
    RegelfragenApiJsonObjects.QuestionResponse getQuestions();

}
