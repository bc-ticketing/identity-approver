package com.idetix.identityapprover.service.AWS;

import com.amazonaws.services.rekognition.model.Image;
import com.idetix.identityapprover.entity.MRZ;

import java.io.File;

public interface AwsService {
    boolean doFacesMatch(Image source, Image target);
    MRZ doOCR(Image source);
}
