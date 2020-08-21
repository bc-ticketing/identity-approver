package com.idetix.identityapprover.controller;

import com.idetix.identityapprover.service.OCRService.OCRService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
public class OCRController {

    @Autowired
    private OCRService service;

    @SneakyThrows
    @PostMapping("/DoOCR")
    public String getOCRresult(@RequestParam("file") MultipartFile multipartFile){
        File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+multipartFile.getOriginalFilename());
        multipartFile.transferTo(convFile);
        return service.doOCR(convFile);
    }

}
