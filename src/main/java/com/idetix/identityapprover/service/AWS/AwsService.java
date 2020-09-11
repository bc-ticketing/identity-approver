package com.idetix.identityapprover.service.AWS;

import com.amazonaws.services.rekognition.model.Image;

public interface AwsService {
    boolean doFacesMatch(Image source, Image target);
}
