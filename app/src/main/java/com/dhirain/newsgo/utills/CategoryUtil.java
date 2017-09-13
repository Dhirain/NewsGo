package com.dhirain.newsgo.utills;

/**
 * Created by DJ on 14-09-2017.
 */

public class CategoryUtil {
    public static String getCatogeryToDisplay(String catogery){
        switch (catogery){
            case "b":
                return "Business";
            case "t":
                return "Science and Technology";
            case "e":
                return "Entertainment";
            case "m":
                return "Health";
            default:
                return "";
        }
    }

    public static String getCatogeryFromDisplay(String catogery){
        switch (catogery){
            case "Business":
                return "b";
            case "Science and Technology":
                return "t";
            case "Entertainment":
                return "e";
            case "Health":
                return "m";
            default:
                return "";
        }
    }
}
