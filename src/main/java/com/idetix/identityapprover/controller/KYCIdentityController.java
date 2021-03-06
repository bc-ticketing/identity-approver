package com.idetix.identityapprover.controller;

import com.amazonaws.services.rekognition.model.Image;
import com.idetix.identityapprover.entity.Exceptions.MRZException;
import com.idetix.identityapprover.entity.KYCIdentity;
import com.idetix.identityapprover.entity.MRZ;
import com.idetix.identityapprover.service.KYC.KYCIdentityService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.nio.ByteBuffer;

@RestController
public class KYCIdentityController {

    @Autowired
    private KYCIdentityService kycIdentityService;

    @SneakyThrows
    @PostMapping("/AddKYCIdentity")
    public MRZ addKYCIdentity(@RequestParam("mrz") MultipartFile mrz, @RequestParam("front") MultipartFile front, @RequestParam("selfie") MultipartFile selfie){
        File mrzFile = new File(System.getProperty("java.io.tmpdir")+"/"+mrz.getOriginalFilename());
        mrz.transferTo(mrzFile);
        Image frontImage = new Image().withBytes(ByteBuffer.wrap(front.getBytes()));
        Image selfieImage = new Image().withBytes(ByteBuffer.wrap(selfie.getBytes()));
        try {
            return kycIdentityService.addKYCIdentity(frontImage,selfieImage,mrzFile);
        }catch (MRZException e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }

    }

    @PostMapping("/KYCIdentity")
    public KYCIdentity approveKYCIdentity(@RequestParam MRZ mrz,@RequestParam String signature, @RequestParam String ethAddress){
        return kycIdentityService.verifyKYCIdentity(mrz, signature, ethAddress);
    }

}
