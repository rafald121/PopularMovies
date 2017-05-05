package com.example.admin.myapplication.Utils;

import android.content.Context;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

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

    public static boolean isPhoneRotated(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        if(rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180)
            return false;
        else
            return true;
    }


}
