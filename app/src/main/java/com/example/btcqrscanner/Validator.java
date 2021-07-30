package com.example.btcqrscanner;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    public static final int NOT_KNOWN = 0;

    public static final int PRIVATE_KEY_HEX = 1;
    public static final int PRIVATE_KEY_DEC = 2;
    public static final int PRIVATE_KEY_WIF_COMPRESSED = 3;
    public static final int PRIVATE_KEY_WIF_UNCOMPRESSED = 4;

    public static final int PUBLIC_KEY_COMPRESSED = 5;
    public static final int PUBLIC_KEY_UNCOMPRESSED = 6;

    public static final int ADDRESS = 7;
    public static final int ADDRESS_BECH32 = 8;
    public static final int ADDRESS_P2SH= 9;

    private static final Pattern HEXADECIMAL_PATTERN = Pattern.compile("\\p{XDigit}+");

    private static boolean isHexadecimal(String input) {
        final Matcher matcher = HEXADECIMAL_PATTERN.matcher(input);
        return matcher.matches();
    }

    private static boolean isNumeric(String strNum) {
        try {
            new BigInteger(strNum, 10);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static int validate(String string){
        if (isHexadecimal(string) && string.length() == 64){
            return PRIVATE_KEY_HEX;
        } else if (isNumeric(string)){
            return PRIVATE_KEY_DEC;
        } else if (string.length() == 51 && string.startsWith("5")){
            return PRIVATE_KEY_WIF_UNCOMPRESSED;
        } else if (string.length() == 52 && (string.startsWith("L") || string.startsWith("K"))){
            return PRIVATE_KEY_WIF_COMPRESSED;
        } else if (string.length() >= 26 && string.length() <= 35){
            if (string.startsWith("1")){
                return ADDRESS;
            } else if (string.startsWith("3")){
                return ADDRESS_P2SH;
            } else if (string.startsWith("bc")){
                return ADDRESS_BECH32;
            }
        } else if (string.startsWith("03")){
            return PUBLIC_KEY_COMPRESSED;
        } else if (string.startsWith("04")){
            return PUBLIC_KEY_UNCOMPRESSED;
        }

        return NOT_KNOWN;
    }

    public static boolean isAddress(int type){
        return type == ADDRESS || type == ADDRESS_BECH32 || type == ADDRESS_P2SH;
    }

    public static boolean isAddress(String string){
        return isAddress(validate(string));
    }


    public static boolean isPrivateKey(int type){
        return type == PRIVATE_KEY_DEC || type == PRIVATE_KEY_HEX ||
                type == PRIVATE_KEY_WIF_COMPRESSED || type == PRIVATE_KEY_WIF_UNCOMPRESSED;
    }

    public static boolean isPrivateKey(String string){
        return isPrivateKey(validate(string));
    }

    public static boolean isPublicKey(int type){
        return type == PUBLIC_KEY_COMPRESSED || type == PUBLIC_KEY_UNCOMPRESSED;
    }

    public static boolean isPublicKey(String string){
        return isPublicKey(validate(string));
    }



}
