package com.project.utils;

import com.project.constants.Constants;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DeleteFacesRequest;
import software.amazon.awssdk.services.rekognition.model.RekognitionException;

public class DeleteFacesFromCollection
{
    public static void main(String[] args)
    {
        String collectionId = Constants.collectionId;
        String faceId = "1dd85191-4b06-4776-8213-142e00d03234";
        Region region = Region.AP_SOUTH_1;
        RekognitionClient rekClient = RekognitionClient.builder()
                .region(region)
                .build();

        System.out.println("Deleting collection: " + collectionId);
        deleteFacesCollection(rekClient, collectionId, faceId);
        rekClient.close();
    }

    public static void deleteFacesCollection(RekognitionClient rekClient,
                                             String collectionId,
                                             String faceId)
    {

        try
        {
            DeleteFacesRequest deleteFacesRequest = DeleteFacesRequest.builder()
                    .collectionId(collectionId)
                    .faceIds(faceId)
                    .build();

            rekClient.deleteFaces(deleteFacesRequest);
            System.out.println("The face was deleted from the collection.");

        }

        catch (RekognitionException e)
        {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
