package com.example.search.client;

import com.example.search.api.CreateDocumentRequestDto;
import com.example.search.api.GetDocumentResponseDto;
import com.example.search.api.SearchResponseDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DocumentApiClient {

    @POST("documents")
    Call<Void> createNewDocument(@Body CreateDocumentRequestDto createDocumentRequestDto);

    @GET("documents/{documentKey}")
    Call<GetDocumentResponseDto> getDocumentContentByKey(@Path("documentKey") String documentKey);

    @GET("documents")
    Call<SearchResponseDto> getDocumentsByTokens(@Query("token") String... tokens);
}