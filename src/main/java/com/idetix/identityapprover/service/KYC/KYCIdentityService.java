package com.idetix.identityapprover.service.KYC;

import com.amazonaws.services.rekognition.model.Image;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idetix.identityapprover.entity.EmailIdentity;
import com.idetix.identityapprover.entity.Exceptions.MRZException;
import com.idetix.identityapprover.entity.KYCIdentity;
import com.idetix.identityapprover.entity.MRZ;
import com.idetix.identityapprover.entity.MRZType;
import com.idetix.identityapprover.repository.KYCIdentityRepository;
import com.idetix.identityapprover.service.AWS.AwsService;
import com.idetix.identityapprover.service.OCR.OCRService;
import com.idetix.identityapprover.service.blockchain.BlockchainService;
import com.idetix.identityapprover.service.security.SecurityService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;

@Service
public class KYCIdentityService {
    @Autowired
    private KYCIdentityRepository repository;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private OCRService ocrService;
    @Autowired
    private AwsService awsService;
    @Autowired
    private BlockchainService blockchainService;

    public MRZ addKYCIdentity(Image source, Image target, File mrzSource) throws MRZException {
        MRZ mrz = ocrService.doOCR(mrzSource);
//        Input ist kein Foto von MRZ
        if (mrz == null){
            throw new MRZException("Provided Foto is not a Picture of MRZ or OCR failed");
        }
        if (awsService.doFacesMatch(source,target)){
            KYCIdentity kycIdentity = repository.findById(mrz.getMrz()).orElse(null);
            if (kycIdentity == null){
                kycIdentity = new KYCIdentity(mrz.getMrz(),mrz.getIdNumber(),mrz.getType(),mrz.getIsValid(),mrz.getDate(),null ,false);
                repository.save(kycIdentity);
            }else{
                if(kycIdentity.getVerified()){
                    return null;
                }else{
                    kycIdentity = new KYCIdentity(mrz.getMrz(),mrz.getIdNumber(),mrz.getType(),mrz.getIsValid(),mrz.getDate(),null ,false);
                    repository.save(kycIdentity);
                }
            }
            return mrz;
        }else {
            mrz.setIsValid(false);
            return mrz;
        }

    }

    @SneakyThrows
    public KYCIdentity verifyKYCIdentity(MRZ mrz, String signature, String ethAddress){
        KYCIdentity kycIdentity = getKYCIdentityById(mrz.getMrz());
        MRZ mrzFromDB = new MRZ(kycIdentity.getIdNumber(),kycIdentity.getType(),kycIdentity.getMrz(),kycIdentity.getIsValid(),kycIdentity.getDate());
        ObjectMapper mapper = new ObjectMapper();
        if(securityService.verifyAddressFromSignature(ethAddress,signature,mapper.writeValueAsString(mrzFromDB))){
            if (blockchainService.getSecurityLevelForAddress(ethAddress) < 1) {
                if (blockchainService.saveIdentityProofToChain(ethAddress, 1)) {
                    kycIdentity.setVerified(true);
                    kycIdentity.setEthAddress(ethAddress);
                    repository.save(kycIdentity);
                }
            }
        }
        return kycIdentity;
    }





    private KYCIdentity getKYCIdentityById(String mrz) {
        KYCIdentity kycIdentity = repository.findById(mrz).orElse(null);
        if (kycIdentity == null) {
            throw new IllegalArgumentException("The repository does not contain an KYC identity for the MRZ" +
                    " `" + mrz + "`.");
        }
        return kycIdentity;
    }

}
