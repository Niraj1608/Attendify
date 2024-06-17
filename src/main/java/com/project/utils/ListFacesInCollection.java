package com.project.utils;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.Face;
import software.amazon.awssdk.services.rekognition.model.ListFacesRequest;
import software.amazon.awssdk.services.rekognition.model.ListFacesResponse;
import software.amazon.awssdk.services.rekognition.model.RekognitionException;
import java.util.List;
import com.project.constants.Constants;

public class ListFacesInCollection
{

    public static void main(String[] args)
    {

        String collectionId = Constants.collectionId;
        Region region = Region.AP_SOUTH_1;
        RekognitionClient rekClient = RekognitionClient.builder()
                .region(region)
                .build();

        System.out.println("Faces in collection " + collectionId);
        listFacesCollection(rekClient, collectionId) ;
        rekClient.close();
    }

    public static void listFacesCollection(RekognitionClient rekClient, String collectionId )
    {
        try
        {
            ListFacesRequest facesRequest = ListFacesRequest.builder()
                    .collectionId(collectionId)
                    .maxResults(10)
                    .build();

            ListFacesResponse facesResponse = rekClient.listFaces(facesRequest);
            List<Face> faces = facesResponse.faces();
            for (Face face: faces)
            {
                System.out.println("Confidence level there is a face: "+face.confidence());
                System.out.println("The face Id value is "+face.faceId());
            }

        }

        catch (RekognitionException e)
        {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
