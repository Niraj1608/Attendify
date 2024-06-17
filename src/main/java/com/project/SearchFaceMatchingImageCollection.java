package com.project;

import com.project.constants.Constants;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.RekognitionException;
import software.amazon.awssdk.services.rekognition.model.SearchFacesByImageRequest;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.SearchFacesByImageResponse;
import software.amazon.awssdk.services.rekognition.model.FaceMatch;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import com.project.constants.Constants;

public class SearchFaceMatchingImageCollection
{
    public static String search(String img)
    {

        String collectionId = Constants.collectionId;
        String sourceImage = img;
        Region region = Region.AP_SOUTH_1;

        RekognitionClient rekClient = RekognitionClient.builder()
                .region(region)
                .build();

        String result = searchFaceInCollection(rekClient, collectionId, sourceImage);
        rekClient.close();
        return result;
    }

    public static String searchFaceInCollection(RekognitionClient rekClient, String collectionId, String sourceImage)
    {
        try
        {
            InputStream sourceStream = new FileInputStream(new File(sourceImage));
            SdkBytes sourceBytes = SdkBytes.fromInputStream(sourceStream);
            Image souImage = Image.builder()
                    .bytes(sourceBytes)
                    .build();

            SearchFacesByImageRequest facesByImageRequest = SearchFacesByImageRequest.builder()
                    .image(souImage)
                    .maxFaces(10)
                    .faceMatchThreshold(70F)
                    .collectionId(collectionId)
                    .build();

            SearchFacesByImageResponse imageResponse = rekClient.searchFacesByImage(facesByImageRequest);
            List<FaceMatch> faceImageMatches = imageResponse.faceMatches();
            String faceId = null;
            // take only the first match
            for (FaceMatch face : faceImageMatches)
            {
                faceId = face.face().faceId();
            }
            return faceId;
        }

        catch (RekognitionException | FileNotFoundException e)
        {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }
}
