package com.idetix.identityapprover.service.OCR;

import com.idetix.identityapprover.entity.MRZ;
import com.idetix.identityapprover.service.security.SecurityService;
import lombok.SneakyThrows;
import net.sourceforge.tess4j.Tesseract1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class OCRService {
    final Tesseract1 tesseract;

    @Autowired
    private SecurityService securityService;

    @Autowired
    public OCRService(@Value("${TesseractDataPath}") String tesseractDataPath){
        tesseract = new Tesseract1();
        tesseract.setDatapath(tesseractDataPath);
    }
    @SneakyThrows
    public MRZ doOCR(File file){
        String ocrResult = tesseract.doOCR(file);
        MRZ mrz = new MRZ(ocrResult);
        if (mrz.getIsValid()){
            return mrz;
        }
        else {
            return null;
        }
    }
}
