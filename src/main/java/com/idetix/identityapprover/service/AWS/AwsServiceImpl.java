package com.idetix.identityapprover.service.AWS;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Profile("!dev")
@Service
public class AwsServiceImpl implements AwsService {
    final AmazonRekognition amazonRekognition;
    final Float similarityThreshold;


    @Autowired
    public AwsServiceImpl (
            @Value("${aws.similarityThreshold}") String similarityThreshold,
            @Value("${aws.access-key}") String accessKey,
            @Value("${aws.secret-key}") String secretKey
            ) {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        amazonRekognition = AmazonRekognitionClientBuilder.standard()
                .withRegion(Regions.EU_WEST_1)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
        this.similarityThreshold = Float.valueOf(similarityThreshold);
    }


    @Override
    public boolean doFacesMatch(Image source, Image target) {
        CompareFacesRequest request = new CompareFacesRequest()
                .withSourceImage(source)
                .withTargetImage(target)
                .withSimilarityThreshold(similarityThreshold);

        CompareFacesResult compareFacesResult = amazonRekognition.compareFaces(request);

        List<CompareFacesMatch> faceDetails = compareFacesResult.getFaceMatches();
        return faceDetails.size() > 0;
    }
}
