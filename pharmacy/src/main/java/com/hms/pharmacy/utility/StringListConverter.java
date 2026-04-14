package com.hms.pharmacy.utility;

import java.util.List;

public class StringListConverter {
    public static String convertListToString(List<String> list){
        if(list==null || list.isEmpty()){
            return "";
        }
        return String.join(",",list);
    }

    public static List<String> convertStringToList(String string){
        if(string==null || string.isEmpty()){
            return List.of();
        }
        return List.of(string.split(","));
    }
}
