package com.idetix.identityapprover.service.security;

public interface SecurityService {
    boolean verifyAddressFromSignature(String address, String signature, String message);

    String getAlphaNumericString(int n, boolean numOnly);

    static int getChecksum(String s){
        int checksum = 0;
        int multiplier = 7;
        for(char c : s.toCharArray()){
            int value = Character.getNumericValue(c);
            if (value < 0) {value = 0;}
            int toChecksum = (value*multiplier)%10;
            checksum = checksum + toChecksum;
            if (multiplier == 7) {
                multiplier = 3;
            }
            else if (multiplier == 3) {
                multiplier = 1;
            }
            else {
                multiplier = 7;
            }
        }
        return checksum%10;
    }

}
