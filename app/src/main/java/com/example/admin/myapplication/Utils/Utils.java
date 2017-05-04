package com.example.admin.myapplication.Utils;

/**
 * Created by admin on 04.05.2017.
 */

public class Utils {

    public static boolean validateParams(String[] params){
        if(params == null || params[0] == null){
            return false;
        }

        if(params.length==0 && params[0].equals("")){
            return false;
        }

        if(params[0].length() == 0) {
            return false;
        }

        return true;
    }
}
