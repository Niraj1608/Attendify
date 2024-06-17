package com.project;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.IndexFacesResponse;
import software.amazon.awssdk.services.rekognition.model.IndexFacesRequest;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.QualityFilter;
import software.amazon.awssdk.services.rekognition.model.Attribute;
import software.amazon.awssdk.services.rekognition.model.FaceRecord;
import software.amazon.awssdk.services.rekognition.model.UnindexedFace;
import software.amazon.awssdk.services.rekognition.model.RekognitionException;
import software.amazon.awssdk.services.rekognition.model.Reason;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import com.project.constants.Constants;

public class AddFacesToCollection
{
    public static String add(String img)
    {
        String collectionId = Constants.collectionId;
        String sourceImage = img;
        Region region = Region.AP_SOUTH_1;
        RekognitionClient client = RekognitionClient.builder()
                .region(region)
                .build();

        String result = addToCollection(client, collectionId, sourceImage);
        client.close();
        return result;
    }

    public static String addToCollection(RekognitionClient client, String collectionId, String sourceImage)
    {
        String faceId = "";
        try
        {
            InputStream sourceStream = new FileInputStream(sourceImage);
            SdkBytes sourceBytes = SdkBytes.fromInputStream(sourceStream);
            Image image = Image.builder()
                    .bytes(sourceBytes)
                    .build();

            IndexFacesRequest request = IndexFacesRequest.builder()
                    .collectionId(collectionId)
                    .image(image)
                    .maxFaces(1)
                    .qualityFilter(QualityFilter.AUTO)
                    .detectionAttributes(Attribute.DEFAULT)
                    .build();

            IndexFacesResponse response = client.indexFaces(request);
            System.out.println("Results for the Image: ");
            System.out.println("\n Faces Indexed");
            List<FaceRecord> faceRecords = response.faceRecords();
            for (FaceRecord faceRecord : faceRecords)
            {
                System.out.println("  Face ID: " + faceRecord.face().faceId());
                faceId = faceRecord.face().faceId();
                System.out.println("  Location:" + faceRecord.faceDetail().boundingBox().toString());
            }

            List<UnindexedFace> unindexedFaces = response.unindexedFaces();

            for (UnindexedFace unindexedFace : unindexedFaces) {
                System.out.println("Faces not indexed:");
                System.out.println("  Location:" + unindexedFace.faceDetail().boundingBox().toString());
                System.out.println("  Reasons:");
                for (Reason reason : unindexedFace.reasons()) {
                    System.out.println("Reason:  " + reason);
                }
            }
        }

        catch (RekognitionException | FileNotFoundException e) {
            System.out.println("Exception: " + e);
            System.exit(1);
        }
        return faceId;
    }
}
