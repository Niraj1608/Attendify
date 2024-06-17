package com.project.utils;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.CreateCollectionRequest;
import software.amazon.awssdk.services.rekognition.model.CreateCollectionResponse;
import software.amazon.awssdk.services.rekognition.model.RekognitionException;

public class CreateCollection
{
    public static void main(String[] args)
    {

        String collectionId = "attendify";
        Region region = Region.AP_SOUTH_1;
        try (RekognitionClient client = RekognitionClient.builder()
                .region(region)
                .build())
        {
            System.out.println("Creating collection: " + collectionId);
            createMyCollection(client, collectionId);
        }

        catch (Exception e)
        {
            System.out.println("Exception: " + e);
        }
    }

    public static void createMyCollection(RekognitionClient client, String collectionId)
    {
        try
        {
            CreateCollectionRequest request = CreateCollectionRequest.builder()
                    .collectionId(collectionId)
                    .build();

            CreateCollectionResponse collectionResponse = client.createCollection(request);
            System.out.println("CollectionArn: " + collectionResponse.collectionArn());
            System.out.println("Status code: " + collectionResponse.statusCode().toString());
        }

        catch (RekognitionException e)
        {
            System.out.println("Exception: " + e);
            System.exit(1);
        }
    }
}
