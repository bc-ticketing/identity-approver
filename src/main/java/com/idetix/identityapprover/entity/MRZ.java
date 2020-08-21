package com.idetix.identityapprover.entity;

import com.idetix.identityapprover.service.security.SecurityService;
import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class MRZ {
    private MRZType type;
    private String mrz;

    public MRZ(String input){
        Pattern patternTD1 = Pattern.compile("(I|C|A).[A-Z0<]{3}[A-Z0-9]{1,9}<?[0-9O]{1}[A-Z0-9<]{14,22}\\n[0-9O]{7}(M|F|<)[0-9O]{7}[A-Z0<]{3}[A-Z0-9<]{11}[0-9O]\\n([A-Z0]+<)+<([A-Z0]+<)+<+", Pattern.CASE_INSENSITIVE);
        Matcher matcherTD1 = patternTD1.matcher(input);
        if( matcherTD1.find()){
            this.mrz = matcherTD1.group().toUpperCase();
            this.type= MRZType.TD1;
        }

        Pattern patternTD2 = Pattern.compile("[A-Z0]{1,2}<?[A-Z0<]{3}([A-Z0]+<)+<([A-Z0]+<)+<+\\n[A-Z0-9]{1,9}.*[A-Z0<]{3}[0-9O]{7}(M|F|<)[0-9O]{7}[A-Z0-9<]*", Pattern.CASE_INSENSITIVE);
        Matcher matcherTD2 = patternTD2.matcher(input);
        if( matcherTD2.find()){
            this.mrz = matcherTD2.group().toUpperCase();
            this.type= MRZType.TD2;
        }

        Pattern patternTD3 = Pattern.compile("P.[A-Z0<]{3}([A-Z0]+<)+<([A-Z0]+<)+<+\\n[A-Z0-9]{1,9}<?[0-9O]{1}[A-Z0<]{3}[0-9]{7}(M|F|<)[0-9O]{7}[A-Z0-9<]+", Pattern.CASE_INSENSITIVE);
        Matcher matcherTD3 = patternTD3.matcher(input);
        if( matcherTD3.find()){
            this.mrz = matcherTD3.group().toUpperCase();
            this.type= MRZType.TD3;
        }

        Pattern patternSwissDriver = Pattern.compile("[A-Z0-9]{3}[0-9O]{3}(D|F|I|R)<<\\n[A-Z0]{1,2}<?(CHE|LIE)[0-9O]{12}<<[0-9O]{6}<*\\n([A-Z0]+<)+<([A-Z0]+<)+<+", Pattern.CASE_INSENSITIVE);
        Matcher matcherSwissDriver = patternSwissDriver.matcher(input);
        if( matcherSwissDriver.find()){
            this.mrz = matcherSwissDriver.group().toUpperCase();
            this.type= MRZType.Swiss_Driving_License;
        }
    }

    public boolean isValid() {
        if (this.type == MRZType.TD1) {
            String[] mrzParts = mrz.split("\n");
            String authNum = mrzParts[0].substring(5, 9);
            String conNum = mrzParts[0].substring(9, 14);
            String proof1 = mrzParts[0].substring(14, 15);
            boolean valid1 = Integer.parseInt(proof1) == SecurityService.getChecksum(authNum + conNum);

            String gebDat = mrzParts[1].substring(0, 6);
            String proof2 = mrzParts[1].substring(6, 7);
            boolean valid2 = Integer.parseInt(proof2) == SecurityService.getChecksum(gebDat);

            String sex = mrzParts[1].substring(7, 8);
            String valDat = mrzParts[1].substring(8, 14);
            String proof3 = mrzParts[1].substring(14, 15);
            boolean valid3 = Integer.parseInt(proof3) == SecurityService.getChecksum(valDat);

            String proof4 = mrzParts[1].substring(29, 30);
            boolean valid4 = Integer.parseInt(proof4) == SecurityService.getChecksum(authNum + conNum + proof1 + gebDat + proof2 + valDat + proof3);
            return valid1 && valid2 && valid3 && valid4;
        }
        return false;
    }


}
