package com.example.sonarexperiment;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

interface APIInterface {

    @GET("/api/unknown")
    Call<MultipleResource> doGetListResources();

}