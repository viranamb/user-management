package com.capitalone.migration.utils;

/**
 * Created by msz519 on 11/10/17.
 */
public enum CountryEnum {

    US, CA, UK;

    public static boolean isValidEnum(String countryCode){
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
