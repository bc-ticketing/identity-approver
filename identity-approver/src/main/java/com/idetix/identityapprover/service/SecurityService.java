package com.idetix.identityapprover.service;

import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;

public class SecurityService {


    public static final String PERSONAL_MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";

    public static boolean verifyAddressFromSignature(String address, String signature, String message)
    {
        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        String prefix = PERSONAL_MESSAGE_PREFIX + message.length();
        byte[] msgHash = Hash.sha3((prefix + message).getBytes());
        byte v = signatureBytes[64];
        if(v < 27)
        {
            v += 27;
        }
        Sign.SignatureData sd = new Sign.SignatureData(
                v,
                (byte[]) Arrays.copyOfRange(signatureBytes, 0, 32),
                (byte[]) Arrays.copyOfRange(signatureBytes, 32, 64));
        String addressRecovered = null;
        boolean match = false;
        // Iterate for each possible key to recover
        for (int i = 0; i < 4; i++)
        {
            BigInteger publicKey = Sign.recoverFromSignature(
                    (byte) i,
                    new ECDSASignature(new BigInteger(1, sd.getR()), new BigInteger(1, sd.getS())),
                    msgHash);
            if (publicKey != null)
            {
                addressRecovered = "0x" + Keys.getAddress(publicKey);
                if (addressRecovered.equals(address))
                {
                    match = true;
                    break;
                }
            }
        }
        return match;
    }
    static String getAlphaNumericString(int n)
    {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }
}
