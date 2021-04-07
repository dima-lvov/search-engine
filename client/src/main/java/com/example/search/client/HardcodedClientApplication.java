package com.example.search.client;

import com.example.search.api.CreateDocumentRequestDto;
import com.example.search.api.GetDocumentResponseDto;
import com.example.search.api.SearchResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class HardcodedClientApplication {

    public static void main(String[] args) throws Exception {
        DocumentApiClient client = getSearchDocumentClient();

        System.out.println("Star calling Search Engine API...");

        System.out.println("\n################################################################################\n");

        System.out.println("GET: '/documents/notExistingKey'");
        Response<GetDocumentResponseDto> notExistingKeyResponse =
                client.getDocumentContentByKey("notExistingKey").execute();
        System.out.println("Response Code: " + notExistingKeyResponse.code());
        System.out.println("Content: " + notExistingKeyResponse.body());

        System.out.println("\n################################################################################\n");

        System.out.println("POST: '/documents' {'documentKey': 'key1', 'content': 'token1 token2 token3'}");
        System.out.println("Response Code: " + client.createNewDocument(
                new CreateDocumentRequestDto("key1", "token1 token2 token3")).execute().code());

        System.out.println("\n################################################################################\n");

        System.out.println("Trying to create document with the same key...");
        System.out.println("POST: '/documents' {'documentKey': 'key1', 'content': 'token1 token2 token3'}");
        Response<Void> duplicatedKeyCreationResponse = client.createNewDocument(
                new CreateDocumentRequestDto("key1", "token1 token2 token3")).execute();
        System.out.println("Response Code: " + duplicatedKeyCreationResponse.code());
        System.out.println("Content: " + duplicatedKeyCreationResponse.errorBody().string());

        System.out.println("\n################################################################################\n");

        System.out.println("POST: '/documents' {'documentKey': 'key2', 'content': 'token2 token3 token4'}");
        System.out.println("Response Code: " + client.createNewDocument(
                new CreateDocumentRequestDto("key2", "token2 token3 token4")).execute().code());

        System.out.println("\n################################################################################\n");

        System.out.println("POST: '/documents' {'documentKey': 'key3', 'content': 'token5 token7'}");
        System.out.println("Response Code: " + client.createNewDocument(
                new CreateDocumentRequestDto("key3", "token5 token7")).execute().code());

        System.out.println("\n################################################################################\n");

        System.out.println("GET: '/documents/key1'");
        Response<GetDocumentResponseDto> response = client.getDocumentContentByKey("key1").execute();
        System.out.println("Response Code: " + response.code());
        System.out.println("Content: " + response.body().getContent());

        System.out.println("\n################################################################################\n");

        System.out.println("GET: '/documents?token=token2,token3'");
        Response<SearchResponseDto> searchResponse = client.getDocumentsByTokens("token2", "token3").execute();
        System.out.println("Response Code: " + response.code());
        System.out.println("Content: " + searchResponse.body().getDocumentKeys().toString());

        System.out.println("\n################################################################################\n");

        System.out.println("GET: '/documents?token=notExistingKey'");
        Response<SearchResponseDto> searchResponseNotExistingKey =
                client.getDocumentsByTokens("notExistingKey").execute();
        System.out.println("Response Code: " + searchResponseNotExistingKey.code());
        System.out.println("Content: " + searchResponseNotExistingKey.body().getDocumentKeys().toString());

        System.out.println("\n#################### END OF DEMO #################################\n");
    }

    private static DocumentApiClient getSearchDocumentClient() {
        String baseUrl = "http://localhost:8088/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(new OkHttpClient().newBuilder().build())
                .addConverterFactory(JacksonConverterFactory.create(new ObjectMapper()))
                .build();
        return retrofit.create(DocumentApiClient.class);
    }

}
