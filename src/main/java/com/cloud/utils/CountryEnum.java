package com.cloud.utils;

/**
 * Enum for countries
 */
public enum CountryEnum {

    US, CA, UK;

    public static boolean isValidEnum(String countryCode) {
        if (countryCode == null) {
            return false;
        }
        try {
            CountryEnum.valueOf(countryCode);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

}
